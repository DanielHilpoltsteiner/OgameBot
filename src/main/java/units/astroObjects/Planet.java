package units.astroObjects;

import comp.Player;
import comp.Position;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import units.Building.Building;
import units.Building.PlanetBuilding;
import units.units.Resource;
import units.warfare.DefenceType;
import units.warfare.DefenceUnit;
import units.warfare.Ship;
import units.warfare.ShipType;

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
    private Map<DefenceType, DefenceUnit> defences = new HashMap<>();
    private Map<ShipType, Ship> ships = new HashMap<>();
    private StringProperty name = new SimpleStringProperty();

    Planet(int maxT, int fields, Position position, Moon moon, Resource debrisField, Player player, String name) {
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

    public Building getBuilding(PlanetBuilding type) {
        Building building = buildingMap.get(type);

        if (building == null) {
            building = type.create();

            if (usedFields == null) {
                usedFields = Bindings.add(0, building.levelProperty());
            } else {
                usedFields.add(building.levelProperty());
            }

            buildingMap.put(type, building);
        }

        return building;
    }

    public Ship getShip(ShipType type) {
        Ship ship = ships.get(type);

        if (ship == null) {
            ship = type.create();
            ships.put(type, ship);
        }

        return ship;
    }

    public DefenceUnit getDefence(DefenceType type) {
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
}
