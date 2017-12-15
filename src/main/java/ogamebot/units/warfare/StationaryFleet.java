package ogamebot.units.warfare;

import ogamebot.units.astroObjects.CelestialBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class StationaryFleet extends Fleet {
    private Ships solarSatellites = null;
    private Ships interceptor_missiles = null;

    public StationaryFleet(CelestialBody owner) {
        super(owner);
    }

    public StationaryFleet() {
        for (ShipType type : ShipType.values()) {
            put(type, type.create());
        }
    }

    public StationaryFleet(CelestialBody owner, Collection<Ships> ships) {
        super(owner);
        List<ShipType> types = new ArrayList<>(Arrays.asList(ShipType.values()));

        for (Ships ship : ships) {
            final ShipType type = ship.getType();
            put(type, ship);
            types.remove(type);
        }
        types.forEach(type -> put(type, type.create()));
    }

    @Override
    public void addShip(Ships ship) {
        if (ship.getType() == ShipType.SOLAR_SATELLITE) {
            solarSatellites = ship;
        } else {
            super.addShip(ship);
        }
    }
}
