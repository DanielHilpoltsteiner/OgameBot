package ogamebot.online;

import com.google.gson.Gson;
import ogamebot.comp.Universe;
import ogamebot.comp.UniverseBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 */
public class OgameServer {

    private General general;
    private JumpGate jumpgate;
    private Speed speed;
    private Size size;
    private Combat combat;
    private Wreckage wreckfield;
    private Alliance alliance;

    public static List<Universe> getServer() {
        final Document document;
        try {
            document = Jsoup.connect("https://de.ogame.gameforge.com").get();
        } catch (IOException e) {
            return new ArrayList<>();
        }
        final Element uni_selection = document.getElementById("uni_selection");
        final Elements server = uni_selection.getElementsByClass("server-row");

        final Pattern pattern = Pattern.compile("'([^']*)'");

        return server.stream().map(element -> parseToUniverse(pattern, element)).collect(Collectors.toList());
    }

    private static Universe parseToUniverse(Pattern pattern, Element element) {
        final String onclick = element.attr("onclick");
        final Matcher matcher = pattern.matcher(onclick);

        String name = "NOT_FOUND";
        boolean firstFound = false;

        while (matcher.find()) {
            if (!firstFound) {
                firstFound = true;
            } else {
                name = matcher.group(1);
                break;
            }
        }
        String data_tooltip = "";
        try {
            data_tooltip = URLDecoder.decode(element.attr("data-tooltip"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final OgameServer ogameServer = new Gson().fromJson(data_tooltip, OgameServer.class);

        return new UniverseBuilder()
                .setName(name)
                .setNameId(ogameServer.general.name)
                .setLanguage(ogameServer.general.language)
                .setExpeditionEnabled(ogameServer.general.expedition == 1)
                .setdM_sign_Up_gift(ogameServer.general.dark_matter_signup_gift)
                .setJumpGate_Enabled(ogameServer.jumpgate.enabled == 1)
                .setJumpGate_basic_duration(ogameServer.jumpgate.basic_duration)
                .setJumpGate_minimum_duration(ogameServer.jumpgate.minimum_duration)
                .setFlightSpeed(ogameServer.speed.fleet)
                .setEconomySpeed(ogameServer.speed.server)
                .setGalaxies_max(ogameServer.size.galaxies_max)
                .setPlanets_max(ogameServer.size.planets_max)
                .setSystems_max(ogameServer.size.systems_max)
                .setBonusFields(ogameServer.size.planet_field_bonus)
                .setDefTf(ogameServer.combat.debris_field_factor_def / 100)
                .setFleetTf(ogameServer.combat.debris_field_factor_ships / 100)
                .setWreckage_Enabled(ogameServer.wreckfield.enabled == 1)
                .setAks_Enabled(ogameServer.alliance.enabled == 1)
                .createUniverse();
    }

    @Override
    public String toString() {
        return "OgameServer{" +
                "general=" + general +
                ", jumpGate=" + jumpgate +
                ", speed=" + speed +
                ", size=" + size +
                ", combat=" + combat +
                ", wreckfield=" + wreckfield +
                ", alliance=" + alliance +
                '}';
    }

    private class General {
        int name;
        String language;
        int noob_protection_factor;
        int noob_protection_advanced;
        int expedition;
        int dark_matter_signup_gift;

        @Override
        public String toString() {
            return "General{" +
                    "name=" + name +
                    ", language='" + language + '\'' +
                    ", noob_protection_factor=" + noob_protection_factor +
                    ", noob_protection_advanced=" + noob_protection_advanced +
                    ", expedition=" + expedition +
                    ", dark_matter_signup_gift=" + dark_matter_signup_gift +
                    '}';
        }
    }

    private class JumpGate {
        int enabled;
        int basic_duration;
        int minimum_duration;

        @Override
        public String toString() {
            return "JumpGate{" +
                    "enabled=" + enabled +
                    ", basic_duration=" + basic_duration +
                    ", minimum_duration=" + minimum_duration +
                    '}';
        }
    }

    private class Speed {
        int server;
        int fleet;


        @Override
        public String toString() {
            return "Speed{" +
                    "server=" + server +
                    ", fleet=" + fleet +
                    '}';
        }
    }

    private class Size {
        int galaxies_max;
        int planets_max;
        int planet_field_bonus;
        int systems_max;

        @Override
        public String toString() {
            return "Size{" +
                    "galaxies_max=" + galaxies_max +
                    ", planets_max=" + planets_max +
                    ", planet_field_bonus=" + planet_field_bonus +
                    ", systems_max=" + systems_max +
                    '}';
        }
    }

    private class Combat {
        double debris_field_factor_ships;
        double debris_field_factor_def;
        int espionage_raids;

        @Override
        public String toString() {
            return "Combat{" +
                    "debris_field_factor_ships=" + debris_field_factor_ships +
                    ", debris_field_factor_def=" + debris_field_factor_def +
                    ", espionage_raids=" + espionage_raids +
                    '}';
        }
    }

    private class Wreckage {
        int enabled;

        @Override
        public String toString() {
            return "Wreckage{" +
                    "enabled=" + enabled +
                    '}';
        }
    }

    private class Alliance {
        int enabled;

        @Override
        public String toString() {
            return "Alliance{" +
                    "enabled=" + enabled +
                    '}';
        }
    }


}
