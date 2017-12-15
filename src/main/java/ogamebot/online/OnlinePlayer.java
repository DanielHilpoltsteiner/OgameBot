package ogamebot.online;

import ogamebot.comp.Player;
import ogamebot.comp.PlayerBuilder;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.research.Researches;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 */
class OnlinePlayer implements OnlineSupplier<Player> {
    private final Document document;

    OnlinePlayer(Document document) {
        this.document = document;
    }

    @Override
    public Void update(AccountAccess updater, Player player) throws IOException {
        final Map<String, Planet> planetMap = player.getPlanets().stream().collect(Collectors.toMap(Planet::getName, Function.identity()));
        final Map<Integer, String> idNameMap = getIdNameMap();

        for (Map.Entry<Integer, String> entry : idNameMap.entrySet()) {
            final String name = entry.getValue();
            if (planetMap.containsKey(name)) {
                new OnlinePlanet(entry.getKey()).update(updater, planetMap.get(name));
            } else {
                final Planet planet = new OnlinePlanet(entry.getKey()).create(updater);
                player.getPlanets().add(planet);
            }
            planetMap.remove(name);
        }


        final String playerName = getPlayerName();

        if (!player.getName().equals(playerName)) {
            //todo signal mismatched playerName to the user
        }

        final Researches research = player.getResearch();
        OnlineUpgradeable.fillResearch(updater, research);

        final PlayerStats playerStats = getPlayerStats(updater);

        if (playerStats != null) {
            player.playerPlaceProperty().set(playerStats.getPosition());
            player.pointsProperty().set(playerStats.getPoints());
            player.darkMatterProperty().set(getDarkMatter());
            player.highscoreProperty().set(playerStats.highScore);
        }

        return null;
    }

    @Override
    public Player create(AccountAccess updater) throws IOException {
        List<Planet> planets = new ArrayList<>();

        for (Integer planetId : getIdNameMap().keySet()) {
            final Planet planet = new OnlinePlanet(planetId).create(updater);

            if (planet != null) {
                planets.add(planet);
            }
        }

        final Researches researches = new Researches();
        OnlineUpgradeable.fillResearch(updater, researches);

        final PlayerStats playerStats = getPlayerStats(updater);
        if (playerStats != null) {
            final int darkMatter = getDarkMatter();

            return new PlayerBuilder()
                    .setName(getPlayerName())
                    .setUniverse(updater.getUniverse())
                    .setPlanets(planets)
                    .setResearch(researches.getValues())
                    .setDarkMatter(darkMatter)
                    .setPoints(playerStats.getPoints())
                    .setHighscore(0)
                    .setPlayerPlace(playerStats.getPosition())
                    .setGeologist(false)
                    .setCommander(false)
                    .setTechnocrat(false)
                    .setEngineer(false)
                    .setAdmiral(false)
                    .setIsOnlinePlayer(true)
                    .createPlayer();

        } else {
            return null;
        }
    }


    private PlayerStats getPlayerStats(AccountAccess updater) throws IOException {
        final Document document = updater.getDocument(PageParameter.HIGHSCORE);
        final Elements playerRank = document.select(".myrank");

        if (!playerRank.isEmpty()) {
            final Element highscoreRank = playerRank.get(0);

            final Element positionElement = highscoreRank.getElementsByClass("position").get(0);
            final Element pointsElement = highscoreRank.getElementsByClass("score").get(0);
            final Element honorElement = highscoreRank.select(".honorscore [title]").get(0);

            int position = parse(positionElement.text());
            int points = parse(pointsElement.text());
            int honor = parse(honorElement.text());

            final Elements honorranks = highscoreRank.getElementsByClass("honorrank");
            String honorRank = null;
            if (!honorranks.isEmpty()) {
                final Element honorRankElement = honorranks.get(0);
                honorRank = honorRankElement.text();
            }
            final Element playerNameElement = highscoreRank.getElementsByClass("playername").get(0);

            return new PlayerStats(position, points, honor, playerNameElement.text(), honorRank);
        }
        return null;
    }

    private int parse(String text) {
        try {
            return NumberFormat.getInstance().parse(text).intValue();
        } catch (ParseException ignored) {
        }
        return 0;
    }

    private int getPoints() {
        return getScorePointsValue(1);
    }

    private int getPlayerPlace() {
        return getScorePointsValue(3);
    }

    private int getHighScore() {
        return getScorePointsValue(5);
    }

    private int getScorePointsValue(int group) {
        final Matcher matcher = getHighScoreMatcher();

        if (matcher.find()) {
            final String playerPlace = matcher.group(group);
            return getInt(playerPlace);
        }
        return -1;
    }

    private int getInt(String s) {
        if (s.isEmpty()) {
            return 0;
        }
        try {
            return NumberFormat.getInstance().parse(s).intValue();
        } catch (ParseException e) {
            return -1;
        }
    }

    private Matcher getHighScoreMatcher() {
        final Element scoreContentField = document.getElementById("scoreContentField");
        final String text = scoreContentField.text();
        Pattern pattern = Pattern.compile("(\\d{1,3}(\\.\\d{1,3})*) \\(Platz (\\d{1,3}(\\.\\d{1,3})*) von (\\d{1,3}(\\.\\d{1,3})*)\\)");
        return pattern.matcher(text);
    }


    private int getHonor() {
        final Element honorContentField = document.getElementById("honorContentField");
        return Integer.parseInt(honorContentField.text());
    }

    private int getDarkMatter() {
        final Element resources_darkmatter = document.getElementById("resources_darkmatter");
        return getInt(resources_darkmatter.text());
    }

    private Map<Integer, String> getIdNameMap() {
        final Element planetList = document.getElementById("planetList");
        final Pattern planetPattern = Pattern.compile("planet-(\\d*)");

        Map<Integer, String> idNameMap = new HashMap<>();

        for (Element element : planetList.children()) {
            final String id = element.id();
            Matcher matcher = planetPattern.matcher(id);

            if (matcher.find()) {
                final String planetId = matcher.group(1);

                if (planetId != null && !planetId.isEmpty()) {
                    final String planetName = element.getElementsByClass("planet-name").get(0).text();
                    idNameMap.put(Integer.parseInt(planetId), planetName);
                }
            }
        }
        return idNameMap;
    }

    private String getPlayerName() {
        final Element nameElement = document.getElementById("playerName");
        final Elements span = nameElement.getElementsByTag("span");
        return span.get(0).text();
    }
}
