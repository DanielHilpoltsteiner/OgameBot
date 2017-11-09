package units.warfare;

import units.UnitType;

/**
 *
 */
public enum ShipType implements UnitType<Ship> {
    LIGHT_FIGHTER("Leichter Jäger"),
    HEAVY_FIGHTER("Schwerer Jäger"),
    CRUISER("Kreuzer"),
    BATTLESHIP("Schlachtschiff"),
    BATTLECRUISER("Schlachtkreuzer"),
    BOMBER("Bomber"),
    DESTROYER("Zerstörer"),
    DEATH_STAR("Todesstern"),
    SMALL_TRANSPORTER("Kleiner Transporter"),
    BIG_TRANSPORTER("Großer Transporter"),
    COLONYSHIP("Kolonieschiff"),
    RECYCLER("Recycler"),
    SPY_SONDE("Spionagesonde"),
    SOLAR_SATELLITE("Solarsatellit");

    private final String name;

    ShipType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Ship create() {
        return new Ship();
    }
}
