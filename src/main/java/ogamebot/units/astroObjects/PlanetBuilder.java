package ogamebot.units.astroObjects;

import ogamebot.comp.Player;
import ogamebot.comp.Position;
import ogamebot.units.building.Building;
import ogamebot.units.units.Resource;
import ogamebot.units.warfare.DefenceUnit;
import ogamebot.units.warfare.Ships;

import java.util.ArrayList;
import java.util.Collection;

public class PlanetBuilder {
    private final String name;
    private int maxT;
    private int fields;
    private Position position;
    private Moon moon;
    private Resource debrisField;
    private Player player;
    private Collection<DefenceUnit> defences = new ArrayList<>();
    private Collection<Ships> ships = new ArrayList<>();
    private Collection<Building> buildings = new ArrayList<>();

    public PlanetBuilder(String name) {
        this.name = name;
    }

    public PlanetBuilder setMaxT(int maxT) {
        this.maxT = maxT;
        return this;
    }

    public PlanetBuilder setFields(int fields) {
        this.fields = fields;
        return this;
    }

    public PlanetBuilder setPosition(Position position) {
        this.position = position;
        return this;
    }

    public PlanetBuilder setMoon(Moon moon) {
        this.moon = moon;
        return this;
    }

    public PlanetBuilder setDebrisField(Resource debrisField) {
        this.debrisField = debrisField;
        return this;
    }

    public PlanetBuilder setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public PlanetBuilder setDefences(Collection<DefenceUnit> defences) {
        this.defences = defences;
        return this;
    }

    public PlanetBuilder setShips(Collection<Ships> ships) {
        this.ships = ships;
        return this;
    }

    public PlanetBuilder setBuildings(Collection<Building> buildings) {
        this.buildings = buildings;
        return this;
    }

    public Planet createPlanet() {
        return new Planet(maxT, fields, position, moon, debrisField, player, name, defences, ships, buildings);
    }
}