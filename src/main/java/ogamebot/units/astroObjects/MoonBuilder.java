package ogamebot.units.astroObjects;

import ogamebot.units.building.Building;
import ogamebot.units.units.Resource;
import ogamebot.units.warfare.DefenceUnit;
import ogamebot.units.warfare.Ships;

import java.util.Collection;

public class MoonBuilder {
    private int maxT;
    private int fields;
    private String name;
    private Collection<Building> buildings;
    private Resource resource;
    private Collection<Ships> ships;
    private Collection<DefenceUnit> defences;

    public MoonBuilder setMaxT(int maxT) {
        this.maxT = maxT;
        return this;
    }

    public MoonBuilder setFields(int fields) {
        this.fields = fields;
        return this;
    }

    public MoonBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public MoonBuilder setBuildings(Collection<Building> buildings) {
        this.buildings = buildings;
        return this;
    }

    public MoonBuilder setResource(Resource resource) {
        this.resource = resource;
        return this;
    }

    public MoonBuilder setShips(Collection<Ships> ships) {
        this.ships = ships;
        return this;
    }

    public MoonBuilder setDefences(Collection<DefenceUnit> defences) {
        this.defences = defences;
        return this;
    }

    public Moon createMoon() {
        return new Moon(maxT, fields, name, buildings, resource, ships, defences);
    }
}