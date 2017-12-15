package ogamebot.units.units;

import gorgon.external.DataAccess;
import gorgon.external.GorgonEntry;
import ogamebot.data.daos.ResourceDao;
import tools.Condition;

import java.math.BigInteger;

/**
 *
 */
@DataAccess(ResourceDao.class)
public class Resource implements GorgonEntry {
    private BigInteger metal = BigInteger.ZERO;
    private BigInteger crystal = BigInteger.ZERO;
    private BigInteger deut = BigInteger.ZERO;

    public Resource() {
        this(0, 0, 0);
    }

    public Resource(BigInteger metal, BigInteger crystal, BigInteger deut) {
        Condition.check().nonNull(metal, crystal, deut);
        this.metal = metal;
        this.crystal = crystal;
        this.deut = deut;
    }

    public Resource(int metal, int crystal, int deut) {
        Condition.check().positive(metal, crystal, deut);
        this.metal = BigInteger.valueOf(metal);
        this.crystal = BigInteger.valueOf(crystal);
        this.deut = BigInteger.valueOf(deut);

    }

    public Resource(long metal, long crystal, long deut) {
        Condition.check().positive(metal, crystal, deut);
        this.metal = BigInteger.valueOf(metal);
        this.crystal = BigInteger.valueOf(crystal);
        this.deut = BigInteger.valueOf(deut);
    }

    public BigInteger getMetal() {
        return metal;
    }

    public BigInteger getCrystal() {
        return crystal;
    }

    public BigInteger getDeut() {
        return deut;
    }

    public Resource add(Resource resource) {
        final BigInteger newMet = getMetal().add(resource.getMetal());
        final BigInteger newCrys = getCrystal().add(resource.getCrystal());
        final BigInteger newDeut = getDeut().add(resource.getDeut());

        return new Resource(newMet, newCrys, newDeut);
    }

    public Resource subtract(Resource resource) {
        final BigInteger newMet = getMetal().subtract(resource.getMetal());
        final BigInteger newCrys = getCrystal().subtract(resource.getCrystal());
        final BigInteger newDeut = getDeut().subtract(resource.getDeut());

        return new Resource(newMet, newCrys, newDeut);
    }

    @Override
    public String toString() {
        return "Resource{" +
                "metal=" + metal +
                ", crystal=" + crystal +
                ", deut=" + deut +
                '}';
    }

    @Override
    public int compareTo(GorgonEntry gorgonEntry) {
        if (gorgonEntry == null) return -1;
        if (gorgonEntry == this) return 0;
        if (gorgonEntry.getClass() != getClass()) return -1;

        Resource resource = (Resource) gorgonEntry;
        int compare = getMetal().compareTo(resource.getMetal());

        if (compare == 0) {
            compare = getCrystal().compareTo(resource.getCrystal());
        }

        if (compare == 0) {
            compare = getDeut().compareTo(resource.getDeut());
        }
        return compare;
    }
}
