package ogamebot.data.daos;

import gorgon.external.*;
import ogamebot.units.units.Resource;

import java.util.List;

import static gorgon.external.Modifier.NOT_NULL;
import static gorgon.external.Ratio.ONE_TO_ONE;
import static gorgon.external.Type.INTEGER;

/**
 *
 */
public class ResourceDao extends DataTable<Resource> {
    private Relation<Resource, Integer> metal = Relate.build(ONE_TO_ONE, "metal", INTEGER, resource -> resource.getMetal().intValueExact(), NOT_NULL);
    private Relation<Resource, Integer> crystal = Relate.build(ONE_TO_ONE, "crystal", INTEGER, resource -> resource.getCrystal().intValueExact(), NOT_NULL);
    private Relation<Resource, Integer> deut = Relate.build(ONE_TO_ONE, "deut", INTEGER, resource -> resource.getDeut().intValueExact(), NOT_NULL);

    protected ResourceDao() {
        super("RESOURCE_TABLE");
    }

    @Override
    public List<Relation<Resource, ?>> getOneToOne() {
        return List.of(metal, crystal, deut);
    }

    @Override
    public List<Relation<Resource, ?>> getOneToMany() {
        return List.of();
    }

    @Override
    public Resource getData(Result<Resource> result) throws PersistenceException {
        final Integer metal = result.get(this.metal);
        final Integer crystal = result.get(this.crystal);
        final Integer deut = result.get(this.deut);
        return new Resource(metal, crystal, deut);
    }
}
