package ogamebot.units.building;

import javafx.beans.property.*;
import ogamebot.comp.Cost;
import ogamebot.comp.UpgradeAble;
import ogamebot.units.UnitType;

/**
 *
 */
public class Building implements UpgradeAble {
    private final ReadOnlyStringProperty name;
    private IntegerProperty level = new SimpleIntegerProperty();
    private UnitType<Building> type;
    private DoubleProperty output = new SimpleDoubleProperty(1);

    public Building(UnitType<Building> type) {
        this.type = type;
        name = new SimpleStringProperty(type.getName());
        level.addListener((observable, oldValue, newValue) -> System.out.println(newValue));
    }

    public UnitType<Building> getType() {
        return type;
    }

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public ReadOnlyStringProperty nameProperty() {
        return name;
    }

    @Override
    public Cost getCost() {
        return type.getCost(getCounter());
    }

    @Override
    public IntegerProperty counterProperty() {
        return level;
    }

    @Override
    public int getCounter() {
        return level.get();
    }

    @Override
    public int compareTo(UpgradeAble o) {
        return 0;
    }

    public double getOutput() {
        return output.get();
    }

    public DoubleProperty outputProperty() {
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Building building = (Building) o;

        return getName().equals(building.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return "Building{" +
                "name=" + name +
                ", level=" + level +
                '}';
    }
}
