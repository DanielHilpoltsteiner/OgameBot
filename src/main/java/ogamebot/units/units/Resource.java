package ogamebot.units.units;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import tools.Condition;

import java.math.BigInteger;

/**
 *
 */
public class Resource {
    private ObjectProperty<BigInteger> metal = new SimpleObjectProperty<>(BigInteger.ZERO);
    private ObjectProperty<BigInteger> crystal = new SimpleObjectProperty<>(BigInteger.ZERO);
    private ObjectProperty<BigInteger> deut = new SimpleObjectProperty<>(BigInteger.ZERO);

    public Resource() {
        this(0, 0, 0);
    }

    public Resource(BigInteger metal, BigInteger crystal, BigInteger deut) {
        Condition.check().nonNull(metal, crystal, deut);
        this.metal.set(metal);
        this.crystal.set(crystal);
        this.deut.set(deut);
    }

    public Resource(int metal, int crystal, int deut) {
        Condition.check().positive(metal, crystal, deut);
        this.metal.set(BigInteger.valueOf(metal));
        this.crystal.set(BigInteger.valueOf(crystal));
        this.deut.set(BigInteger.valueOf(deut));
    }

    public Resource(long metal, long crystal, long deut) {
        Condition.check().positive(metal, crystal, deut);
        this.metal.set(BigInteger.valueOf(metal));
        this.crystal.set(BigInteger.valueOf(crystal));
        this.deut.set(BigInteger.valueOf(deut));
    }

    public BigInteger getMetal() {
        return metal.get();
    }

    public BigInteger getCrystal() {
        return crystal.get();
    }

    public BigInteger getDeut() {
        return deut.get();
    }

    public ObjectProperty<BigInteger> metalProperty() {
        return metal;
    }

    public ObjectProperty<BigInteger> crystalProperty() {
        return crystal;
    }

    public ObjectProperty<BigInteger> deutProperty() {
        return deut;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "metal=" + metal.get() +
                ", crystal=" + crystal.get() +
                ", deut=" + deut.get() +
                '}';
    }
}
