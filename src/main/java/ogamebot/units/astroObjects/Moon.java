package ogamebot.units.astroObjects;

import gorgon.external.DataAccess;
import gorgon.external.GorgonEntry;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import ogamebot.comp.Player;
import ogamebot.comp.Position;
import ogamebot.data.daos.MoonDao;
import ogamebot.units.UnitType;
import ogamebot.units.building.Building;
import ogamebot.units.building.BuildingType;
import ogamebot.units.building.Buildings;
import ogamebot.units.units.Resource;
import ogamebot.units.warfare.*;

import java.util.Collection;
import java.util.Objects;

/**
 *
 */
@DataAccess(MoonDao.class)
public class Moon implements CelestialBody, GorgonEntry {
    private int maxT;
    private int fields;
    private String name;

    private Planet owner;
    private ObjectProperty<Resource> resource;

    private Buildings buildings = Buildings.moonBuildings();
    private Fleet fleet = new StationaryFleet(this);
    private Defence defence = new Defence(this);

    public Moon() {
        resource = new SimpleObjectProperty<>(new Resource());
    }

    public Moon(int maxT, int fields, String name, Collection<Building> buildings, Resource resource, Collection<Ships> ships, Collection<DefenceUnit> defences) {
        this.maxT = maxT;
        this.fields = fields;
        this.name = name;
        this.resource.set(resource);

        this.defence = new Defence(this, defences);
        this.fleet = new StationaryFleet(this, ships);
        this.buildings = Buildings.moonBuildings(buildings);
    }

    public Moon(int maxT, int fields, String name) {
        this.maxT = maxT;
        this.fields = fields;
        this.name = name;
    }

    @Override
    public Building getBuilding(BuildingType type) {
        return buildings.getValue(type);
    }

    @Override
    public Buildings getBuilding() {
        return buildings;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getMaxT() {
        return maxT;
    }

    public int getFields() {
        return fields;
    }

    public Number getUsedFields() {
        return usedFieldsProperty().getValue();
    }

    @Override
    public NumberBinding usedFieldsProperty() {
        return buildings.usedFieldsProperty();
    }

    @Override
    public Player getPlayer() {
        return owner.getPlayer();
    }

    @Override
    public Ships getShip(UnitType<Ships> type) {
        return fleet.getValue((ShipType) type);
    }

    @Override
    public DefenceUnit getDefence(UnitType<DefenceUnit> type) {
        return defence.getValue((DefenceType) type);
    }

    public Collection<Ships> getShips() {
        return fleet.getFleet();
    }

    public Collection<DefenceUnit> getDefences() {
        return defence.getValues();
    }

    @Override
    public Collection<Building> getBuildings() {
        return buildings.getValues();
    }

    @Override
    public ObjectProperty<Resource> resourceProperty() {
        return resource;
    }

    public Planet getOwner() {
        return owner;
    }

    public void setOwner(Planet owner) {
        Objects.requireNonNull(owner);
        this.owner = owner;
    }

    @Override
    public int compareTo(GorgonEntry gorgonEntry) {
        if (gorgonEntry == null) return -1;
        if (gorgonEntry == this) return 0;
        if (gorgonEntry.getClass() != getClass()) return -1;

        Moon moon = (Moon) gorgonEntry;

        int compare = getName().compareTo(moon.getName());

        if (compare == 0) {
            compare = getOwner().compareTo(moon.getOwner());
        }
        return compare;
    }

    @Override
    public Position getPosition() {
        return owner.getPosition();
    }

    @Override
    public Fleet getFleet() {
        return fleet;
    }

    @Override
    public Defence getDefence() {
        return defence;
    }
}
