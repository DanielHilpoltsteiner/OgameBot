package ogamebot.gui.queue;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;
import ogamebot.comp.Cost;
import ogamebot.comp.UpgradeAble;
import ogamebot.tools.DurationCalc;
import ogamebot.units.astroObjects.CelestialBody;

import java.math.BigInteger;
import java.time.Duration;

/**
 *
 */
public class QueueItem {
    private final CelestialBody body;
    private final UpgradeAble able;
    private final NumberBinding counter;
    private boolean isRunning;

    private ObjectProperty<Cost> cost = new SimpleObjectProperty<>(new Cost(1, 1, 1));
    private IntegerProperty disparity = new SimpleIntegerProperty();

    private ObjectProperty<BigInteger> metalCost = new SimpleObjectProperty<>(BigInteger.ZERO);
    private ObjectProperty<BigInteger> crystalCost = new SimpleObjectProperty<>(BigInteger.ZERO);
    private ObjectProperty<BigInteger> deutCost = new SimpleObjectProperty<>(BigInteger.ZERO);

    private ObjectProperty<Duration> remainingDuration = new SimpleObjectProperty<>(Duration.ZERO);
    private ObjectProperty<Duration> fullDuration = new SimpleObjectProperty<>(Duration.ZERO);
    private DoubleBinding percentage;


    public QueueItem(UpgradeAble able, IntegerProperty counter, CelestialBody body) {
        this.able = able;
        this.counter = counter.subtract(disparity);
        this.body = body;

        initProperties(able, body);
        initValues();
    }

    private void initProperties(UpgradeAble able, CelestialBody body) {
        metalCost.bind(Bindings.createObjectBinding(() -> cost.get().getMetal(), cost));
        crystalCost.bind(Bindings.createObjectBinding(() -> cost.get().getCrystal(), cost));
        deutCost.bind(Bindings.createObjectBinding(() -> cost.get().getDeut(), cost));

        fullDuration.addListener((observable, oldValue, newValue) -> remainingDuration.set(newValue));
        percentage = Bindings.createDoubleBinding(this::calcDurationQuotient, fullDuration, remainingDuration);

        this.counter.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 0) {
                final Cost cost = able.getType().getCost(newValue.intValue());
                this.cost.set(cost);

                final Duration newDuration = DurationCalc.calculateDuration(able.getType(), this.counter.intValue(), body);
                fullDuration.set(newDuration);
            }
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
            result = 1 - result;
        } else {
            result = -1;
        }
        return result;
    }

    private void initValues() {
        final Cost initialCost = able.getType().getCost(this.counter.intValue());
        this.cost.set(initialCost);

        final Duration newDuration = DurationCalc.calculateDuration(able.getType(), this.counter.intValue(), body);
        fullDuration.set(newDuration);
    }

    public void setDisparity(int disparity) {
        this.disparity.set(disparity);
    }

    public ReadOnlyStringProperty nameProperty() {
        return able.nameProperty();
    }

    public NumberBinding counterProperty() {
        return counter;
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
}
