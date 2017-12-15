package ogamebot.units.astroObjects;

import gorgon.external.DataAccess;
import gorgon.external.GorgonEntry;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import ogamebot.comp.Player;
import ogamebot.comp.Position;
import ogamebot.data.daos.PlanetDao;
import ogamebot.units.UnitType;
import ogamebot.units.building.Building;
import ogamebot.units.building.BuildingType;
import ogamebot.units.building.Buildings;
import ogamebot.units.units.Resource;
import ogamebot.units.warfare.*;
import tools.Condition;

import java.util.Collection;

/**
 *
 */
@DataAccess(PlanetDao.class)
public class Planet implements CelestialBody, GorgonEntry {
    private final Buildings buildings;
    private int maxT;
    private final Fleet fleet;

    private Position position;
    private ObjectProperty<Moon> moon = new SimpleObjectProperty<>();
    private Resource debrisField;
    private final Defence defence;
    private StringProperty name = new SimpleStringProperty();
    private int basicFields;
    private ObjectProperty<Resource> planetResource = new SimpleObjectProperty<>(new Resource());
    private Player player;


    public Planet(int maxT, int fields, Position position, Moon moon, Resource debrisField, Player player, String name) {
        this.maxT = maxT;
        this.basicFields = fields;
        this.position = position;
        this.moon.set(moon);
        this.debrisField = debrisField;
        this.player = player;
        this.name.set(name);
        this.buildings = Buildings.planetBuildings();
        this.fleet = new StationaryFleet(this);
        this.defence = new Defence(this);
    }

    Planet(int maxT, int fields, Position position, Moon moon, Resource debrisField, Player player, String name, Collection<DefenceUnit> defences, Collection<Ships> ships, Collection<Building> buildings) {
        Condition.check().positive(fields, maxT);
        this.maxT = maxT;
        this.basicFields = fields;
        this.position = position;
        this.moon.set(moon);
        this.debrisField = debrisField;
        this.player = player;
        this.name.set(name);

        this.defence = new Defence(this, defences);
        this.fleet = new StationaryFleet(this, ships);
        this.buildings = Buildings.planetBuildings(buildings);
    }

    @Override
    public Collection<Ships> getShips() {
        return fleet.getFleet();
    }

    @Override
    public Collection<DefenceUnit> getDefences() {
        return defence.getValues();
    }

    @Override
    public Collection<Building> getBuildings() {
        return buildings.getValues();
    }

    public Fleet getFleet() {
        return fleet;
    }

    public Defence getDefence() {
        return defence;
    }

    public Buildings getBuilding() {
        return buildings;
    }

    public void destroyMoon() {
        moon.set(null);
    }

    public int getMaxT() {
        return maxT;
    }

    public int getBasicFields() {
        return basicFields;
    }

    public int getUsedFields() {
        return usedFieldsProperty().getValue().intValue();
    }

    @Override
    public ObservableValue<Number> usedFieldsProperty() {
        return buildings.usedFieldsProperty();
    }

    public Position getPosition() {
        return position == null ? Position.EMPTY : position;
    }

    public Moon getMoon() {
        return moon.get();
    }

    public ObjectProperty<Moon> moonProperty() {
        return moon;
    }

    public Resource getDebrisField() {
        return debrisField;
    }

    public Resource getPlanetResource() {
        return planetResource.get();
    }

    @Override
    public ObjectProperty<Resource> resourceProperty() {
        return planetResource;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public Building getBuilding(BuildingType type) {
        return buildings.getValue(type);
    }

    @Override
    public Ships getShip(UnitType<Ships> type) {
        return fleet.getValue((ShipType) type);
    }

    @Override
    public DefenceUnit getDefence(UnitType<DefenceUnit> type) {
        return defence.getValue((DefenceType) type);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Planet planet = (Planet) o;

        if (getMaxT() != planet.getMaxT()) return false;
        if (!getPlayer().equals(planet.getPlayer())) return false;
        return getName().equals(planet.getName());
    }

    @Override
    public int hashCode() {
        int result = getMaxT();
        result = 31 * result + getPlayer().hashCode();
        result = 31 * result + getName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Planet{" +
                "name=" + name.get() +
                ", maxT=" + maxT +
                ", basicFields=" + basicFields +
                ", position=" + position +
                ", moon=" + moon.get() +
                ", debrisField=" + debrisField +
                ", planetResource=" + planetResource.get() +
                ", player=" + player.getName() +
                ", buildings=" + buildings +
                ", fleet=" + fleet +
                ", defence=" + defence +
                '}';
    }

    @Override
    public int compareTo(GorgonEntry gorgonEntry) {
        if (gorgonEntry == null) return -1;
        if (gorgonEntry == this) return 0;
        if (gorgonEntry.getClass() != getClass()) return -1;

        Planet planet = (Planet) gorgonEntry;
        int compare = getName().compareTo(planet.getName());

        if (compare == 0) {
            compare = getPlayer().compareTo(planet.getPlayer());
        }
        return compare;
    }

    public void setMoon(Moon moon) {
        this.moon.set(moon);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
