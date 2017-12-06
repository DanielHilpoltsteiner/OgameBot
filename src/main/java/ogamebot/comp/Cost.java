package ogamebot.comp;

import ogamebot.units.units.Resource;
import tools.Condition;

import java.math.BigInteger;

/**
 *
 */
public class Cost {
    private Resource resource;

    public Cost(Resource resource) {
        this.resource = resource;
    }

    public Cost(BigInteger metal, BigInteger crystal, BigInteger deut) {
        Condition.check().nonNull(metal, crystal, deut);
        this.resource = new Resource(metal, crystal, deut);
    }

    public Cost(long metal, long crystal, long deut) {
        Condition.check().positive(metal, crystal, deut);
        this.resource = new Resource(metal, crystal, deut);
    }

    public BigInteger getMetal() {
        return resource.getMetal();
    }

    public BigInteger getCrystal() {
        return resource.getCrystal();
    }

    public BigInteger getDeut() {
        return resource.getDeut();
    }

    @Override
    public String toString() {
        return "Cost{" +
                "resource=" + resource +
                '}';
    }
}
