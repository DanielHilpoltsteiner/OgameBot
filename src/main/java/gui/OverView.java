package gui;

import comp.Buildable;
import comp.Player;
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
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;
import units.Building.Building;
import units.Building.PlanetBuilding;
import units.UnitType;
import units.astroObjects.Planet;
import units.astroObjects.PlanetBuilder;
import units.units.Resource;
import units.warfare.DefenceType;
import units.warfare.DefenceUnit;
import units.warfare.Ship;
import units.warfare.ShipType;

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
    private TextField planetTextField;

    @FXML
    private Button addPlanetBtn;

    @FXML
    private Text metalValue;

    @FXML
    private Text crystalValue;

    @FXML
    private Text deutValue;

    private ObjectProperty<Player> currentPlayer = new SimpleObjectProperty<>();
    @FXML
    private Hyperlink detailsLink;
    private Map<UnitType, TextField> fields = new HashMap<>();
    @FXML
    private ListView<Planet> planetList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        detailsLink.ellipsisStringProperty().bind(detailsLink.textProperty());
        bindButton();
        initGrids();
        load();
    }

    @FXML
    void addPlanet() {
        if (currentPlayer.get() != null) {
            String text = planetTextField.getText();

            Planet planet = new PlanetBuilder(text).createPlanet();

            if (!planetList.getItems().contains(planet)) {
                currentPlayer.get().getPlanets().add(planet);
            }

            planetTextField.clear();
        } else {
            new ErrorPopup(planetTextField, "PLAYER IS NULL").showError();
        }
    }

    @FXML
    void showDetails() {
        detailsLink.setVisited(false);
        detailsLink.setText("Normal");

        // TODO: 09.11.2017 calculate the point value of each building and show it in new column
        // TODO: 09.11.2017 show grid in table-form, with "column titles"
        // TODO: 09.11.2017 show "production" of each building (production of mines, reduction for accelerators, power of machines)
        // TODO: 09.11.2017 show energy, available vs used
    }

    private void bindButton() {
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
    }

    private void fillGrid(UnitType[] values, GridPane grid, String numberFieldCss, int maxNumber, int maxChars) {
        for (int i = 0; i < values.length; i++) {
            UnitType value = values[i];

            Text text = new Text(value.getName());
            text.getStyleClass().add("overViewGridText");
            text.getStyleClass().add("overViewText");


            grid.addRow(i);
            grid.add(text, 0, i);

//            TextField numberField = getNumberTextField(maxChars);
            Spinner<Integer> numberField = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxNumber, 0));
            numberField.getEditor().setTextFormatter(getTextFormatter(maxChars));
            numberField.setEditable(true);
            numberField.getStyleClass().add(numberFieldCss);
            // TODO: 09.11.2017 checkbox for shields
            grid.add(numberField, 1, i);

//            fields.put(value, numberField);
        }
    }

    private TextField getNumberTextField(final int maxChars) {
        TextField field = new TextField();
        field.setTextFormatter(getTextFormatter(maxChars));
        return field;
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
            bindProperties();
            planetList.setItems(player.getPlanets());
        }
    }

    private void bindProperties() {
        planetList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (oldValue != null) {
                    unbindOld(oldValue);
                }
//                bindNew(newValue);
            }
        });

    }

    private void unbindOld(Planet planet) {
        operateBinding(planet, Bindings::unbindBidirectional);
    }

    private void bindNew(Planet planet) {
        operateBinding(planet, (stringProperty, property) ->
                Bindings.bindBidirectional(
                        stringProperty,
                        property,
                        new NumberStringConverter()));
    }

    private void operateBinding(Planet planet, BiConsumer<StringProperty, IntegerProperty> bindOperation) {
        List<UnitType<Building>> buildings = new ArrayList<>(Arrays.asList(PlanetBuilding.values()));
        List<UnitType<Ship>> shipUnits = new ArrayList<>(Arrays.asList(ShipType.values()));
        List<UnitType<DefenceUnit>> defenceUnits = new ArrayList<>(Arrays.asList(DefenceType.values()));

        operateBindUnitTypes(buildings, unitType -> planet.getBuilding((PlanetBuilding) unitType), bindOperation);
        operateBindUnitTypes(shipUnits, unitType -> planet.getShip((ShipType) unitType), bindOperation);
        operateBindUnitTypes(defenceUnits, unitType -> planet.getDefence((DefenceType) unitType), bindOperation);

        Resource resource = planet.getPlanetResource();

        metalValue.textProperty().bind(resource.metalProperty().asString());
        crystalValue.textProperty().bind(resource.crystalProperty().asString());
        deutValue.textProperty().bind(resource.deutProperty().asString());
    }

    private <E extends Buildable> void operateBindUnitTypes(List<UnitType<E>> unitTypes, Function<UnitType<E>, E> function, BiConsumer<StringProperty, IntegerProperty> bindOperation) {
        for (UnitType<E> unitType : unitTypes) {
            E unit = function.apply(unitType);
            TextField field = fields.get(unitType);

            if (field != null) {
                bindOperation.accept(field.textProperty(), unit.counterProperty());
            }
        }
    }
}
