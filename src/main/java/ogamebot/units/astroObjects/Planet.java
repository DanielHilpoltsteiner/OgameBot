package ogamebot.units.astroObjects;

import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ogamebot.comp.Player;
import ogamebot.comp.Position;
import ogamebot.units.UnitType;
import ogamebot.units.building.Building;
import ogamebot.units.building.PlanetBuilding;
import ogamebot.units.units.Resource;
import ogamebot.units.warfare.DefenceUnit;
import ogamebot.units.warfare.Ship;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Planet implements CelestialBody {
    private int maxT;
    private int fields;
    private transient NumberBinding usedFields;

    private Position position;
    private ObjectProperty<Moon> moon = new SimpleObjectProperty<>();
    private Resource debrisField;
    private Resource planetResource;
    private Instant lastResourceUpdate;

    private transient Player player;

    private Map<PlanetBuilding, Building> buildingMap = new HashMap<>();
    private Map<UnitType<DefenceUnit>, DefenceUnit> defences = new HashMap<>();
    private Map<UnitType<Ship>, Ship> ships = new HashMap<>();
    private StringProperty name = new SimpleStringProperty();

    public Planet(int maxT, int fields, Position position, Moon moon, Resource debrisField, Player player, String name) {
        this.maxT = maxT;
        this.fields = fields;
        this.position = position;
        this.moon.set(moon);
        this.debrisField = debrisField;
        this.player = player;
        this.name.set(name);
        this.planetResource = new Resource();
    }


    public void createMoon() {
        moon.set(new Moon());
        moon.get().setPlanet(this);
    }

    public void destroyMoon() {
        moon.set(null);
    }

    public int getMaxT() {
        return maxT;
    }

    public int getFields() {
        return fields;
    }

    public int getUsedFields() {
        return usedFields.intValue();
    }

    public Position getPosition() {
        return position;
    }

    public Moon getMoon() {
        return moon.get();
    }

    public Resource getDebrisField() {
        return debrisField;
    }

    public Resource getPlanetResource() {
        return planetResource;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public Building getBuilding(UnitType<Building> type) {
        if (!(type instanceof PlanetBuilding)) {
            throw new IllegalArgumentException();
        }
        Building building = buildingMap.get(type);

        if (building == null) {
            building = type.create();

            if (usedFields == null) {
                usedFields = building.counterProperty().add(0);
            } else {
                usedFields.add(building.counterProperty());
            }

            buildingMap.put((PlanetBuilding) type, building);
        }

        return building;
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

    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public String getName() {
        return name.get();
    }

    public double getMetalBooster() {
        return 1;
    }

    public double getCrystalBooster() {
        return 1;
    }

    public double getDeutBooster() {
        return 1;
    }
}
