package units.Building;

import comp.Buildable;
import comp.Cost;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 */
public abstract class Building implements Buildable {
    private PlanetBuilding type;
    private Cost cost;
    private IntegerProperty level = new SimpleIntegerProperty();

    public PlanetBuilding getType() {
        return type;
    }

    public int getLevel() {
        return level.get();
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public Cost getCost() {
        return cost;
    }

    @Override
    public IntegerProperty counterProperty() {
        return level;
    }
}
