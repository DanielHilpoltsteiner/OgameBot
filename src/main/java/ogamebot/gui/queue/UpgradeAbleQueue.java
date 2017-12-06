package ogamebot.gui.queue;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import ogamebot.comp.Production;
import ogamebot.comp.UpgradeAble;
import ogamebot.concurrent.CountDown;
import ogamebot.tools.DurationCalc;
import ogamebot.units.astroObjects.CelestialBody;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.building.PlanetBuilding;
import ogamebot.units.units.Resource;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static ogamebot.units.building.PlanetBuilding.*;

/**
 *
 */
public class UpgradeAbleQueue {
    private final Comparator<UpgradeAble> upgradeAbleComparator = Comparator.comparing(UpgradeAble::getName);
    private final ObjectProperty<CelestialBody> celestialBody;
    private final CountDown task = CountDown.getWorker();
    private final ObjectProperty<ZonedDateTime> queueFinishedProperty = new SimpleObjectProperty<>(ZonedDateTime.now());
    private ObservableList<QueueItem> queue = FXCollections.observableArrayList();
    private Map<UpgradeAble, ObservableList<QueueItem>> itemsMap = new TreeMap<>(upgradeAbleComparator);
    private Map<UpgradeAble, IntegerProperty> baseCounter = new TreeMap<>(upgradeAbleComparator);
    private StringProperty availableMet = new SimpleStringProperty();
    private StringProperty availableCrys = new SimpleStringProperty();
    private StringProperty availableDeut = new SimpleStringProperty();
    private boolean running;
    private IntegerProperty metalCounter;
    private IntegerProperty crystalCounter;
    private IntegerProperty deutCounter;
    private Map<QueueItem, ChangeListener<Duration>> itemListener = new HashMap<>();
    private ChangeListener<? super Duration> countDownListener;

    public UpgradeAbleQueue(ObjectProperty<CelestialBody> celestialBody) {
        this.celestialBody = celestialBody;
        task.refreshInstant(queueFinishedProperty);
        listener();
    }

    public static void stopTask() {
        CountDown.shutDown();
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        running = true;
        queueNext(null);
    }

    public void stop() {
        running = false;

        if (!queue.isEmpty()) {
            final QueueItem item = queue.get(0);
            item.remainingDurationProperty().set(item.fullDurationProperty().get());
        }
    }

    public StringExpression queueFinishedProperty() {
        return Bindings.createStringBinding(this::convert, queueFinishedProperty);
    }

    public ReadOnlyStringProperty availableMetProperty() {
        return availableMet;
    }

    public ReadOnlyStringProperty availableCrysProperty() {
        return availableCrys;
    }

    public ReadOnlyStringProperty availableDeutProperty() {
        return availableDeut;
    }

    public void addItem(UpgradeAble upgradeAble, IntegerProperty counter) {
        addItem(upgradeAble, counter, 1);
    }

    public void addItem(UpgradeAble upgradeAble, IntegerProperty counter, int increment) {
        counter.set(counter.get() + increment);
        prepareItem(upgradeAble, counter);

        if (upgradeAble.getType() == METALMINE) {
            metalCounter = counter;
        } else if (upgradeAble.getType() == CRYSTALMINE) {
            crystalCounter = counter;
        } else if (upgradeAble.getType() == DEUTSYNTH) {
            deutCounter = counter;
        }
    }

    public int getNumberQueued(UpgradeAble able) {
        return itemsMap.get(able).size();
    }

    public void removeItem(QueueItem item) {
        final List<QueueItem> items = itemsMap.computeIfAbsent(item.getAble(), upgradeAble -> getObservableList());

        final IntegerProperty counter = baseCounter.get(item.getAble());
        final int count = counter.get();
        counter.set(count - 1);

        queue.remove(item);
        items.remove(item);

        if (items.isEmpty()) {
            itemsMap.remove(item.getAble());
        }
    }

    public ObservableList<QueueItem> getQueue() {
        return queue;
    }

    private void prepareItem(UpgradeAble upgradeAble, IntegerProperty counter) {
        final QueueItem item = new QueueItem(upgradeAble, counter, celestialBody.get());
        queue.add(item);

        final ObservableList<QueueItem> items = itemsMap.computeIfAbsent(upgradeAble, upgradeAble1 -> getObservableList());
        items.add(0, item);
        baseCounter.put(upgradeAble, counter);
    }

    private ObservableList<QueueItem> getObservableList() {
        final ObservableList<QueueItem> items = FXCollections.observableArrayList();

        items.addListener((ListChangeListener<? super QueueItem>) observable -> {
            if (observable.next()) {
                final ObservableList<? extends QueueItem> list = observable.getList();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setDisparity(i);
                }
            }
        });
        return items;
    }

    private Resource calcResource(ZonedDateTime dateTime, CelestialBody body) {
        if (body instanceof Planet) {
            final Duration between = Duration.between(ZonedDateTime.now(), dateTime);

            int metLevel = getMineLevel(body, PlanetBuilding.METALMINE, metalCounter);
            int crysLevel = getMineLevel(body, PlanetBuilding.CRYSTALMINE, crystalCounter);
            int deutLevel = getMineLevel(body, PlanetBuilding.DEUTSYNTH, deutCounter);

            return Production.forDuration((Planet) body, metLevel, crysLevel, deutLevel, between);
        }
        return new Resource();
    }

    private int getMineLevel(CelestialBody body, PlanetBuilding building, IntegerProperty counter) {
        return counter == null ? body.getBuilding(building).getCounter() : counter.get();
    }

    private String convert() {
        ZonedDateTime zonedDateTime = queueFinishedProperty.get();
        if (zonedDateTime == null) {
            zonedDateTime = ZonedDateTime.now();
        }
        return zonedDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }

    private void listener() {
        queueFinishedProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                final CelestialBody body = this.celestialBody.get();
                if (body != null) {
                    final Resource resource = calcResource(newValue, body);

                    availableMet.set(String.valueOf(resource.getMetal()));
                    availableCrys.set(String.valueOf(resource.getCrystal()));
                    availableDeut.set(String.valueOf(resource.getDeut()));
                }
            }
        });
        queue.addListener((ListChangeListener<? super QueueItem>) observable -> {
            if (observable.next()) {
                if (observable.wasAdded()) {
                    Duration duration = Duration.ZERO;

                    for (QueueItem item : observable.getAddedSubList()) {
                        duration = getDuration(item, item.counterProperty().intValue());

                        final ChangeListener<Duration> changeListener = getDurationChangeListener();
                        item.fullDurationProperty().addListener(changeListener);
                        itemListener.put(item, changeListener);
                    }

                    final ZonedDateTime newDateTime = queueFinishedProperty.get().plus(duration);
                    queueFinishedProperty.set(newDateTime);
                }
                if (observable.wasRemoved()) {
                    if (observable.getList().isEmpty()) {
                        queueFinishedProperty.set(ZonedDateTime.now());
                    } else {
                        Duration duration = Duration.ZERO;

                        for (QueueItem item : observable.getRemoved()) {
                            duration = duration.minus(getDuration(item, item.counterProperty().intValue()));
                            item.fullDurationProperty().removeListener(itemListener.get(item));
                            itemListener.remove(item);
                        }
                        final ZonedDateTime newDateTime = queueFinishedProperty.get().minus(duration);
                        queueFinishedProperty.set(newDateTime);
                    }
                }
            }
        });
    }

    private ChangeListener<Duration> getDurationChangeListener() {
        return (observable1, oldValue, newValue) -> {
            if (oldValue != null && newValue != null) {
                final Duration difference = newValue.minus(oldValue);
                final ZonedDateTime updated = queueFinishedProperty.get().plus(difference);
                queueFinishedProperty.set(updated);
            }
        };
    }

    private Duration getDuration(QueueItem item, int counter) {
        return DurationCalc.calculateDuration(item.getAble().getType(), counter, celestialBody.get());
    }

    private void queueNext(QueueItem currentItem) {
        if (!queue.isEmpty()) {
            QueueItem nextItem = queue.get(0);

            if (nextItem == currentItem && queue.size() >= 2) {
                nextItem = queue.get(1);
            } else if (nextItem == currentItem) {
                return;
            }

            final ObjectProperty<Duration> durationProperty = nextItem.remainingDurationProperty();
            task.addCountDown(durationProperty, Duration.ZERO);
            durationProperty.addListener((observable, oldValue, newValue) -> System.out.println(newValue));

            countDownListener = countDownListener(nextItem);
            nextItem.remainingDurationProperty().addListener(countDownListener);
        }
    }

    private ChangeListener<? super Duration> countDownListener(QueueItem item) {
        return (observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isZero() || newValue.isNegative()) {

                    item.remainingDurationProperty().removeListener(countDownListener);
                    item.getAble().counterProperty().set(item.counterProperty().intValue());
                    removeItem(item);

                    queueNext(item);
                }
            }
        };
    }
}
