package ogamebot.online;

import ogamebot.units.astroObjects.Moon;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.astroObjects.PlanetBuilder;
import ogamebot.units.building.Buildings;
import ogamebot.units.units.Resource;
import ogamebot.units.warfare.Defence;
import ogamebot.units.warfare.Fleet;
import ogamebot.units.warfare.StationaryFleet;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
class OnlinePlanet implements OnlineSupplier<Planet> {

    private final Integer planetId;

    OnlinePlanet(Integer planetId) {
        this.planetId = planetId;
    }

    @Override
    public Void update(AccountAccess updater, Planet planet) throws IOException {
        final String query = updater.getPageQuery(PageParameter.OVERVIEW);

        HttpGet get = new HttpGet(query);
        final Document document = updater.getDocument(query, get);

        if (document == null) {
            return null;
        }

        final Resource resource = getResource(document);
        planet.resourceProperty().set(resource);

        final Buildings container = planet.getBuilding();
        final Fleet fleet = planet.getFleet();
        final Defence defence = planet.getDefence();

        fillContainer(updater, container, fleet, defence);

        final Element planetList = document.getElementById("planetList");
        final Element planetListPosition = planetList.getElementsByClass("planet-" + planetId).get(0);
        final Moon moon = planet.getMoon();

        if (moon != null) {
            updateMoon(planetListPosition, updater, moon);
        } else {
            final Moon newMoon = getMoon(planetListPosition, updater);
            planet.setMoon(newMoon);
        }
        return null;
    }

    private void fillContainer(AccountAccess updater, Buildings buildings, Fleet fleet, Defence defence) throws IOException {
        OnlineUpgradeable.fillBuildings(updater, buildings, planetId);
        OnlineUpgradeable.fillDefence(updater, defence, planetId);
        OnlineUpgradeable.fillFleet(updater, fleet, planetId);
    }

    @Override
    public Planet create(AccountAccess updater) throws IOException {
        final Buildings buildings = Buildings.planetBuildings();
        final StationaryFleet fleet = new StationaryFleet();
        final Defence defence = new Defence();

        final Document document = updater.getDocument(getPlanetQuery(updater, PageParameter.OVERVIEW));
        final Element planetList = document.getElementById("planetList");
        final Element planetListPosition = planetList.getElementById("planet-" + planetId);

        final Element planetElement = planetListPosition.getElementsByClass("planetlink").get(0);
        final String title = planetElement.attr("title");

        CelestialBodyProperties properties = CelestialBodyProperties.readTitle(title);

        final Moon moon = getMoon(planetListPosition, updater);

        if (properties != null) {
            fillContainer(updater, buildings, fleet, defence);

            return new PlanetBuilder(properties.getName())
                    .setMoon(moon)
                    .setDebrisField(null)
                    .setFields(properties.getAvailableFields())
                    .setMaxT(properties.getMaxT())
                    .setPosition(properties.getPosition())
                    .setPlayer(null)
                    .setBuildings(buildings.getValues())
                    .setDefences(defence.getValues())
                    .setShips(fleet.getValues())
                    .createPlanet();
        } else {
            return null;
        }


    }

    private Moon getMoon(Element planetListPosition, AccountAccess updater) throws IOException {
        int moonId = getMoonId(planetListPosition);
        return moonId > 0 ? new OnlineMoon(moonId, planetListPosition).create(updater) : null;
    }

    private void updateMoon(Element planetListPosition, AccountAccess updater, Moon moon) throws IOException {
        final int moonId = getMoonId(planetListPosition);
        if (moonId > 0) {
            new OnlineMoon(moonId, planetListPosition).update(updater, moon);
        }
    }

    private int getMoonId(Element planetListPosition) {
        final Elements moonElements = planetListPosition.getElementsByClass("moonlink");

        int moonId = -1;

        if (!moonElements.isEmpty()) {
            final Element moonElement = moonElements.get(0);
            final String url = moonElement.absUrl("href");
            final Matcher matcher = Pattern.compile("cp=(\\d*)").matcher(url);

            if (matcher.find()) {
                final String moonIdString = matcher.group(1);

                if (moonIdString.chars().allMatch(Character::isDigit)) {
                    moonId = Integer.parseInt(moonIdString);
                }
            }
        }
        return moonId;
    }

    private String getPlanetQuery(AccountAccess updater, PageParameter parameter) {
        return updater.getPageQuery(parameter) + "&cp=" + planetId;
    }

    private Resource getResource(Document document) {
        final int metal = getValue(document, "resources_metal");
        final int crystal = getValue(document, "resources_crystal");
        final int deuterium = getValue(document, "resources_deuterium");
        return new Resource(metal, crystal, deuterium);
    }

    private int getValue(Document document, String id) {
        final Element resources_crystal = document.getElementById(id);
        final String text = resources_crystal.text();
        return Integer.parseInt(text);
    }
}
