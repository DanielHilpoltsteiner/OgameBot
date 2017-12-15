package ogamebot.gui.overview;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;
import ogamebot.calc.CustomBindings;
import ogamebot.comp.Player;
import ogamebot.comp.UpgradeAble;
import ogamebot.gui.DataHolder;
import ogamebot.units.UnitType;
import ogamebot.units.astroObjects.CelestialBody;
import ogamebot.units.building.BuildingType;
import ogamebot.units.research.ResearchField;
import ogamebot.units.units.Resource;
import ogamebot.units.warfare.DefenceType;
import ogamebot.units.warfare.DefenceUnit;
import ogamebot.units.warfare.ShipType;

import java.net.URL;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static ogamebot.gui.GuiTools.getTextFormatter;

/**
 *
 */
public class OverView implements Initializable {
    @FXML
    private GridPane buildingGrid;

    @FXML
    private GridPane shipGrid;

    @FXML
    private GridPane defenceGrid;

    @FXML
    private GridPane researchGrid;

    @FXML
    private Text metalValue;

    @FXML
    private Text crystalValue;

    @FXML
    private Text deutValue;

    @FXML
    private VBox planetContent;

    @FXML
    private Text scoreValue;

    @FXML
    private Text darkMatterValue;


    @FXML
    private Hyperlink detailsLink;

    private ObjectProperty<Player> currentPlayer = new SimpleObjectProperty<>();
    private ReadOnlyObjectProperty<CelestialBody> body = DataHolder.getInstance().currentBodyProperty();

    private Map<UnitType, Spinner<Integer>> fields = new HashMap<>();
    private Map<UnitType, CheckBox> boxMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        detailsLink.ellipsisStringProperty().bind(detailsLink.textProperty());
        planetContent.disableProperty().bind(body.isNull());
        initGrids();
        load();
    }

    private DetailMode detailMode = DetailMode.NORMAL;

    @FXML
    void showDetails() {
        detailsLink.setVisited(false);

        if (detailMode == DetailMode.NORMAL) {
            detailMode = DetailMode.DETAILS;

            detailsLink.setText("Normal");
        } else {
            detailMode = DetailMode.NORMAL;
        }
        detailMode.change(this);
        // TODO: 09.11.2017 calculate the point value of each building and show it in new column
        // TODO: 09.11.2017 show grid in table-form, with "column titles"
        // TODO: 09.11.2017 show "production" of each building (production of mines, reduction for accelerators, power of machines)
        // TODO: 09.11.2017 show energy, available vs used
    }

    private void fillGrid(UnitType[] values, GridPane grid, String numberFieldCss, int maxNumber, int maxChars) {
        for (int i = 0; i < values.length; i++) {
            UnitType value = values[i];

            Text text = new Text(value.getName());
            text.getStyleClass().add("overViewGridText");
            text.getStyleClass().add("overViewText");


            Node node;
            if (value == DefenceType.GREAT_SHIELD || value == DefenceType.SMALL_SHIELD) {
                final CheckBox box = new CheckBox();
                boxMap.put(value, box);
                node = box;
            } else {
                Spinner<Integer> numberField = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxNumber, 0));
                numberField.getEditor().setTextFormatter(getTextFormatter(maxChars));
                numberField.setEditable(true);
                numberField.getStyleClass().add(numberFieldCss);
                node = numberField;
                fields.put(value, numberField);
            }
            grid.add(text, 0, i);
            grid.add(node, 1, i);
        }
    }

    private void load() {
        Player player = DataHolder.getInstance().getCurrentPlayer();
        if (player != null) {
            loadData(player);
        }
        DataHolder.getInstance().addPlayerListener((observable, oldValue, newValue) -> loadData(newValue));
    }

    private void initGrids() {
        fillGrid(BuildingType.values(), buildingGrid, "smallNumberField", 99, 2);
        fillGrid(ShipType.values(), shipGrid, "middleNumberField", 999999, 6);
        fillGrid(DefenceType.values(), defenceGrid, "middleNumberField", 999999, 6);
        fillGrid(ResearchField.values(), researchGrid, "middleNumberField", 99, 2);
    }

    private void bindProperties() {
        body.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (oldValue != null) {
                    unbindOld(oldValue);
                }
                bindNew(newValue);
            }
        });

    }

    private void loadData(Player player) {
        currentPlayer.set(player);


        if (player != null) {
            scoreValue.textProperty().bind(player.playerPlaceProperty().asString());
            darkMatterValue.textProperty().bind(player.darkMatterProperty().asString());

            bindProperties();
        }
    }

    private <E extends UpgradeAble, R extends UnitType<E>> void operateBindUnitTypes(Collection<R> unitTypes, Function<R, E> function, BiConsumer<StringProperty, IntegerProperty> bindOperation) {
        for (R unitType : unitTypes) {
            E unit = function.apply(unitType);
            Spinner<Integer> field = fields.get(unitType);

            if ((unitType == DefenceType.SMALL_SHIELD || unitType == DefenceType.GREAT_SHIELD)) {
                if (unit instanceof DefenceUnit) {
                    final CheckBox checkBox = boxMap.get(unitType);
                    CustomBindings.bindBidirectional(unit.counterProperty(), checkBox.selectedProperty());
                }
            } else if (field != null) {
                bindOperation.accept(field.getEditor().textProperty(), unit.counterProperty());
            }
        }
    }

    private void unbindOld(CelestialBody body) {
        operateBinding(body, Bindings::unbindBidirectional);
    }

    private void bindNew(CelestialBody body) {
        operateBinding(body, (stringProperty, property) ->
                Bindings.bindBidirectional(stringProperty, property, new NumberStringConverter()));
    }

    private void operateBinding(CelestialBody body, BiConsumer<StringProperty, IntegerProperty> bindOperation) {
        final Set<BuildingType> buildingTypes = body.getBuilding().getTypes();
        final Set<ShipType> shipTypes = body.getFleet().getTypes();
        final Set<DefenceType> defenceTypes = body.getDefence().getTypes();

        operateBindUnitTypes(buildingTypes, body::getBuilding, bindOperation);
        operateBindUnitTypes(shipTypes, body::getShip, bindOperation);
        operateBindUnitTypes(defenceTypes, body::getDefence, bindOperation);

        ObjectProperty<Resource> resource = body.resourceProperty();

        metalValue.textProperty().bind(Bindings.createStringBinding(() -> resource.get().getMetal().toString(), resource));
        crystalValue.textProperty().bind(Bindings.createStringBinding(() -> resource.get().getCrystal().toString(), resource));
        deutValue.textProperty().bind(Bindings.createStringBinding(() -> resource.get().getDeut().toString(), resource));
    }

    private enum DetailMode {
        NORMAL {
            @Override
            void change(OverView view) {
                view.detailsLink.setText("Erweitert");
                GridPane buildingGrid = view.buildingGrid;
                texts.forEach(text -> buildingGrid.getChildren().remove(text));
            }
        },
        DETAILS {
            @Override
            void change(OverView view) {
                view.detailsLink.setText("Normal");
                GridPane buildingGrid = view.buildingGrid;

                for (int i = 0; i < buildingGrid.getRowCount(); i++) {
                    Text text;

                    if (texts.size() <= i) {
                        text = new Text("hi");
                        texts.add(text);
                    } else {
                        text = texts.get(i);
                    }
                    if (!buildingGrid.getChildren().contains(text)) {
                        buildingGrid.add(text, 2, i);
                    }
                }
            }
        };
        static List<Text> texts = new ArrayList<>();

        abstract void change(OverView view);
    }
}
