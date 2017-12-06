package ogamebot.units.astroObjects;

import ogamebot.comp.Player;
import ogamebot.units.UnitType;
import ogamebot.units.building.Building;
import ogamebot.units.warfare.DefenceUnit;
import ogamebot.units.warfare.Ship;

/**
 *
 */
public interface CelestialBody {
    String getName();

    Building getBuilding(UnitType<Building> type);

    Player getPlayer();

    Ship getShip(UnitType<Ship> shipType);

    DefenceUnit getDefence(UnitType<DefenceUnit> defence);
}
