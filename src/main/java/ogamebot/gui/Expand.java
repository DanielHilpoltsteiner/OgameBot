package ogamebot.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import ogamebot.comp.RequireAble;
import ogamebot.comp.Requirement;
import ogamebot.comp.UpgradeAble;
import ogamebot.gui.queue.QueueItem;
import ogamebot.gui.queue.UpgradeAbleQueue;
import ogamebot.units.UnitType;
import ogamebot.units.astroObjects.CelestialBody;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.warfare.DefenceType;
import ogamebot.units.warfare.ShipType;
import ogamebot.units.warfare.WarfareType;

import java.math.BigInteger;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 *
 */
public class Expand implements Initializable {
    @FXML
    private Text availableAt;

    @FXML
    private Text availableMetal;

    @FXML
    private Text availableCrystal;

    @FXML
    private Text availableDeut;

    @FXML
    private TableView<QueueItem> queueTable;

    private static ObjectProperty<CelestialBody> bodyProperty;
    private static Map<CelestialBody, ObservableMap<UnitType, IntegerProperty>> bodyTypeCountMap;

    @FXML
    private Button queueButton;

    private BiFunction<CelestialBody, UnitType, UpgradeAble> upgradeAbleFunction;
    private UnitType[] values;
    private Map<UnitType, Text> typeTextMap = new HashMap<>();
    private static Map<CelestialBody, List<BooleanBinding>> bindings = new HashMap<>();
    private static Map<CelestialBody, Boolean> bodyInitMap = new HashMap<>();
    private static Map<UnitType, Button> typeButtonMap;
    private static Map<Button, Boolean> disabledMap = new HashMap<>();

    static {
        bodyProperty = new SimpleObjectProperty<>();
        bodyTypeCountMap = new HashMap<>();
        typeButtonMap = new HashMap<>();
    }

    @FXML
    private HBox gridContainer;
    @FXML
    private GridPane primary;
    private Map<UnitType, TextField> typeFieldMap = new HashMap<>();
    private Map<Text, Map<CelestialBody, UpgradeAble>> upgradeAbleMap = new HashMap<>();
    private Map<CelestialBody, UpgradeAbleQueue> bodyQueueMap = new HashMap<>();
    private ObjectProperty<UpgradeAbleQueue> currentQueue = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listener();
        initGui();
    }

    private static MapChangeListener<? super UnitType, ? super IntegerProperty> getRequirementListener() {
        return observable -> {
            final CelestialBody key = bodyProperty.get();
            if (!bodyInitMap.computeIfAbsent(key, body -> false)) {
                if (observable.getMap().size() == typeButtonMap.size()) {
                    typeButtonMap.forEach((k, v) -> {

                        Requirement requirement = k.getRequirement();
                        Set<RequireAble> requirements = requirement.getRequirements();

                        IntegerProperty[] dependencies = requirements.stream().map(Expand::getCounter).toArray(IntegerProperty[]::new);

                        BooleanBinding booleanBinding = Bindings.createBooleanBinding(() -> requirements.stream().allMatch(requireAble -> {
                            IntegerProperty property = getCounter(requireAble);

                            final int level = requirement.get(requireAble);
                            return property != null && property.get() >= level;
                        }), dependencies);

                        bindings.computeIfAbsent(key, body -> new ArrayList<>()).add(booleanBinding);

                        booleanBinding.addListener((observable1) -> {
                            final boolean b = booleanBinding.get();
                            System.out.println("dis-/enabling");
                            System.out.println("for " + k + " " + b);
                            if (!b) {
                                disable(k, v);
                            } else {
                                enable(v);
                            }
                        });
                        booleanBinding.addListener((observable1, oldValue, newValue) -> System.out.println(newValue));
                        bodyInitMap.put(key, true);
                    });
                }
            }
        };
    }

    static void ready(ReadOnlyObjectProperty<Planet> bodyProperty) {
        Expand.bodyProperty.bind(bodyProperty);
    }

    private static IntegerProperty getCounter(UnitType<?> type) {
        return getTypeCountMap().get(type);
    }

    private static ObservableMap<UnitType, IntegerProperty> getTypeCountMap() {
        return bodyTypeCountMap.computeIfAbsent(bodyProperty.get(), body -> {
            final ObservableMap<UnitType, IntegerProperty> observableMap = FXCollections.observableHashMap();
            observableMap.addListener(getRequirementListener());
            return observableMap;
        });
    }

    private static void putCounter(UnitType<?> type, IntegerProperty property) {
        getTypeCountMap().put(type, property);
    }

    private static void disable(UnitType type, Button value) {
        disabledMap.put(value, true);
        final Tooltip tooltip = new Tooltip();

        tooltip.setAutoHide(true);
        tooltip.setGraphic(getRequirementsPane(type));

        Tooltip.install(value, tooltip);
        value.setGraphic(getDisableCross());
    }

    private static GridPane getRequirementsPane(UnitType k) {
        GridPane pane = new GridPane();
        pane.getStyleClass().add("requirement-Pane");

        Requirement requirement = k.getRequirement();

        List<RequireAble> requireAbles = new ArrayList<>(requirement.getRequirements());

        for (int i = 0, requireAblesSize = requireAbles.size(); i < requireAblesSize; i++) {
            RequireAble requireAble = requireAbles.get(i);
            final int needed = requirement.get(requireAble);
            final IntegerProperty property = getCounter(requireAble);

            int available = property != null ? property.get() : 0;

            final Text neededValue = new Text(String.valueOf(needed));
            neededValue.getStyleClass().add("text");
            final Text neededDesc = new Text("Benötigt " + requireAble.getName() + " Stufe:");
            neededDesc.getStyleClass().add("text");

            final Text availableValue = new Text(String.valueOf(available));
            availableValue.getStyleClass().add("text");
            final Text availableDesc = new Text("Vorhanden:");
            availableDesc.getStyleClass().add("text");

            pane.addRow(i, neededDesc, neededValue, availableDesc, availableValue);
        }
        return pane;
    }

    private static void enable(Button value) {
        disabledMap.put(value, false);
        value.setGraphic(getNormalCross());
    }

    private static Shape getNormalCross() {
        Line horizontal = new Line(-4, 0, 4, 0);
        Line vertical = new Line(0, -4, 0, 4);
        final Shape union = Line.union(horizontal, vertical);
        union.getStyleClass().add("normal-cross");
        union.getStyleClass().add("cross");
        return union;
    }

    private static Shape getDisableCross() {
        Line horizontal = new Line(-4, -4, 4, 4);
        Line vertical = new Line(-4, 4, 4, -4);
        final Shape union = Line.union(horizontal, vertical);
        union.getStyleClass().add("disable-cross");
        union.getStyleClass().add("cross");
        return union;
    }

    private void initGui() {
        disable();
        queueButton.setText("Starten");
        availableAt.setText("Leer");
        availableMetal.setText("0");
        availableCrystal.setText("0");
        availableDeut.setText("0");
    }

    @FXML
    void executeQueue() {
        if (getCurrentQueue().isRunning()) {
            getCurrentQueue().stop();
            queueButton.setText("Starten");
        } else {
            getCurrentQueue().start();
            queueButton.setText("Stoppen");
        }
    }

    private void listener() {
        bodyProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                for (UnitType value : values) {
                    bindNextLevel(value, upgradeAbleFunction.apply(newValue, value));
                }
                currentQueue.set(bodyQueueMap.computeIfAbsent(newValue, UpgradeAbleQueue::new));
                bindings.computeIfAbsent(newValue, body -> new ArrayList<>()).forEach(BooleanBinding::invalidate);
            } else {
                disable();
            }
        });
        currentQueue.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                availableAt.textProperty().bind(newValue.queueFinishedProperty());
                availableMetal.textProperty().bind(newValue.availableMetProperty());
                availableCrystal.textProperty().bind(newValue.availableCrysProperty());
                availableDeut.textProperty().bind(newValue.availableDeutProperty());
            }
        });
    }

    void load(UnitType[] values, BiFunction<CelestialBody, UnitType, UpgradeAble> upgradeAbleSupplier) {
        initGrid(values);
        this.values = values;
        this.upgradeAbleFunction = upgradeAbleSupplier;
    }

    private void readyTable(TableView<QueueItem> queueTable) {
        final TableColumn<QueueItem, String> name = getQueueColumn("Name", QueueItem::nameProperty);

        final TableColumn<QueueItem, Number> level = getQueueColumn("Stufe", QueueItem::upgradeCounterProperty);

        final TableColumn<QueueItem, String> duration = getQueueColumn("Dauer", queueItem -> convertDurationToString(queueItem.fullDurationProperty()));

        final TableColumn<QueueItem, String> remainingDuration = getQueueColumn("Verbleibend", queueItem -> convertDurationToString(queueItem.remainingDurationProperty()));

        final TableColumn<QueueItem, Double> progress = getQueueColumn("Fortschritt", queueItem -> queueItem.remainingPercentProperty().asObject());
        progress.setCellFactory(ProgressBarTableCell.forTableColumn());

        final TableColumn<QueueItem, BigInteger> metalCost = getQueueColumn("Metallkosten", QueueItem::metalCostProperty);

        final TableColumn<QueueItem, BigInteger> crystalCost = getQueueColumn("Kristallkosten", QueueItem::crystalCostProperty);

        final TableColumn<QueueItem, BigInteger> deutCost = getQueueColumn("Deuteriumkosten", QueueItem::deutCostProperty);

        queueTable.getColumns().add(name);
        queueTable.getColumns().add(level);
        queueTable.getColumns().add(duration);
        queueTable.getColumns().add(remainingDuration);
        queueTable.getColumns().add(progress);
        queueTable.getColumns().add(metalCost);
        queueTable.getColumns().add(crystalCost);
        queueTable.getColumns().add(deutCost);

        queueTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        queueTable.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                final QueueItem selectedItem = queueTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    getCurrentQueue().removeItem(selectedItem);
                }
            }
        });

        currentQueue.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                queueTable.setItems(newValue.getQueue());
            } else {
                queueTable.setItems(null);
            }
        });
    }

    private StringBinding convertDurationToString(ObjectProperty<Duration> param) {
        return Bindings.createStringBinding(() -> {
            final Duration duration = param.get();
            if (duration == null) {
                return "0s";
            }
            final long daysPart = duration.toDaysPart();
            final int hoursPart = duration.toHoursPart();
            final int minutesPart = duration.toMinutesPart();
            final int secondsPart = duration.toSecondsPart();
            String result = secondsPart + "s";

            if (minutesPart > 0) {
                result = minutesPart + "m " + result;
            }
            if (hoursPart > 0) {
                result = hoursPart + "h " + result;
            }
            if (daysPart > 0) {
                result = daysPart + "d " + result;
            }
            return result;
        }, param);
    }

    private <E> TableColumn<QueueItem, E> getQueueColumn(String text, Function<QueueItem, ObservableValue<E>> function) {
        final TableColumn<QueueItem, E> name = new TableColumn<>(text);
        name.setCellValueFactory(param -> function.apply(param.getValue()));
        return name;
    }

    private void initGrid(UnitType[] types) {
        gridContainer.setPadding(new Insets(5));
        gridContainer.setSpacing(5);

        readyTable(queueTable);

        primary.setHgap(5);
        primary.setVgap(5);

        if (Arrays.stream(types).anyMatch(unitType -> unitType instanceof ShipType)) {

            final ShipType[] shipTypes = Arrays.stream(types).filter(unitType -> unitType instanceof ShipType).toArray(ShipType[]::new);
            final DefenceType[] defenceTypes = Arrays.stream(types).filter(unitType -> unitType instanceof DefenceType).toArray(DefenceType[]::new);

            initGridPane(shipTypes);
            initGridPane(defenceTypes);
        } else {
            initGridPane(types);
        }
    }

    private void initGridPane(UnitType[] types) {
        GridPane secondary = null;

        for (int i = 0, typesLength = types.length; i < typesLength; i++) {
            UnitType type = types[i];
            Text text = new Text(type.getName());
            text.getStyleClass().add("text");

            Text fromTo = new Text("0 -> 0");
            typeTextMap.put(type, fromTo);

            Button button = new Button();

            button.setMinSize(15, 15);
            button.setMaxSize(15, 15);

            button.setOnAction(event -> addQueueItem(type, fromTo, button));

            typeButtonMap.put(type, button);
            GridPane.setHalignment(text, HPos.RIGHT);

            Node[] nodes = new Node[]{text, fromTo, button};

            if (type instanceof DefenceType || type instanceof ShipType) {
                TextField textField = new TextField();
                textField.setTextFormatter(getTextFormatter(6));
                typeFieldMap.putIfAbsent(type, textField);

                nodes = new Node[]{text, fromTo, textField, button};
            }
            if (type instanceof DefenceType) {
                secondary = addToSecondary(secondary, i, nodes);
            } else if (i >= 7 && !(type instanceof WarfareType)) {
                secondary = addToSecondary(secondary, i - 7, nodes);
            } else {
                addToPrimary(i, nodes);
            }
        }
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

    private void addToPrimary(int i, Node... nodes) {
        primary.addRow(i, nodes);
    }

    private GridPane addToSecondary(GridPane secondary, int i, Node... nodes) {
        if (secondary == null) {
            secondary = new GridPane();
            secondary.setHgap(5);
            secondary.setVgap(5);
            gridContainer.getChildren().add(secondary);
        }
        secondary.addRow(i, nodes);
        return secondary;
    }

    private Background getBackground(Color color) {
        return new Background(new BackgroundFill(color, null, null));
    }

    private void addQueueItem(UnitType unitType, Text fromTo, Button button) {
        if (bodyProperty.get() == null || disabledMap.computeIfAbsent(button, button1 -> true)) {
            showRequirementsPopup(unitType, button);
        } else {
            final UpgradeAble upgradeAble = getUpgradeAble(fromTo);
            final IntegerProperty counter = getCounter(upgradeAble.getType());

            getCurrentQueue().addItem(upgradeAble, counter);
            button.setGraphic(getNormalCross());
        }
    }

    private void showRequirementsPopup(UnitType unitType, Button button) {
        Popup popup = new Popup();
        popup.setAutoHide(true);

        final GridPane content = getRequirementsPane(unitType);
        content.getStylesheets().add(getClass().getResource("/css/popup.css").toExternalForm());
        popup.getContent().add(content);

        final Bounds boundsInLocal = button.getBoundsInLocal();
        final Bounds bounds = button.localToScreen(boundsInLocal);

        final double anchorY = bounds.getMinY() - bounds.getHeight();
        final double anchorX = bounds.getMaxX();
        popup.show(button, anchorX, anchorY);
    }

    private UpgradeAble getUpgradeAble(Text fromTo) {
        return upgradeAbleMap.get(fromTo).get(bodyProperty.get());
    }

    private void disable() {
        typeButtonMap.forEach(Expand::disable);
    }

    private void bindNextLevel(UnitType type, UpgradeAble upgradeAble) {
        final Text text = typeTextMap.computeIfAbsent(type, unitType -> new Text());

        IntegerProperty displayCounter = getCounter(type);

        if (displayCounter == null) {
            final IntegerProperty counter = upgradeAble.counterProperty();

            final SimpleIntegerProperty counterCopy = new SimpleIntegerProperty(counter.get());
            displayCounter = counterCopy;
            counter.addListener((observable, oldValue, newValue) -> {
                final int newLevelValue = newValue.intValue() + getCurrentQueue().getNumberQueued(upgradeAble);
                counterCopy.set(newLevelValue);
            });
            putCounter(type, displayCounter);
        }

        if (type instanceof WarfareType) {
            final TextField textField = typeFieldMap.get(type);
            final StringProperty property = textField.textProperty();

            final IntegerProperty finalDisplayCounter = displayCounter;

            property.addListener((observable, oldValue, newValue) -> {
                final int counter;
                if (newValue == null || newValue.isEmpty()) {
                    counter = 0;
                } else {
                    counter = Integer.parseInt(newValue);
                }
                finalDisplayCounter.set(counter);
            });
            text.textProperty()
                    .bind(displayCounter.asString()
                            .concat(" -> ")
                            .concat(property.get().isEmpty()
                                    ? displayCounter
                                    : displayCounter.add(Integer.parseInt(property.get()))));
        } else {
            text.textProperty().bind(displayCounter.asString().concat(" -> ").concat(displayCounter.add(1)));
        }

        upgradeAbleMap.computeIfAbsent(text, text1 -> new HashMap<>()).putIfAbsent(bodyProperty.get(), upgradeAble);
    }

    private UpgradeAbleQueue getCurrentQueue() {
        final CelestialBody body = bodyProperty.get();
        if (body != null) {
            return currentQueue.get();
        } else {
            throw new IllegalStateException();
        }
    }
}
