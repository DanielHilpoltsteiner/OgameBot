package units.units;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import tools.Condition;

/**
 *
 */
public class Resource {
    private IntegerProperty metal = new SimpleIntegerProperty();
    private IntegerProperty crystal = new SimpleIntegerProperty();
    private IntegerProperty deut = new SimpleIntegerProperty();

    public Resource() {
        this(0, 0, 0);
    }

    public Resource(int metal, int crystal, int deut) {
        Condition.check().positive(metal, crystal, deut);
        this.metal.set(metal);
        this.crystal.set(crystal);
        this.deut.set(deut);
    }

    public int getMetal() {
        return metal.get();
    }

    public int getCrystal() {
        return crystal.get();
    }

    public int getDeut() {
        return deut.get();
    }

    public IntegerProperty metalProperty() {
        return metal;
    }

    public IntegerProperty crystalProperty() {
        return crystal;
    }

    public IntegerProperty deutProperty() {
        return deut;
    }
}
