package ogamebot.comp;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.research.Research;
import ogamebot.units.research.ResearchFields;
import tools.Condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Player implements Comparable<Player>, GameEntity {
    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty points = new SimpleIntegerProperty();
    private IntegerProperty highscore = new SimpleIntegerProperty();
    private IntegerProperty darkMatter = new SimpleIntegerProperty();

    private Map<ResearchFields, Research> research = new HashMap<>();
    private ObservableList<Planet> planets = FXCollections.observableArrayList();
    private List<Production> productions = new ArrayList<>();
    private transient Universe universe;

    public Player(String name, Universe universe) {
        Condition.check().nonNull(universe, name).notEmpty(name);
        this.universe = universe;
        this.name.setValue(name);

        planets.addListener((ListChangeListener<? super Planet>) observable -> {
            if (observable.next()) {
                observable.getAddedSubList().forEach(planet -> productions.add(new Production(planet)));
            }
        });
    }

    public Player(String name, int points, int highscore, int darkMatter, Universe universe) {
        this(name, universe);
        Condition.check().positive(points, highscore, darkMatter).nonNull(research);
        this.points.set(points);
        this.highscore.set(highscore);
        this.darkMatter.set(darkMatter);
    }

    @Override
    public int compareTo(Player o) {
        return 0;
    }

    public Universe getUniverse() {
        return universe;
    }

    public String getName() {
        return name.get();
    }

    public int getPoints() {
        return points.get();
    }

    public int getHighscore() {
        return highscore.get();
    }

    public int getDarkMatter() {
        return darkMatter.get();
    }

    public Research getResearch(ResearchFields fields) {
        return research.computeIfAbsent(fields, Research::new);
    }

    public ObservableList<Planet> getPlanets() {
        return planets;
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
                ", universe=" + universe +
                '}';
    }

    @Override
    public String getText() {
        return name.get();
    }

    @Override
    public List<GameEntity> getChildren() {
        return new ArrayList<>();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public IntegerProperty pointsProperty() {
        return points;
    }

    public IntegerProperty highscoreProperty() {
        return highscore;
    }

    public IntegerProperty darkMatterProperty() {
        return darkMatter;
    }

    public boolean hasGeologist() {
        return false;
    }

    public boolean hasCommander() {
        return false;
    }

    public boolean hasAdmiral() {
        return false;
    }

    public boolean hasEngineer() {
        return false;
    }

    public boolean hasTechnocrat() {
        return false;
    }
}
