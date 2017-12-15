package ogamebot.units.warfare;

import ogamebot.units.astroObjects.CelestialBody;

/**
 *
 */
public class MoveableFleet extends Fleet {

    public MoveableFleet(CelestialBody owner) {
        super(owner);
    }

    public MoveableFleet(Fleet fleet) {
        super(fleet.getOwner());
    }
}
