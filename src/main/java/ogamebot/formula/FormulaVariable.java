package ogamebot.formula;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 */
public class FormulaVariable {
    private IntegerProperty value = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();

    public FormulaVariable(String name) {
        this.name.setValue(name);
    }

    public FormulaVariable(int value, String name) {
        this.value.setValue(value);
        this.name.setValue(name);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getValue() {
        return value.get();
    }

    public IntegerProperty valueProperty() {
        return value;
    }
}
