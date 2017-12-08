package ogamebot.units.warfare;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import ogamebot.comp.Cost;
import ogamebot.comp.UpgradeAble;

/**
 *
 */
public class Ship extends MobileWarfareUnit {
    private final ShipType type;
    private final ReadOnlyStringProperty name;

    public Ship(RapidFire rapidFire, int structurePoints, double shieldStrength, double attackPower, int speed, int load_capacity, int fuel_consumption, ShipType type) {
        super(rapidFire, structurePoints, shieldStrength, attackPower, speed, load_capacity, fuel_consumption);
        this.type = type;
        name = new SimpleStringProperty(type.getName());
    }

    @Override
    public ShipType getType() {
        return type;
    }

    @Override
    public String getName() {
        return name.getName();
    }

    @Override
    public ReadOnlyStringProperty nameProperty() {
        return name;
    }

    void setNumbers(int numbers) {
        this.numbers.set(numbers);
    }

    @Override
    public Cost getCost() {
        return null;
    }

    @Override
    public int compareTo(UpgradeAble o) {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ship ship = (Ship) o;

        return getName().equals(ship.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return "Ship{" +
                "name=" + name +
                ", numbers=" + numbers +
                '}';
    }
}
