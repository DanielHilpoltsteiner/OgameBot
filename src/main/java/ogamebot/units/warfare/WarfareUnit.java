package ogamebot.units.warfare;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import ogamebot.comp.UpgradeAble;
import tools.Condition;

/**
 *
 */
abstract class WarfareUnit implements UpgradeAble {
    private RapidFire rapidFire;
    private IntegerProperty structurePoints = new SimpleIntegerProperty();
    IntegerProperty numbers = new SimpleIntegerProperty();
    private DoubleProperty shieldStrength = new SimpleDoubleProperty();
    private DoubleProperty attackPower = new SimpleDoubleProperty();

    WarfareUnit(RapidFire rapidFire, int structurePoints, double shieldStrength, double attackPower) {
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

    public double getShieldStrength() {
        return shieldStrength.get();
    }

    public DoubleProperty shieldStrengthProperty() {
        return shieldStrength;
    }

    public double getAttackPower() {
        return attackPower.get();
    }

    public DoubleProperty attackPowerProperty() {
        return attackPower;
    }

    public int getNumbers() {
        return numbers.get();
    }

    public IntegerProperty numbersProperty() {
        return numbers;
    }

    @Override
    public IntegerProperty counterProperty() {
        return numbers;
    }

    @Override
    public int getCounter() {
        return numbers.get();
    }
}
