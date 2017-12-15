package ogamebot.online;

import ogamebot.comp.Position;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 */
public class CelestialBodyProperties {
    private Position position = null;
    private int diameter = -1;
    private int used = -1;
    private int available = -1;
    private int maxT = -1;
    private String name = null;

    private CelestialBodyProperties() {

    }

    public static CelestialBodyProperties readTitle(String title) {
        final CelestialBodyProperties properties = new CelestialBodyProperties();

        if (!title.isEmpty()) {
            final Document fragment = Jsoup.parseBodyFragment(title);
            fragment.select("a").remove();
            final Element body = fragment.body();
            final List<Element> textElements = body.children().stream().filter(Element::hasText).collect(Collectors.toList());

            if (!textElements.isEmpty()) {
                final String text = textElements.get(0).text();

                setNamePosition(text, properties);

                setBodyParameter(body, properties);
            }
            return properties;
        }
        return null;
    }

    private static void setBodyParameter(Element body, CelestialBodyProperties properties) {
        final List<TextNode> textNodes = body.textNodes();
        final Pattern sizePattern = Pattern.compile("(\\d{1,3}(\\.\\d{3,})*).?km.?\\((\\d{1,3})/(\\d{2,3})\\)");
        final Pattern temperaturePattern = Pattern.compile("-?\\d{1,2}\\s?°C\\sbis\\s(-?\\d{1,2})");

        for (TextNode textNode : textNodes) {
            final Matcher sizeMatcher = sizePattern.matcher(textNode.text());
            final Matcher tempMatcher = temperaturePattern.matcher(textNode.text());

            if (sizeMatcher.find()) {
                final String diameterString = sizeMatcher.group(1);
                final String usedFieldsString = sizeMatcher.group(3);
                final String availableFieldsString = sizeMatcher.group(4);

                try {
                    properties.setDiameter(NumberFormat.getInstance().parse(diameterString).intValue());
                    properties.setUsedFields(Integer.parseInt(usedFieldsString));
                    properties.setAvailableFields(Integer.parseInt(availableFieldsString));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (tempMatcher.find()) {
                final String maxTempString = tempMatcher.group(1);
                properties.setMaxT(Integer.parseInt(maxTempString));
            }
        }
    }

    private static void setNamePosition(String text, CelestialBodyProperties properties) {
        if (!text.isEmpty()) {
            final String[] strings = text.split("\\s");

            if (strings.length >= 2) {
                properties.setName(strings[0]);
                final String planetPositionString = strings[1];
                properties.setPosition(getPosition(planetPositionString));
            }
        }
    }

    private static Position getPosition(String planetPositionString) {
        Position planetPosition = null;
        final Matcher positionMatcher = Pattern.compile("\\[(\\d):(\\d{1,3}):(\\d{1,2})\\]").matcher(planetPositionString);

        if (positionMatcher.find()) {
            final String galaxyString = positionMatcher.group(1);
            final String systemString = positionMatcher.group(2);
            final String positionString = positionMatcher.group(3);

            final int galaxy = Integer.parseInt(galaxyString);
            final int system = Integer.parseInt(systemString);
            final int position = Integer.parseInt(positionString);
            planetPosition = new Position(galaxy, system, position);
        }
        return planetPosition;
    }

    public int getMaxT() {
        return maxT;
    }

    private void setMaxT(int maxT) {
        this.maxT = maxT;
    }

    public Position getPosition() {
        return position;
    }

    private void setPosition(Position position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public int getDiameter() {
        return diameter;
    }

    private void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    public int getUsedFields() {
        return used;
    }

    private void setUsedFields(int usedFields) {
        this.used = usedFields;
    }

    public int getAvailableFields() {
        return available;
    }

    private void setAvailableFields(int availableFields) {
        this.available = availableFields;
    }
}
