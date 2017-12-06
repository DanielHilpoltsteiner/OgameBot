package ogamebot.gui;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Callback;
import ogamebot.comp.UpgradeAble;
import ogamebot.gui.queue.QueueItem;
import ogamebot.gui.queue.UpgradeAbleQueue;
import ogamebot.units.UnitType;
import ogamebot.units.astroObjects.CelestialBody;
import ogamebot.units.astroObjects.Planet;

import java.math.BigInteger;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

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

    @FXML
    private GridPane grid;

    @FXML
    private Button queueButton;

    private ObjectProperty<CelestialBody> bodyProperty = new SimpleObjectProperty<>();
    private BiFunction<CelestialBody, UnitType, UpgradeAble> upgradeAbleFunction;
    private UnitType[] values;
    private UpgradeAbleQueue queue;
    private Map<UnitType, Text> typeTextMap = new HashMap<>();
    private Map<UnitType, IntegerProperty> upgradeAbleCountMap = new HashMap<>();
    private Map<Text, UpgradeAble> upgradeAbleMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listener();
        initGui();
    }

    private void initGui() {
        queue = new UpgradeAbleQueue(bodyProperty);
        queueButton.setText("Starten");
        availableAt.textProperty().bind(queue.queueFinishedProperty());
        availableMetal.textProperty().bind(queue.availableMetProperty());
        availableCrystal.textProperty().bind(queue.availableCrysProperty());
        availableDeut.textProperty().bind(queue.availableDeutProperty());
    }

    @FXML
    void executeQueue() {
        if (queue.isRunning()) {
            queue.stop();
            queueButton.setText("Starten");
        } else {
            queue.start();
            queueButton.setText("Stoppen");
        }
    }

    private void listener() {
        bodyProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                for (UnitType value : values) {
                    bindNextLevel(value, upgradeAbleFunction.apply(newValue, value));
                }
            }
        });
    }

    void load(UnitType[] values, ReadOnlyObjectProperty<Planet> bodyProperty, BiFunction<CelestialBody, UnitType, UpgradeAble> upgradeAbleSupplier) {
        initGrid(values);
        this.values = values;
        this.upgradeAbleFunction = upgradeAbleSupplier;
        this.bodyProperty.bind(bodyProperty);
    }

    private void readyTable(TableView<QueueItem> queueTable, UpgradeAbleQueue queue) {
        final TableColumn<QueueItem, String> name = getQueueColumn("Name", param -> param.getValue().nameProperty());

        final TableColumn<QueueItem, Number> level = getQueueColumn("Stufe", param -> param.getValue().counterProperty());

        final TableColumn<QueueItem, Duration> duration = getQueueColumn("Dauer", param -> param.getValue().fullDurationProperty());

        final TableColumn<QueueItem, Duration> remainingDuration = getQueueColumn("Verbleibend", param -> param.getValue().remainingDurationProperty());

        final TableColumn<QueueItem, Double> progress = getQueueColumn("Fortschritt", param -> param.getValue().remainingPercentProperty().asObject());
        progress.setCellFactory(ProgressBarTableCell.forTableColumn());

        final TableColumn<QueueItem, BigInteger> metalCost = getQueueColumn("Metallkosten", param -> param.getValue().metalCostProperty());

        final TableColumn<QueueItem, BigInteger> crystalCost = getQueueColumn("Kristallkosten", param -> param.getValue().crystalCostProperty());

        final TableColumn<QueueItem, BigInteger> deutCost = getQueueColumn("Deuteriumkosten", param -> param.getValue().deutCostProperty());

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
                queue.removeItem(selectedItem);
            }
        });
    }

    private <E> TableColumn<QueueItem, E> getQueueColumn(String text, Callback<TableColumn.CellDataFeatures<QueueItem, E>, ObservableValue<E>> callback) {
        final TableColumn<QueueItem, E> name = new TableColumn<>(text);
        name.setCellValueFactory(callback);
        return name;
    }

    private void initGrid(UnitType[] types) {
        grid.setVgap(5);
        grid.setHgap(5);

        readyTable(queueTable, queue);
        queueTable.setItems(queue.getQueue());

        for (int i = 0; i < types.length; i++) {
            final UnitType type = types[i];

            Text text = new Text(type.getName());
            text.getStyleClass().add("text");

            Text fromTo = new Text("0 -> 0");
            typeTextMap.put(type, fromTo);


            Button button = new Button();

            Line horizontal = new Line(-4, 0, 4, 0);
            Line vertical = new Line(0, -4, 0, 4);
            Shape buttonCross = Line.union(horizontal, vertical);

            button.setGraphic(buttonCross);
            button.setMinSize(15, 15);
            button.setMaxSize(15, 15);
            button.disableProperty().bind(bodyProperty.isNull());

            button.setOnAction(event -> {
                final UpgradeAble upgradeAble = upgradeAbleMap.get(fromTo);
                final IntegerProperty counter = upgradeAbleCountMap.get(upgradeAble.getType());
                queue.addItem(upgradeAble, counter);
            });
            grid.addRow(i, text, fromTo, button);
        }
    }

    private void bindNextLevel(UnitType type, UpgradeAble upgradeAble) {
        final Text text = typeTextMap.computeIfAbsent(type, unitType -> new Text());

        final IntegerProperty counter = upgradeAble.counterProperty();

        final SimpleIntegerProperty counterCopy = new SimpleIntegerProperty(counter.get());
        text.textProperty().bind(counterCopy.asString().concat(" -> ").concat(counterCopy.add(1).asString()));

        upgradeAbleCountMap.put(type, counterCopy);
        upgradeAbleMap.put(text, upgradeAble);

        counter.addListener((observable, oldValue, newValue) -> {
            final int newLevelValue = newValue.intValue() + queue.getNumberQueued(upgradeAble);
            counterCopy.set(newLevelValue);
        });
    }
}
