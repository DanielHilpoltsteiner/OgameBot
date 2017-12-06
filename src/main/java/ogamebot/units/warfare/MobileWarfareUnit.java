package ogamebot.units.warfare;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import tools.Condition;

/**
 *
 */
abstract class MobileWarfareUnit extends WarfareUnit {
    private IntegerProperty speed = new SimpleIntegerProperty();
    private IntegerProperty load_capacity = new SimpleIntegerProperty();
    private IntegerProperty fuel_consumption = new SimpleIntegerProperty();

    MobileWarfareUnit(RapidFire rapidFire, int structurePoints, double shieldStrength, double attackPower, int speed, int load_capacity, int fuel_consumption) {
        super(rapidFire, structurePoints, shieldStrength, attackPower);
        Condition.check().positive(speed, load_capacity, fuel_consumption);

        this.speed.set(speed);
        this.load_capacity.set(load_capacity);
        this.fuel_consumption.set(fuel_consumption);
    }

    public int getSpeed() {
        return speed.get();
    }

    public IntegerProperty speedProperty() {
        return speed;
    }

    public int getLoad_capacity() {
        return load_capacity.get();
    }

    public IntegerProperty load_capacityProperty() {
        return load_capacity;
    }

    public int getFuel_consumption() {
        return fuel_consumption.get();
    }

    public IntegerProperty fuel_consumptionProperty() {
        return fuel_consumption;
    }
}
