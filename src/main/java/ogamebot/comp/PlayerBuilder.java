package ogamebot.comp;

import ogamebot.units.astroObjects.Planet;
import ogamebot.units.research.Research;

import java.util.Collection;

public class PlayerBuilder {
    private String name;
    private Universe universe;
    private int points = 0;
    private int highscore = 0;
    private int playerPlace = 0;
    private int darkMatter = 0;
    private boolean admiral = false;
    private boolean engineer = false;
    private boolean commander = false;
    private boolean technocrat = false;
    private boolean geologist = false;
    private Collection<Research> research;
    private Collection<Planet> planets;
    private boolean isOnlinePlayer = false;

    public PlayerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PlayerBuilder setUniverse(Universe universe) {
        this.universe = universe;
        return this;
    }

    public PlayerBuilder setPoints(int points) {
        this.points = points;
        return this;
    }

    public PlayerBuilder setHighscore(int highscore) {
        this.highscore = highscore;
        return this;
    }

    public PlayerBuilder setDarkMatter(int darkMatter) {
        this.darkMatter = darkMatter;
        return this;
    }

    public PlayerBuilder setAdmiral(boolean admiral) {
        this.admiral = admiral;
        return this;
    }

    public PlayerBuilder setCommander(boolean commander) {
        this.commander = commander;
        return this;
    }

    public PlayerBuilder setTechnocrat(boolean technocrat) {
        this.technocrat = technocrat;
        return this;
    }

    public PlayerBuilder setGeologist(boolean geologist) {
        this.geologist = geologist;
        return this;
    }

    public PlayerBuilder setEngineer(boolean engineer) {
        this.engineer = engineer;
        return this;
    }

    public PlayerBuilder setPlayerPlace(int playerPlace) {
        this.playerPlace = playerPlace;
        return this;
    }

    public PlayerBuilder setResearch(Collection<Research> research) {
        this.research = research;
        return this;
    }

    public PlayerBuilder setPlanets(Collection<Planet> planets) {
        this.planets = planets;
        return this;
    }

    public PlayerBuilder setIsOnlinePlayer(boolean isOnlinePlayer) {
        this.isOnlinePlayer = isOnlinePlayer;
        return this;
    }

    public Player createPlayer() {
        return new Player(name, points, highscore, darkMatter, geologist, admiral, engineer, technocrat, commander, research, planets, universe, isOnlinePlayer);
    }
}