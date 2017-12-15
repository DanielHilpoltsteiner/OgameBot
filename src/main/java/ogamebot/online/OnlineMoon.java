package ogamebot.online;

import ogamebot.units.astroObjects.Moon;
import ogamebot.units.building.Buildings;
import ogamebot.units.warfare.Defence;
import ogamebot.units.warfare.Fleet;
import ogamebot.units.warfare.StationaryFleet;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 *
 */
public class OnlineMoon implements OnlineSupplier<Moon> {

    private final int moonId;
    private final Element planetListPosition;

    OnlineMoon(int moonId, Element planetListPosition) {
        this.moonId = moonId;
        this.planetListPosition = planetListPosition;
    }


    @Override
    public Void update(AccountAccess updater, Moon moon) throws IOException {
        final Buildings buildings = moon.getBuilding();
        final Fleet fleet = moon.getFleet();
        final Defence defence = moon.getDefence();

        fillContainer(updater, buildings, fleet, defence);
        return null;
    }

    @Override
    public Moon create(AccountAccess updater) throws IOException {
        final Buildings buildings = Buildings.moonBuildings();
        final Fleet fleet = new StationaryFleet();
        final Defence defence = new Defence();

        fillContainer(updater, buildings, fleet, defence);

        final Elements moonlink = planetListPosition.getElementsByClass("moonlink");
        if (!moonlink.isEmpty()) {
            final Element moonElement = moonlink.get(0);
            final String title = moonElement.attr("title");
            final CelestialBodyProperties properties = CelestialBodyProperties.readTitle(title);

            if (properties != null) {
                return new Moon(properties.getMaxT(), properties.getAvailableFields(), properties.getName());
            }
        }
        return null;
    }

    private void fillContainer(AccountAccess updater, Buildings buildings, Fleet fleet, Defence defence) throws IOException {
        OnlineUpgradeable.fillBuildings(updater, buildings, moonId);
        OnlineUpgradeable.fillDefence(updater, defence, moonId);
        OnlineUpgradeable.fillFleet(updater, fleet, moonId);
    }
}
