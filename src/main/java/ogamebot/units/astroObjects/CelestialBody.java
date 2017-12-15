package ogamebot.units.astroObjects;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import ogamebot.comp.Player;
import ogamebot.comp.Reachable;
import ogamebot.units.UnitType;
import ogamebot.units.building.Building;
import ogamebot.units.building.BuildingType;
import ogamebot.units.building.Buildings;
import ogamebot.units.units.Resource;
import ogamebot.units.warfare.Defence;
import ogamebot.units.warfare.DefenceUnit;
import ogamebot.units.warfare.Fleet;
import ogamebot.units.warfare.Ships;

import java.util.Collection;

/**
 *
 */
public interface CelestialBody extends Reachable {
    String getName();

    Building getBuilding(BuildingType type);

    Player getPlayer();

    Ships getShip(UnitType<Ships> shipType);

    DefenceUnit getDefence(UnitType<DefenceUnit> defence);

    ObjectProperty<Resource> resourceProperty();

    Collection<Ships> getShips();

    Collection<DefenceUnit> getDefences();

    Collection<Building> getBuildings();

    ObservableValue<Number> usedFieldsProperty();

    Fleet getFleet();

    Defence getDefence();

    Buildings getBuilding();
}
