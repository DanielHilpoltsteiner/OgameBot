package units.warfare;

import units.UnitType;

/**
 *
 */
public enum DefenceType implements UnitType<DefenceUnit> {
    LIGHT_LASER("Leichtes Lasergeschütz"),
    HEAVY_LASER("Schweres Lasergeschütz"),
    GAUSS_CANNON("Gaußkanone"),
    ION_SENTRY("Ionengeschütz"),
    PLASMA_SENTRY("Plasmawerfer"),
    SMALL_SHIELD("Kleine Schildkuppel"),
    GREAT_SHIELD("Große Schildkuppel"),;

    private final String name;

    DefenceType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DefenceUnit create() {
        return new DefenceUnit();
    }
}
