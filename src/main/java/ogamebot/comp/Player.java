package ogamebot.comp;

import gorgon.external.DataAccess;
import gorgon.external.GorgonEntry;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import ogamebot.calc.Production;
import ogamebot.data.daos.PlayerDao;
import ogamebot.gui.main.displayTree.TreeAble;
import ogamebot.units.astroObjects.CelestialBody;
import ogamebot.units.astroObjects.Moon;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.research.Research;
import ogamebot.units.research.ResearchField;
import ogamebot.units.research.Researches;
import tools.Condition;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@DataAccess(PlayerDao.class)
public class Player implements TreeAble, GorgonEntry {
    private boolean isOnlinePlayer = false;

    private boolean geologist = false;
    private boolean admiral = false;
    private boolean engineer = false;
    private boolean technocrat = false;
    private boolean commander = false;

    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty points = new SimpleIntegerProperty();
    private IntegerProperty highscore = new SimpleIntegerProperty();
    private IntegerProperty playerPlace = new SimpleIntegerProperty();
    private IntegerProperty darkMatter = new SimpleIntegerProperty();

    private Researches researches = new Researches();
    private ObservableList<Planet> planets = FXCollections.observableArrayList();
    private Universe universe;

    private Map<Planet, Production> productionMap = new TreeMap<>(Comparator.comparing(Planet::getName));

    public Player(String name, Universe universe) {
        Condition.check().nonNull(name).notEmpty(name);
        this.universe = universe;
        this.name.setValue(name);

        planets.addListener((ListChangeListener<? super Planet>) observable -> {
            if (observable.next()) {
                observable.getAddedSubList().forEach(planet -> productionMap.put(planet, new Production(planet)));
                observable.getRemoved().forEach(planet -> productionMap.remove(planet));
            }
        });
    }

    public Player(String name, int points, int highscore, int darkMatter, boolean geologist, boolean admiral, boolean engineer, boolean technocrat, boolean commander, Collection<Research> researches, Collection<Planet> planets, Universe universe, boolean isOnlinePlayer) {
        this(name, points, highscore, darkMatter, universe);
        this.geologist = geologist;
        this.admiral = admiral;
        this.engineer = engineer;
        this.technocrat = technocrat;
        this.commander = commander;
        this.researches = new Researches(researches);
        this.isOnlinePlayer = isOnlinePlayer;
        this.planets.addAll(planets);
        this.planets.forEach(planet -> planet.setPlayer(this));
    }

    public Player(String name, int points, int playerPlace, int darkMatter, Universe universe) {
        this(name, universe);
//        Condition.check().positive(points, highscore, darkMatter);
        this.points.set(points);
        this.playerPlace.set(playerPlace);
        this.darkMatter.set(darkMatter);
    }

    public Universe getUniverse() {
        return universe;
    }

    public void setUniverse(Universe universe) {
        this.universe = universe;
    }

    public int getPoints() {
        return points.get();
    }

    public int getPlayerPlace() {
        return playerPlace.get();
    }

    public int getHighscore() {
        return highscore.get();
    }

    public IntegerProperty highscoreProperty() {
        return highscore;
    }

    public int getDarkMatter() {
        return darkMatter.get();
    }

    public Research getResearch(ResearchField fields) {
        return researches.getValue(fields);
    }

    public ObservableList<Planet> getPlanets() {
        return planets;
    }

    public ObservableList<CelestialBody> getCelestialBodies() {
        ObservableList<CelestialBody> wrapper = FXCollections.observableArrayList();
        final List<Moon> moons = planets.stream().filter(planet -> planet.getMoon() != null).map(Planet::getMoon).collect(Collectors.toList());
        wrapper.addAll(moons);
        Bindings.bindContent(wrapper, planets);

        planets.forEach(planet -> planet.moonProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                wrapper.add(newValue);
            } else {
                wrapper.remove(oldValue);
            }
        }));
        return wrapper;
    }

    public Collection<Research> getResearches() {
        return researches.getValues();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (!getName().equals(player.getName())) return false;
        return getUniverse().equals(player.getUniverse());
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getUniverse().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name=" + name +
                ", points=" + points +
                ", highscore=" + highscore +
                ", playerPlace=" + playerPlace +
                ", darkMatter=" + darkMatter +
                ", researches=" + researches +
                ", planets=" + planets +
                ", universe=" + universe +
                '}';
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public IntegerProperty pointsProperty() {
        return points;
    }

    public IntegerProperty playerPlaceProperty() {
        return playerPlace;
    }

    public IntegerProperty darkMatterProperty() {
        return darkMatter;
    }

    public boolean hasGeologist() {
        return geologist;
    }

    public boolean hasCommander() {
        return commander;
    }

    public boolean hasAdmiral() {
        return admiral;
    }

    public boolean hasEngineer() {
        return engineer;
    }

    public boolean hasTechnocrat() {
        return technocrat;
    }

    public boolean isOnlinePlayer() {
        return isOnlinePlayer;
    }

    public void setOnlinePlayer(boolean onlinePlayer) {
        isOnlinePlayer = onlinePlayer;
    }

    @Override
    public int compareTo(GorgonEntry gorgonEntry) {
        if (gorgonEntry == null) return -1;
        if (gorgonEntry == this) return 0;
        if (gorgonEntry.getClass() != getClass()) return -1;

        Player player = (Player) gorgonEntry;
        int compare = this.getName().compareTo(player.getName());

        if (compare == 0) {
            compare = getUniverse().compareTo(player.getUniverse());
        }
        return compare;


    }

    public Researches getResearch() {
        return null;
    }
}
