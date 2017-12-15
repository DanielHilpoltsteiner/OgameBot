package ogamebot.comp;

import tools.Condition;

/**
 *
 */
public class Position {
    public static final Position EMPTY = new Position(0, 0, 0);
    private int galaxy;
    private int solarSystem;
    private int planetPosition;

    public Position(int galaxy, int solarSystem, int planetPosition) {
        Condition.check().positive(galaxy, solarSystem, planetPosition);
        this.galaxy = galaxy;
        this.solarSystem = solarSystem;
        this.planetPosition = planetPosition;
    }

    public int getGalaxy() {
        return galaxy;
    }

    public int getSolarSystem() {
        return solarSystem;
    }

    public int getSystemPosition() {
        return planetPosition;
    }

}
