package ogamebot.units.astroObjects;

import ogamebot.comp.Player;
import ogamebot.comp.Position;
import ogamebot.units.units.Resource;

public class PlanetBuilder {
    private final String name;
    private int maxT;
    private int fields;
    private Position position;
    private Moon moon;
    private Resource debrisField;
    private Player player;

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

    public Planet createPlanet() {
        Planet planet = new Planet(maxT, fields, position, moon, debrisField, player, name);
        if (moon != null) {
            moon.setPlanet(planet);
        }
        return planet;
    }
}