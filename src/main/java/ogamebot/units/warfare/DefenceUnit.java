package ogamebot.units.warfare;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import ogamebot.comp.Cost;
import ogamebot.comp.UpgradeAble;

/**
 *
 */
public class DefenceUnit extends WarfareUnit {
    private final DefenceType type;
    private boolean onlyOne;
    private final ReadOnlyStringProperty name;

    DefenceUnit(RapidFire rapidFire, int structurePoints, int shieldStrength, int attackPower, DefenceType type) {
        super(rapidFire, structurePoints, shieldStrength, attackPower);
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
    public int compareTo(UpgradeAble o) {
        return 0;
    }
}
