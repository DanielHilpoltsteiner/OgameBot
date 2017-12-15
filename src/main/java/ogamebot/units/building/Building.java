package ogamebot.units.building;

import gorgon.external.DataAccess;
import gorgon.external.GorgonEntry;
import javafx.beans.property.*;
import ogamebot.comp.Cost;
import ogamebot.comp.UpgradeAble;
import ogamebot.data.daos.BuildingsDao;

/**
 *
 */
@DataAccess(BuildingsDao.class)
public class Building implements UpgradeAble, GorgonEntry {
    private final ReadOnlyStringProperty name;
    private IntegerProperty level = new SimpleIntegerProperty();
    private BuildingType type;
    private DoubleProperty output = new SimpleDoubleProperty(1);

    public Building(BuildingType type) {
        this.type = type;
        name = new SimpleStringProperty(type.getName());
    }

    public static Building create(BuildingType buildingType, Integer level, double output) {
        final Building building = new Building(buildingType);
        building.counterProperty().set(level);
        building.outputProperty().setValue(output);
        return building;
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

    public BuildingType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Building{" +
                "name=" + name.get() +
                ", level=" + level.get() +
                '}';
    }

    @Override
    public int compareTo(GorgonEntry gorgonEntry) {
        if (gorgonEntry == null) return -1;
        if (gorgonEntry == this) return 0;
        if (gorgonEntry.getClass() != getClass()) return -1;

        Building building = (Building) gorgonEntry;
        return getName().compareTo(building.getName());
    }
}
