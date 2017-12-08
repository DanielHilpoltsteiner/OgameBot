package ogamebot.units.research;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import ogamebot.comp.Cost;
import ogamebot.comp.UpgradeAble;
import ogamebot.units.UnitType;

/**
 *
 */
public class Research implements UpgradeAble {
    private final ResearchFields fields;
    private final ReadOnlyStringProperty name;
    private IntegerProperty level = new SimpleIntegerProperty();


    public Research(ResearchFields fields) {
        this.fields = fields;
        name = new SimpleStringProperty(fields.getName());
    }

    @Override
    public Cost getCost() {
        return fields.getCost(getCounter());
    }

    @Override
    public IntegerProperty counterProperty() {
        return level;
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
    public int getCounter() {
        return level.get();
    }

    @Override
    public UnitType<?> getType() {
        return fields;
    }

    @Override
    public int compareTo(UpgradeAble o) {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Research research = (Research) o;

        return getName().equals(research.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return "Research{" +
                "name=" + name +
                ", level=" + level +
                '}';
    }
}
