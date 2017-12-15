package ogamebot.gui.expand.queue;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import ogamebot.calc.DurationCalc;
import ogamebot.comp.Cost;
import ogamebot.comp.UpgradeAble;
import ogamebot.units.astroObjects.CelestialBody;

import java.math.BigInteger;
import java.time.Duration;

/**
 *
 */
public class QueueItem {
    private final CelestialBody body;
    private final UpgradeAble able;

    private static int count = 0;
    private final IntegerProperty counter;

    private ObjectProperty<Cost> cost = new SimpleObjectProperty<>(new Cost(1, 1, 1));

    private ObjectProperty<BigInteger> metalCost = new SimpleObjectProperty<>(BigInteger.ZERO);
    private ObjectProperty<BigInteger> crystalCost = new SimpleObjectProperty<>(BigInteger.ZERO);
    private ObjectProperty<BigInteger> deutCost = new SimpleObjectProperty<>(BigInteger.ZERO);

    private ObjectProperty<Duration> remainingDuration = new SimpleObjectProperty<>(Duration.ZERO);
    private ObjectProperty<Duration> fullDuration = new SimpleObjectProperty<>(Duration.ZERO);

    private DoubleBinding percentage;
    private final ObservableList<QueueItem> items;
    private IntegerBinding upgradeCounter;
    private int id = count++;

    public QueueItem(UpgradeAble able, IntegerProperty counter, CelestialBody body, ObservableList<QueueItem> items) {
        this.able = able;
        this.counter = counter;
        this.body = body;
        this.items = items;

        initProperties(able, body);
        initValues();

    }

    private void initProperties(UpgradeAble able, CelestialBody body) {
        upgradeCounter = Bindings.createIntegerBinding(() -> counter.get() - items.indexOf(this), counter);

        metalCost.bind(Bindings.createObjectBinding(() -> cost.get().getMetal(), cost));
        crystalCost.bind(Bindings.createObjectBinding(() -> cost.get().getCrystal(), cost));
        deutCost.bind(Bindings.createObjectBinding(() -> cost.get().getDeut(), cost));

        fullDuration.addListener((observable, oldValue, newValue) -> remainingDuration.set(newValue));
        percentage = Bindings.createDoubleBinding(this::calcDurationQuotient, fullDuration, remainingDuration);

        this.upgradeCounter.addListener((observable, oldValue, newValue) -> {
                final Cost cost = able.getType().getCost(newValue.intValue());
                this.cost.set(cost);

            final Duration newDuration = DurationCalc.calculateDuration(able.getType(), this.upgradeCounter.intValue(), body);
                fullDuration.set(newDuration);
        });
    }

    private Double calcDurationQuotient() {
        final Duration dividend = remainingDuration.get();
        final Duration divisor = fullDuration.get();

        double result;
        if (!divisor.isZero()) {
            final double first = dividend.getSeconds();
            final double second = divisor.getSeconds();

            result = first / second;

            if (result == 1) {
                result = -1;
            } else {
                result = 1 - result;
            }
        } else {
            result = -1;
        }
        return result;
    }

    private void initValues() {
        final Cost initialCost = able.getType().getCost(this.upgradeCounter.intValue());
        this.cost.set(initialCost);

        final Duration newDuration = DurationCalc.calculateDuration(able.getType(), this.upgradeCounter.intValue(), body);
        fullDuration.set(newDuration);
    }


    public ReadOnlyStringProperty nameProperty() {
        return able.nameProperty();
    }

    public IntegerBinding upgradeCounterProperty() {
        return upgradeCounter;
    }

    public ObjectProperty<Duration> remainingDurationProperty() {
        return remainingDuration;
    }

    public ObjectProperty<Duration> fullDurationProperty() {
        return fullDuration;
    }

    public DoubleBinding remainingPercentProperty() {
        return percentage;
    }

    public ReadOnlyObjectProperty<BigInteger> metalCostProperty() {
        return metalCost;
    }

    public ReadOnlyObjectProperty<BigInteger> crystalCostProperty() {
        return crystalCost;
    }

    public ReadOnlyObjectProperty<BigInteger> deutCostProperty() {
        return deutCost;
    }

    public UpgradeAble getAble() {
        return able;
    }

    @Override
    public String toString() {
        return "QueueItem{" +
                "able=" + able +
                ", upgradeCounter=" + upgradeCounter +
                '}';
    }
}
