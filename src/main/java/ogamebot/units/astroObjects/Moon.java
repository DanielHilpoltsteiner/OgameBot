package ogamebot.units.astroObjects;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import ogamebot.comp.Player;
import ogamebot.units.UnitType;
import ogamebot.units.building.Building;
import ogamebot.units.building.MoonBuilding;
import ogamebot.units.warfare.DefenceUnit;
import ogamebot.units.warfare.Ship;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
public class Moon implements CelestialBody {
    private int maxT;
    private int fields;
    private String name;
    private NumberBinding usedFields;

    private Planet planet;
    private Map<MoonBuilding, Building> buildingMap = new HashMap<>();

    @Override
    public String getName() {
        return name;
    }

    private Map<UnitType<Ship>, Ship> ships = new HashMap<>();
    private Map<UnitType<DefenceUnit>, DefenceUnit> defences = new HashMap<>();

    @Override
    public Building getBuilding(UnitType<Building> type) {
        if (!(type instanceof MoonBuilding)) {
            throw new IllegalArgumentException();
        }
        Building building = buildingMap.get(type);

        if (building == null) {
            building = type.create();

            if (usedFields == null) {
                usedFields = Bindings.add(0, building.counterProperty());
            } else {
                usedFields.add(building.counterProperty());
            }

            buildingMap.put((MoonBuilding) type, building);
        }

        return building;
    }

    public int getMaxT() {
        return maxT;
    }

    public int getFields() {
        return fields;
    }

    public Number getUsedFields() {
        return usedFields.getValue();
    }

    public NumberBinding usedFieldsProperty() {
        return usedFields;
    }

    @Override
    public Player getPlayer() {
        return planet.getPlayer();
    }

    @Override
    public Ship getShip(UnitType<Ship> type) {
        Ship ship = ships.get(type);

        if (ship == null) {
            ship = type.create();
            ships.put(type, ship);
        }

        return ship;
    }

    @Override
    public DefenceUnit getDefence(UnitType<DefenceUnit> type) {
        DefenceUnit defenceUnit = defences.get(type);

        if (defenceUnit == null) {
            defenceUnit = type.create();
            defences.put(type, defenceUnit);
        }

        return defenceUnit;
    }

    void setPlanet(Planet planet) {
        Objects.requireNonNull(planet);
        this.planet = planet;
    }
}
