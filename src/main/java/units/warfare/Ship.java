package units.warfare;

import comp.Buildable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 */
public class Ship implements Machine, Buildable {
    private RapidFire rapidFire;
    private int structurePoints;
    private int shieldStrength;
    private int attackPower;
    private int speed;
    private int load_capacity;
    private int fuel_consumption;
    private IntegerProperty numbers = new SimpleIntegerProperty();

    public RapidFire getRapidFire() {
        return rapidFire;
    }

    public int getStructurePoints() {
        return structurePoints;
    }

    public int getShieldStrength() {
        return shieldStrength;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getSpeed() {
        return speed;
    }

    public int getLoad_capacity() {
        return load_capacity;
    }

    public int getFuel_consumption() {
        return fuel_consumption;
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
}
