package ogamebot.data;

import gorgon.external.Gorgon;
import gorgon.external.PersistenceException;
import ogamebot.comp.Player;
import ogamebot.comp.PlayerBuilder;
import ogamebot.comp.Universe;
import ogamebot.comp.UniverseBuilder;
import ogamebot.units.astroObjects.PlanetBuilder;
import tools.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class DataManager {
    private static DataManager manager = new DataManager();
    private Gorgon gorgon = Gorgon.create("ogame-data", FileUtils.workDirectory());

    private DataManager() {
        if (manager != null) {
            throw new IllegalStateException();
        }
    }

    public static List<Universe> get() {
        return manager.gorgon.get(Universe.class, true);
    }

    public static void add(Universe universe) {
        manager.gorgon.wrapEntry(universe);
    }

    public static void save() {
        manager.gorgon.addNew();
    }

    public static void delete(Universe universe) throws PersistenceException {
        manager.gorgon.delete(List.of(universe));
    }

    public static void startUpdate() {
        manager.gorgon.startUpdater(1, TimeUnit.MINUTES);
    }

    public static void stopUpdate() {
        manager.gorgon.stopUpdater();
    }

    public static List<Universe> getStubs() {
        Universe value = new UniverseBuilder().setDefTf(0.1).setFleetTf(0.7).setFlightSpeed(2).setEconomySpeed(3).setDonut(true).setRapidFire(true).setBonusFields(25).setName("orion").createUniverse();
        Player w = new PlayerBuilder().setName("w").setPoints(0).setHighscore(0).setDarkMatter(0).setUniverse(value).createPlayer();
        Player f = new PlayerBuilder().setName("f").setPoints(0).setHighscore(0).setDarkMatter(0).setUniverse(value).createPlayer();
        Player a = new PlayerBuilder().setName("a").setPoints(0).setHighscore(0).setDarkMatter(0).setUniverse(value).createPlayer();
        Player s = new PlayerBuilder().setName("s").setPoints(0).setHighscore(0).setDarkMatter(0).setUniverse(value).createPlayer();
        Player d = new PlayerBuilder().setName("d").setPoints(0).setHighscore(0).setDarkMatter(0).setUniverse(value).createPlayer();
        Player player1 = new PlayerBuilder().setName("!").setPoints(0).setHighscore(0).setDarkMatter(0).setUniverse(value).createPlayer();
        value.addPlayer(w);
        value.addPlayer(a);
        value.addPlayer(s);
        value.addPlayer(d);
        value.addPlayer(f);
        value.addPlayer(player1);
        player1.getPlanets().add(new PlanetBuilder("franz").setPlayer(player1).createPlanet());
        player1.getPlanets().add(new PlanetBuilder("hans").setPlayer(player1).createPlanet());

        List<Universe> entities = new ArrayList<>();
        entities.add(value);
        return entities;
    }
}
