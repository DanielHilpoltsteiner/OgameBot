package ogamebot.gui.expand.queue;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import ogamebot.calc.DurationCalc;
import ogamebot.calc.Production;
import ogamebot.comp.UpgradeAble;
import ogamebot.concurrent.CountDown;
import ogamebot.units.Effector;
import ogamebot.units.UnitType;
import ogamebot.units.astroObjects.CelestialBody;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.building.BuildingType;
import ogamebot.units.research.ResearchField;
import ogamebot.units.units.Resource;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class UpgradeAbleQueue {
    private final CelestialBody celestialBody;
    private final ObjectProperty<ZonedDateTime> queueFinishedProperty = new SimpleObjectProperty<>(ZonedDateTime.now());

    private ObservableList<QueueItem> queue = FXCollections.observableArrayList();
    private Map<UpgradeAble, ObservableList<QueueItem>> itemsMap = new TreeMap<>(Comparator.comparing(UpgradeAble::getName));

    private StringProperty availableMet = new SimpleStringProperty();
    private StringProperty availableCrys = new SimpleStringProperty();
    private StringProperty availableDeut = new SimpleStringProperty();

    private final CountDown task = CountDown.getWorker();
    private boolean running;

    private Map<UnitType, IntegerProperty> typeCounterMap;

    private Map<QueueItem, ChangeListener<Duration>> itemListener = new HashMap<>();
    private ChangeListener<? super Duration> countDownListener;
    private Map<UnitType, Map<Integer, Range>> typeLevelRangeMap = new HashMap<>();
    private ObjectBinding<Resource> resourceBinding;

    public UpgradeAbleQueue(CelestialBody celestialBody, ObservableMap<UnitType, IntegerProperty> typeCounterMap) {
        this.celestialBody = celestialBody;
        this.typeCounterMap = typeCounterMap;
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

    public ObservableList<QueueItem> getQueue() {
        return queue;
    }

    public void addItem(UpgradeAble upgradeAble) {
        addItem(upgradeAble, 1);
    }

    public void addItem(UpgradeAble upgradeAble, int increment) {
        prepareItem(upgradeAble);
        final IntegerProperty property = typeCounterMap.get(upgradeAble.getType());
        property.set(property.get() + increment);
    }

    public int getNumberQueued(UpgradeAble able) {
        return itemsMap.computeIfAbsent(able, upgradeAble -> FXCollections.observableArrayList()).size();
    }

    public void removeItem(QueueItem item) {
        final List<QueueItem> items = itemsMap.computeIfAbsent(item.getAble(), upgradeAble -> FXCollections.observableArrayList());

        final IntegerProperty counter = typeCounterMap.get(item.getAble().getType());
        final int count = counter.get();

        queue.remove(item);
        items.remove(item);

        counter.set(count - 1);

        if (items.isEmpty()) {
            itemsMap.remove(item.getAble());
        }
    }

    private void prepareItem(UpgradeAble upgradeAble) {
        final ObservableList<QueueItem> items = itemsMap.computeIfAbsent(upgradeAble, upgradeAble1 -> FXCollections.observableArrayList());

        final QueueItem item = new QueueItem(upgradeAble, typeCounterMap.get(upgradeAble.getType()), celestialBody, items);
        items.add(0, item);
        queue.add(item);
    }

    private Resource calcResource(ZonedDateTime dateTime, CelestialBody body) {
        if (body instanceof Planet) {
            final Duration between = Duration.between(ZonedDateTime.now(), dateTime);

            int metLevel = getMineLevel(body, BuildingType.METALMINE);
            int crysLevel = getMineLevel(body, BuildingType.CRYSTALMINE);
            int deutLevel = getMineLevel(body, BuildingType.DEUTSYNTH);

            return Production.forDuration((Planet) body, metLevel, crysLevel, deutLevel, between);
        }
        return new Resource();
    }

    private String convert() {
        ZonedDateTime zonedDateTime = queueFinishedProperty.get();
        if (zonedDateTime == null) {
            zonedDateTime = ZonedDateTime.now();
        }
        return zonedDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }

    private int getMineLevel(CelestialBody body, BuildingType building) {
        return typeCounterMap.getOrDefault(building, body.getBuilding(building).counterProperty()).get();
    }

    private void listener() {
        resourceBinding = Bindings.createObjectBinding(() -> {
            Resource bodyResource = celestialBody.resourceProperty().get();

            if (celestialBody instanceof Planet) {
                final Resource resource = calcResource(queueFinishedProperty.get(), celestialBody);
                bodyResource = bodyResource.add(resource);
            }
            return bodyResource;
        }, queueFinishedProperty, celestialBody.resourceProperty());

        availableMet.bind(Bindings.createStringBinding(() ->
                DecimalFormat.getInstance().format(resourceBinding.get().getMetal()), resourceBinding));

        availableCrys.bind(Bindings.createStringBinding(() ->
                DecimalFormat.getInstance().format(resourceBinding.get().getCrystal()), resourceBinding));

        availableDeut.bind(Bindings.createStringBinding(() ->
                DecimalFormat.getInstance().format(resourceBinding.get().getDeut()), resourceBinding));

        queue.addListener((ListChangeListener<? super QueueItem>) observable -> {
            if (observable.next()) {
                if (observable.wasAdded()) {
                    if (running && observable.getList().size() == observable.getAddedSize()) {
                        queueNext(null);
                    }
                    refreshQueueFinishedOnAdd(observable);
                }
                if (observable.wasRemoved()) {
                    refreshQueueFinishedOnRemove(observable);
                }
                recalculateProperties();
            }
        });
    }

    private void recalculateProperties() {
        typeLevelRangeMap.clear();
        Map<UnitType, Integer> integerMap = new HashMap<>();

        final Map<UnitType<?>, Long> maxFrequencyMap = queue
                .stream()
                .collect(Collectors.groupingBy(e -> e.getAble().getType(), Collectors.counting()));
        Map<UnitType, Integer> currentFrequencyMap = new HashMap<>();
        Map<UnitType, Integer> previousLevelMap = new HashMap<>();

        for (int i = 0; i < queue.size(); i++) {
            QueueItem item = queue.get(i);
            final UnitType<?> type = item.getAble().getType();

            if (type instanceof Effector) {
                if (integerMap.containsKey(type)) {
                    currentFrequencyMap.compute(type, (unitType, integer) -> integer + 1);

                    final Integer from = integerMap.put(type, i);
                    final int to = i - 1;
                    final Integer previousLevel = previousLevelMap.get(type);
                    typeLevelRangeMap.computeIfAbsent(type, unitType -> new HashMap<>()).put(previousLevel, new Range(from, to));

                    if (maxFrequencyMap.get(type).intValue() == currentFrequencyMap.get(type)) {
                        final int currentLevel = item.upgradeCounterProperty().get();
                        typeLevelRangeMap.get(type).put(currentLevel, new Range(i, queue.size() - 1));
                    } else {
                        previousLevelMap.put(type, item.upgradeCounterProperty().get());
                    }
                } else {
                    previousLevelMap.put(type, item.upgradeCounterProperty().get());
                    currentFrequencyMap.put(type, 1);
                    integerMap.put(type, 0);
                }
            }
        }
        integerMap.entrySet().stream().filter(entry -> entry.getValue() == 0).forEach(entry -> {
            final UnitType key = entry.getKey();
            if (key instanceof BuildingType) {

                final int counter = celestialBody.getBuilding((BuildingType) key).getCounter();
                typeLevelRangeMap.put(key, Map.of(counter, new Range(0, queue.size() - 1)));

            } else if (key instanceof ResearchField) {
                final int counter = celestialBody.getPlayer().getResearch((ResearchField) key).getCounter();
                typeLevelRangeMap.put(key, Map.of(counter, new Range(0, queue.size() - 1)));
            }
        });
        recalculateProduction();
        recalculateDurations();
    }

    private void recalculateDurations() {

    }

    private void recalculateProduction() {

    }

    private void refreshQueueFinishedOnRemove(ListChangeListener.Change<? extends QueueItem> observable) {
        if (observable.getList().isEmpty()) {
            queueFinishedProperty.set(ZonedDateTime.now());
        } else {
            Duration duration = Duration.ZERO;

            for (QueueItem item : observable.getRemoved()) {
                duration = duration.minus(getDuration(item, item.upgradeCounterProperty().intValue()));
                item.fullDurationProperty().removeListener(itemListener.get(item));
                itemListener.remove(item);
            }
            final ZonedDateTime newDateTime = queueFinishedProperty.get().minus(duration);
            queueFinishedProperty.set(newDateTime);
        }
    }

    private void refreshQueueFinishedOnAdd(ListChangeListener.Change<? extends QueueItem> observable) {
        Duration duration = Duration.ZERO;

        for (QueueItem item : observable.getAddedSubList()) {
            duration = getDuration(item, item.upgradeCounterProperty().intValue());

            final ChangeListener<Duration> changeListener = getDurationChangeListener();
            item.fullDurationProperty().addListener(changeListener);
            itemListener.put(item, changeListener);
        }

        final ZonedDateTime newDateTime = queueFinishedProperty.get().plus(duration);
        queueFinishedProperty.set(newDateTime);
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
        return DurationCalc.calculateDuration(item.getAble().getType(), counter, celestialBody);
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

            countDownListener = countDownListener(nextItem);
            nextItem.remainingDurationProperty().addListener(countDownListener);
        }
    }

    private ChangeListener<? super Duration> countDownListener(QueueItem item) {
        return (observable, oldValue, newValue) -> {
            if (newValue != null) {

                if (newValue.isZero() || newValue.isNegative()) {
                    item.remainingDurationProperty().removeListener(countDownListener);
                    item.getAble().counterProperty().set(item.upgradeCounterProperty().intValue());
                    removeItem(item);

                    queueNext(item);
                }
            }
        };
    }
}
