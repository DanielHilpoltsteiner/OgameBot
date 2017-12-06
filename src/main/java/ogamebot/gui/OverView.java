package ogamebot.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;
import ogamebot.comp.Player;
import ogamebot.comp.Requirement;
import ogamebot.comp.UpgradeAble;
import ogamebot.tools.CustomBindings;
import ogamebot.units.UnitType;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.astroObjects.PlanetBuilder;
import ogamebot.units.building.Building;
import ogamebot.units.building.PlanetBuilding;
import ogamebot.units.research.ResearchFields;
import ogamebot.units.units.Resource;
import ogamebot.units.warfare.DefenceType;
import ogamebot.units.warfare.DefenceUnit;
import ogamebot.units.warfare.Ship;
import ogamebot.units.warfare.ShipType;

import java.net.URL;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

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
    private TextField planetTextField;

    @FXML
    private Button addPlanetBtn;

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

    private ObjectProperty<Player> currentPlayer = new SimpleObjectProperty<>();
    @FXML
    private Hyperlink detailsLink;
    private Map<UnitType, Spinner<Integer>> fields = new HashMap<>();
    @FXML
    private ListView<Planet> planetList;
    private Map<UnitType, CheckBox> boxMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        detailsLink.ellipsisStringProperty().bind(detailsLink.textProperty());
        planetContent.disableProperty().bind(planetList.getSelectionModel().selectedItemProperty().isNull());
        bind();
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

    @FXML
    void addPlanet() {
        if (currentPlayer.get() != null) {
            String text = planetTextField.getText();

            Planet planet = new PlanetBuilder(text).setPlayer(currentPlayer.get()).createPlanet();

            if (!planetList.getItems().contains(planet)) {
                currentPlayer.get().getPlanets().add(planet);
                planetList.getSelectionModel().select(planet);
            }

            planetTextField.clear();
        } else {
            new ErrorPopup(planetTextField, "PLAYER IS NULL").showError();
        }
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

    private void bind() {
        BooleanExpression expression = BooleanExpression.
                booleanExpression(planetTextField.textProperty().isEmpty())
                .or(currentPlayer.isNull());

        //wrapper object for lambda
        ObjectProperty<BooleanExpression> expressionObjectProperty = new SimpleObjectProperty<>(expression);

        //allow only planets with unique name
        planetList.itemsProperty().addListener(observable -> {
            planetList.getItems().addListener((ListChangeListener<? super Planet>) c -> {
                if (c.next()) {
                    c.getAddedSubList().forEach(planet -> {
                        BooleanBinding equalTo = planet.nameProperty().isEqualTo(planetTextField.textProperty());
                        BooleanExpression booleanExpression = expressionObjectProperty.get().or(equalTo);
                        expressionObjectProperty.set(booleanExpression);

                        addPlanetBtn.disableProperty().bind(booleanExpression);
                    });
                }
            });
        });
        addPlanetBtn.disableProperty().bind(expression);
    }

    private void load() {
        Player player = DataHolder.getInstance().getCurrentPlayer();

        if (player != null) {
            loadData(player);
        }

        planetList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Planet item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        DataHolder.getInstance().addListener((observable, oldValue, newValue) -> loadData(newValue));
    }

    private void initGrids() {
        fillGrid(PlanetBuilding.values(), buildingGrid, "smallNumberField", 99, 2);
        fillGrid(ShipType.values(), shipGrid, "middleNumberField", 999999, 6);
        fillGrid(DefenceType.values(), defenceGrid, "middleNumberField", 999999, 6);
        fillGrid(ResearchFields.values(), researchGrid, "middleNumberField", 99, 2);
    }

    private void bindProperties() {
        planetList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (oldValue != null) {
                    unbindOld(oldValue);
                }
                bindNew(newValue);
            }
        });

    }

    private TextFormatter<String> getTextFormatter(int maxChars) {
        return new TextFormatter<>(c -> {
            if (c.isContentChange()) {
                int newLength = c.getControlNewText().length();
                if (!c.getControlNewText().matches("[0-9]*")) {
                    c.setText(c.getControlText());
                }
                if (c.getControlNewText().isEmpty()) {
                    c.setText("0");
                    // TODO: 09.11.2017 do sth better
                }
                if (newLength > maxChars) {
                    c.setText(c.getControlText());
                    c.setRange(0, maxChars);
                }
            }
            return c;
        });
    }

    private void loadData(Player player) {
        currentPlayer.set(player);


        if (player != null) {
            scoreValue.textProperty().bind(player.highscoreProperty().asString());
            darkMatterValue.textProperty().bind(player.darkMatterProperty().asString());

            bindProperties();
            planetList.setItems(player.getPlanets());
        }
    }

    private <E extends UpgradeAble> void operateBindUnitTypes(Planet planet, List<UnitType<E>> unitTypes, Function<UnitType<E>, E> function, BiConsumer<StringProperty, IntegerProperty> bindOperation) {
        for (UnitType<E> unitType : unitTypes) {
            E unit = function.apply(unitType);
            Spinner<Integer> field = fields.get(unitType);

            if ((unitType == DefenceType.SMALL_SHIELD || unitType == DefenceType.GREAT_SHIELD)) {
                if (unit instanceof DefenceUnit) {
                    final CheckBox checkBox = boxMap.get(unitType);
                    CustomBindings.bindBidirectional(unit.counterProperty(), checkBox.selectedProperty());
                }
            } else if (field != null) {
                final Requirement requirement = unit.getType().getRequirement();
                unit.counterProperty().addListener((observable) -> {
                    if (!requirement.check(planet)) {
                        field.setDisable(true);
                    }
                });
                field.disableProperty().bind(Bindings.createBooleanBinding(() -> !requirement.check(planet)));
                bindOperation.accept(field.getEditor().textProperty(), unit.counterProperty());
            }
        }
    }

    private void unbindOld(Planet planet) {
        operateBinding(planet, Bindings::unbindBidirectional);
    }

    private void bindNew(Planet planet) {
        operateBinding(planet, (stringProperty, property) ->
                Bindings.bindBidirectional(stringProperty, property, new NumberStringConverter()));
    }

    private void operateBinding(Planet planet, BiConsumer<StringProperty, IntegerProperty> bindOperation) {
        List<UnitType<Building>> buildings = new ArrayList<>(Arrays.asList(PlanetBuilding.values()));
        List<UnitType<Ship>> shipUnits = new ArrayList<>(Arrays.asList(ShipType.values()));
        List<UnitType<DefenceUnit>> defenceUnits = new ArrayList<>(Arrays.asList(DefenceType.values()));

        operateBindUnitTypes(planet, buildings, planet::getBuilding, bindOperation);
        operateBindUnitTypes(planet, shipUnits, planet::getShip, bindOperation);
        operateBindUnitTypes(planet, defenceUnits, planet::getDefence, bindOperation);

        Resource resource = planet.getPlanetResource();

        metalValue.textProperty().bind(resource.metalProperty().asString());
        crystalValue.textProperty().bind(resource.crystalProperty().asString());
        deutValue.textProperty().bind(resource.deutProperty().asString());
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
