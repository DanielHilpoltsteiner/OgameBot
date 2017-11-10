package units.warfare;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import tools.Condition;

/**
 *
 */
public abstract class WarfareUnit {
    private RapidFire rapidFire;
    private IntegerProperty structurePoints = new SimpleIntegerProperty();
    private IntegerProperty shieldStrength = new SimpleIntegerProperty();
    private IntegerProperty attackPower = new SimpleIntegerProperty();
    private IntegerProperty numbers = new SimpleIntegerProperty();

    WarfareUnit(RapidFire rapidFire, int structurePoints, int shieldStrength, int attackPower) {
        Condition.check().nonNull(rapidFire).positive(structurePoints, shieldStrength, attackPower);
        this.rapidFire = rapidFire;
        this.structurePoints.set(structurePoints);
        this.shieldStrength.set(shieldStrength);
        this.attackPower.set(attackPower);

    }

    public RapidFire getRapidFire() {
        return rapidFire;
    }

    public int getStructurePoints() {
        return structurePoints.get();
    }

    public IntegerProperty structurePointsProperty() {
        return structurePoints;
    }

    public int getShieldStrength() {
        return shieldStrength.get();
    }

    public IntegerProperty shieldStrengthProperty() {
        return shieldStrength;
    }

    public int getAttackPower() {
        return attackPower.get();
    }

    public IntegerProperty attackPowerProperty() {
        return attackPower;
    }

    public int getNumbers() {
        return numbers.get();
    }

    public IntegerProperty numbersProperty() {
        return numbers;
    }
}
