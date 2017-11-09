package comp;

import tools.Condition;
import units.units.Resource;

/**
 *
 */
public class Cost {
    private Resource resource;

    public Cost(Resource resource) {
        this.resource = resource;
    }

    public Cost(int metal, int crystal, int deut) {
        Condition.check().positive(metal, crystal, deut);
        this.resource = new Resource(metal, crystal, deut);
    }

    public Resource getResource() {
        return resource;
    }
}
