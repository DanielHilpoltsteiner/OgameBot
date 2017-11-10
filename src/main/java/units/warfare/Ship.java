package units.warfare;

import comp.Buildable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 */
public class Ship extends MobileWarfareUnit implements Machine, Buildable {
    private IntegerProperty numbers = new SimpleIntegerProperty();

    public Ship(RapidFire rapidFire, int structurePoints, int shieldStrength, int attackPower, int speed, int load_capacity, int fuel_consumption) {
        super(rapidFire, structurePoints, shieldStrength, attackPower, speed, load_capacity, fuel_consumption);
    }

    void setNumbers(int numbers) {
        this.numbers.set(numbers);
    }

    @Override
    public IntegerProperty counterProperty() {
        return numbers;
    }
}
