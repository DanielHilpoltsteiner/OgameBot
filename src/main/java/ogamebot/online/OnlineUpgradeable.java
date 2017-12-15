package ogamebot.online;

import ogamebot.comp.UpgradeAble;
import ogamebot.units.UnitContainer;
import ogamebot.units.UnitType;
import ogamebot.units.building.Buildings;
import ogamebot.units.research.Researches;
import ogamebot.units.warfare.Defence;
import ogamebot.units.warfare.Fleet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 */
class OnlineUpgradeable {

    static void fillBuildings(AccountAccess updater, Buildings buildings, int planetId) throws IOException {
        fill(updater, buildings, getPlanetQuery(updater, PageParameter.SUPPLY, planetId));
        fill(updater, buildings, getPlanetQuery(updater, PageParameter.FACILITIES, planetId));
    }

    static void fillFleet(AccountAccess updater, Fleet fleet, int planetId) throws IOException {
        fill(updater, fleet, getPlanetQuery(updater, PageParameter.SHIPYARD, planetId));

    }

    static void fillDefence(AccountAccess updater, Defence defence, int planetId) throws IOException {
        fill(updater, defence, getPlanetQuery(updater, PageParameter.DEFENCE, planetId));

    }

    static void fillResearch(AccountAccess updater, Researches researches) throws IOException {
        fill(updater, researches, updater.getPageQuery(PageParameter.RESEARCH));

    }

    private static <E extends UpgradeAble, R extends UnitType<E>> void fill(AccountAccess updater, UnitContainer<E, R> container, String query) throws IOException {
        final Document document = updater.getDocument(query);
        fillContainer(document, container);
    }

    private static String getPlanetQuery(AccountAccess updater, PageParameter parameter, int planetId) {
        return updater.getPageQuery(parameter) + "&cp=" + planetId;
    }

    private static <E extends UpgradeAble, R extends UnitType<E>> void fillContainer(Document document, UnitContainer<E, R> container) {
        final Map<String, R> nameTypeMap = container.getTypes().stream().collect(Collectors.toMap(UnitType::getName, Function.identity()));

        final Map<String, Integer> nameLevelMap = getNameLevelMap(document);
        nameLevelMap.forEach((name, counter) -> {
            final R type = nameTypeMap.get(name);
            if (type != null) {
                final E value = container.getValue(type);
                value.counterProperty().set(counter);
            }
        });
    }

    private static Map<String, Integer> getNameLevelMap(Document document) {

        final Elements elements = document.select("span.level");
        final Map<String, Integer> nameLevelMap = new HashMap<>();

        for (Element element : elements) {
            if (element.children().size() == 1) {
                final Element nameElement = element.children().get(0);
                nameElement.remove();
                final String name = nameElement.text();

                final String levelString = element.text();

                if (!levelString.isEmpty()) {
                    int level;
                    try {
                        level = NumberFormat.getInstance().parse(levelString).intValue();
                    } catch (ParseException e) {
                        level = 0;
                    }
                    nameLevelMap.put(name, level);
                }
            }
        }
        return nameLevelMap;
    }
}
