package ogamebot.units.warfare;

import gorgon.external.DataAccess;
import gorgon.external.GorgonEntry;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import ogamebot.comp.Cost;
import ogamebot.data.daos.FleetDao;

/**
 * Class representing a arbitrary number of ships of the same type.
 */
@DataAccess(FleetDao.class)
public class Ships extends MobileWarfareUnit implements GorgonEntry {
    private final ShipType type;
    private final ReadOnlyStringProperty name;

    public Ships(int structurePoints, double shieldStrength, double attackPower, int speed, int load_capacity, int fuel_consumption, ShipType type) {
        super(structurePoints, shieldStrength, attackPower, speed, load_capacity, fuel_consumption);
        this.type = type;
        name = new SimpleStringProperty(type.getName());
    }

    public static Ships create(ShipType type, int numbers) {
        final Ships ship = type.create();
        ship.setNumbers(numbers);
        return ship;
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

    @Override
    public Cost getCost() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ships ship = (Ships) o;

        return getName().equals(ship.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return "Ships{" +
                "name=" + name.get() +
                ", numbers=" + numbers.get() +
                '}';
    }

    @Override
    public int compareTo(GorgonEntry gorgonEntry) {
        if (gorgonEntry == null) return -1;
        if (gorgonEntry == this) return 0;
        if (gorgonEntry.getClass() != getClass()) return -1;

        Ships ships = (Ships) gorgonEntry;

        return getName().compareTo(ships.getName());
    }
}
