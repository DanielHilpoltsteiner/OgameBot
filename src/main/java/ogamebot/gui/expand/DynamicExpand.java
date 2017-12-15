package ogamebot.gui.expand;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import ogamebot.comp.Requirement;
import ogamebot.comp.UpgradeAble;
import ogamebot.gui.expand.queue.QueueItem;
import ogamebot.gui.expand.queue.UpgradeAbleQueue;
import ogamebot.units.RequireAble;
import ogamebot.units.UnitType;
import ogamebot.units.astroObjects.CelestialBody;
import ogamebot.units.building.Building;
import ogamebot.units.research.Research;
import ogamebot.units.warfare.WarfareUnit;

import java.math.BigInteger;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;

/**
 *
 */
public class DynamicExpand implements Initializable {

    @FXML
    private Text availableAt;

    @FXML
    private Text availableMet;

    @FXML
    private Text availableCrys;

    @FXML
    private Text availableDeut;

    @FXML
    private TableView<QueueItem> queue;

    @FXML
    private ListView<Building> expandableBuildings;

    @FXML
    private ListView<Research> expandableResearch;

    @FXML
    private ListView<WarfareUnit> expandableMachines;

    private Map<CelestialBody, UpgradeAbleQueue> queueMap = new HashMap<>();

    private ObjectProperty<UpgradeAbleQueue> currentQueue = new SimpleObjectProperty<>();
    private ObjectProperty<CelestialBody> bodyProperty = new SimpleObjectProperty<>();
    private Map<CelestialBody, ObservableMap<UnitType, IntegerProperty>> bodyTypeCounterMap = new HashMap<>();

    private IntegerBinding met;
    private IntegerBinding crys;
    private IntegerBinding deut;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        readyTable(queue);
        readyListViews(expandableBuildings, CelestialBody::getBuildings);
        readyListViews(expandableResearch, body -> body.getPlayer().getResearches());
        readyListViews(expandableMachines, body -> {
            final List<WarfareUnit> units = new ArrayList<>(body.getDefences());
            units.addAll(body.getShips());
            return units;
        });

        expandableBuildings.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Building item, boolean empty) {
                super.updateItem(item, empty);


                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    final IntegerProperty counter = getCounter(item.getType());
                    setText(item.getName() + counter.asString().concat(" -> ").concat(counter.add(1)));
                }
            }
        });
    }

    private BooleanBinding getUnlockBinding(UnitType type) {
        Requirement requirement = type.getRequirement();
        Set<RequireAble> requirements = requirement.getRequirements();

        IntegerProperty[] dependencies = requirements.stream().map(this::getCounter).toArray(IntegerProperty[]::new);

        return Bindings.createBooleanBinding(() -> {
            final boolean techUnlocked = checkRequirement(requirement, requirements);
            final boolean resourceUnlocked = checkResource(type);
            return techUnlocked && resourceUnlocked;
        }, dependencies);
    }

    private boolean checkResource(UnitType type) {
        return true;
    }

    private boolean checkRequirement(Requirement requirement, Set<RequireAble> requirements) {
        /*return requirements.stream().allMatch(requireAble -> {
            IntegerProperty property = getCounter(requireAble);

            final int level = requirement.get(requireAble);
            return property != null && property.get() >= level;
        });*/
        return true;
    }

    private IntegerProperty getCounter(UnitType<?> type) {
        return getTypeCountMap().get(type);
    }

    private ObservableMap<UnitType, IntegerProperty> getTypeCountMap() {
        return FXCollections.observableHashMap();
    }

    private <E extends UpgradeAble> void readyListViews(ListView<E> view, Function<CelestialBody, Collection<E>> collectionFunction) {
        bodyProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                FilteredList<E> filteredList = new FilteredList<>(FXCollections.observableArrayList(collectionFunction.apply(newValue)));
                filteredList.setPredicate(e -> {
                    Requirement requirement = e.getType().getRequirement();
                    Set<RequireAble> requirements = requirement.getRequirements();

                    final boolean checkRequirement = checkRequirement(requirement, requirements);
                    final boolean checkResource = checkResource(e.getType());
                    return checkRequirement && checkResource;
                });
                view.setItems(filteredList);
            }
        });

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

    private <E> TableColumn<QueueItem, E> getQueueColumn(String text, Function<QueueItem, ObservableValue<E>> function) {
        final TableColumn<QueueItem, E> name = new TableColumn<>(text);
        name.setCellValueFactory(param -> function.apply(param.getValue()));
        return name;
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

    private UpgradeAbleQueue getCurrentQueue() {
        final CelestialBody body = bodyProperty.get();
        if (body != null) {
            return currentQueue.get();
        } else {
            throw new IllegalStateException();
        }
    }
}
