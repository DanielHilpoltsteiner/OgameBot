package ogamebot.data.daos;

import gorgon.external.*;
import ogamebot.comp.Position;
import ogamebot.units.astroObjects.Moon;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.astroObjects.PlanetBuilder;
import ogamebot.units.building.Building;
import ogamebot.units.units.Resource;
import ogamebot.units.warfare.DefenceUnit;
import ogamebot.units.warfare.Ships;

import java.util.Collection;
import java.util.List;

import static gorgon.external.Modifier.NOT_NULL;
import static gorgon.external.Ratio.ONE_TO_MANY;
import static gorgon.external.Ratio.ONE_TO_ONE;
import static gorgon.external.Type.*;

/**
 *
 */
public class PlanetDao extends DataTable<Planet> {
    private Relation<Planet, String> name = Relate.build(ONE_TO_ONE, "name", TEXT, Planet::getName, NOT_NULL);
    private Relation<Planet, Integer> maxT = Relate.build(ONE_TO_ONE, "maxT", INTEGER, Planet::getMaxT, NOT_NULL);
    private Relation<Planet, Integer> fields = Relate.build(ONE_TO_ONE, "basicFields", INTEGER, Planet::getBasicFields, NOT_NULL);

    private Relation<Planet, Integer> galaxy = Relate.build(ONE_TO_ONE, "galays", INTEGER, planet -> planet.getPosition().getGalaxy(), NOT_NULL);
    private Relation<Planet, Integer> solarSystem = Relate.build(ONE_TO_ONE, "solarSystem", INTEGER, planet -> planet.getPosition().getSolarSystem(), NOT_NULL);
    private Relation<Planet, Integer> planetPosition = Relate.build(ONE_TO_ONE, "planetPosition", INTEGER, planet -> planet.getPosition().getSystemPosition(), NOT_NULL);

    private Relation<Planet, Resource> debris = Relate.build(ONE_TO_ONE, Resource.class, ID, Planet::getDebrisField);
    private Relation<Planet, Moon> moon = Relate.build(ONE_TO_ONE, Moon.class, ID, Planet::getMoon);

    private Relation<Planet, Collection<DefenceUnit>> defences = Relate.build(ONE_TO_MANY, DefenceUnit.class, ID, Planet::getDefences, NOT_NULL);
    private Relation<Planet, Collection<Ships>> ships = Relate.build(ONE_TO_MANY, Ships.class, ID, Planet::getShips, NOT_NULL);
    private Relation<Planet, Collection<Building>> buildings = Relate.build(ONE_TO_MANY, Building.class, ID, Planet::getBuildings, NOT_NULL);


    protected PlanetDao() {
        super("PLANET_TABLE");
    }

    @Override
    public List<Relation<Planet, ?>> getOneToOne() {
        return List.of(name, maxT, fields, galaxy, solarSystem, planetPosition, debris, moon);
    }

    @Override
    public List<Relation<Planet, ?>> getOneToMany() {
        return List.of(defences, ships, buildings);
    }

    @Override
    public Planet getData(Result<Planet> result) throws PersistenceException {
        final String name = result.get(this.name);
        final Integer maxT = result.get(this.maxT);
        final Integer fields = result.get(this.fields);
        final Integer galaxy = result.get(this.galaxy);
        final Integer solarSystem = result.get(this.solarSystem);
        final Integer planetPosition = result.get(this.planetPosition);
        final Resource debris = result.get(this.debris);
        final Moon moon = result.get(this.moon);

        final Collection<DefenceUnit> defences = result.get(this.defences);
        final Collection<Ships> ships = result.get(this.ships);
        final Collection<Building> buildings = result.get(this.buildings);

        final Planet planet = new PlanetBuilder(name)
                .setPosition(new Position(galaxy, solarSystem, planetPosition))
                .setMaxT(maxT)
                .setFields(fields)
                .setDebrisField(debris)
                .setMoon(moon)
                .setDefences(defences)
                .setShips(ships)
                .setBuildings(buildings)
                .createPlanet();
        if (moon != null) {
            moon.setOwner(planet);
        }
        return planet;
    }
}
