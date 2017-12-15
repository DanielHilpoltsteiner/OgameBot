package ogamebot.units.warfare;

import gorgon.external.DataAccess;
import gorgon.external.GorgonEntry;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import ogamebot.comp.Cost;
import ogamebot.data.daos.DefenceDao;

/**
 *
 */
@DataAccess(DefenceDao.class)
public class DefenceUnit extends WarfareUnit implements GorgonEntry {
    private final DefenceType type;
    private boolean onlyOne;
    private final ReadOnlyStringProperty name;

    DefenceUnit(int structurePoints, int shieldStrength, int attackPower, DefenceType type) {
        super(structurePoints, shieldStrength, attackPower);
        this.type = type;
        onlyOne = type == DefenceType.SMALL_SHIELD || type == DefenceType.GREAT_SHIELD;
        name = new SimpleStringProperty(type.getName());
    }

    public DefenceType getType() {
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

    public boolean isOnlyOne() {
        return onlyOne;
    }

    @Override
    public Cost getCost() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefenceUnit that = (DefenceUnit) o;

        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    public static DefenceUnit create(DefenceType defenceType, Integer numbers) {
        final DefenceUnit unit = defenceType.create();
        unit.setNumbers(numbers);
        return unit;
    }

    @Override
    public String toString() {
        return "DefenceUnit{" +
                "name=" + name.get() +
                ", numbers=" + numbers.get() +
                '}';
    }

    @Override
    public int compareTo(GorgonEntry gorgonEntry) {
        if (gorgonEntry == null) return -1;
        if (gorgonEntry == this) return 0;
        if (gorgonEntry.getClass() != getClass()) return -1;

        DefenceUnit unit = (DefenceUnit) gorgonEntry;
        return getName().compareTo(unit.getName());
    }
}
