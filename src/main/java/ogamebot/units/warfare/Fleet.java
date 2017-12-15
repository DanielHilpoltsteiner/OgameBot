package ogamebot.units.warfare;

import ogamebot.units.Container;
import ogamebot.units.astroObjects.CelestialBody;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class Fleet extends Container<Ships, ShipType> {
    private CelestialBody owner;
    private List<Ships> fleet = new ArrayList<>();

    private int maxSpeed = 0;

    public Fleet(CelestialBody owner) {
        this.owner = owner;
    }

    public Fleet() {

    }

    public void addShip(Ships ship) {
        final int speed = ship.getSpeed();
        //todo calc totalMovementSpeed

        if (maxSpeed == 0 || speed < maxSpeed) {
            maxSpeed = speed;
        }
        fleet.add(ship);
    }

    public void removeShip(Ships ship) {
        fleet.remove(ship);
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public CelestialBody getOwner() {
        return owner;
    }

    public void setOwner(CelestialBody owner) {
        this.owner = owner;
    }

    public List<Ships> getFleet() {
        return new ArrayList<>(fleet);
    }

    public void splitFleet(Fleet fleet) {

    }
}
