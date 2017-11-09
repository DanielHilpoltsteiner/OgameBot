package units.warfare;

import comp.Buildable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 */
public class DefenceUnit implements Machine, Buildable {
    private boolean onlyOne;
    private IntegerProperty numbers = new SimpleIntegerProperty();

    public boolean isOnlyOne() {
        return onlyOne;
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
