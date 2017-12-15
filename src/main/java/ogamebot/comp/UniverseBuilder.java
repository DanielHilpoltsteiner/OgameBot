package ogamebot.comp;

import java.util.ArrayList;
import java.util.Collection;

public class UniverseBuilder {
    private double defTf;
    private double fleetTf;
    private double flightSpeed;
    private double buildSpeed;
    private boolean donut;
    private boolean rapidFire;
    private int bonusFields;
    private String name;
    private Collection<Player> players = new ArrayList<>();
    private int nameId = 0;
    private String language = "";
    private boolean expeditionEnabled = false;
    private int dM_sign_up_gift = 0;
    private boolean jumpGate_enabled = false;
    private int jumpGate_basic_duration = 0;
    private int jumpGate_minimum_duration = 0;
    private int galaxies_max = 0;
    private int planets_max = 0;
    private int systems_max = 0;
    private int espionage_raids = 0;
    private boolean wreckage_enabled = false;
    private boolean aks_enabled = false;

    public UniverseBuilder setDefTf(double defTf) {
        this.defTf = defTf;
        return this;
    }

    public UniverseBuilder setFleetTf(double fleetTf) {
        this.fleetTf = fleetTf;
        return this;
    }

    public UniverseBuilder setFlightSpeed(double flightSpeed) {
        this.flightSpeed = flightSpeed;
        return this;
    }

    public UniverseBuilder setEconomySpeed(double buildSpeed) {
        this.buildSpeed = buildSpeed;
        return this;
    }

    public UniverseBuilder setDonut(boolean donut) {
        this.donut = donut;
        return this;
    }

    public UniverseBuilder setRapidFire(boolean rapidFire) {
        this.rapidFire = rapidFire;
        return this;
    }

    public UniverseBuilder setBonusFields(int bonusFields) {
        this.bonusFields = bonusFields;
        return this;
    }

    public UniverseBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UniverseBuilder setNameId(int id) {
        this.nameId = id;
        return this;
    }

    public UniverseBuilder setPlayers(Collection<Player> players) {
        if (players != null) {
            this.players = players;
        }
        return this;
    }

    public UniverseBuilder setLanguage(String language) {
        this.language = language;
        return this;
    }

    public UniverseBuilder setExpeditionEnabled(boolean expeditionEnabled) {
        this.expeditionEnabled = expeditionEnabled;
        return this;
    }

    public UniverseBuilder setdM_sign_Up_gift(int dM_sign_up_gift) {
        this.dM_sign_up_gift = dM_sign_up_gift;
        return this;
    }

    public UniverseBuilder setJumpGate_Enabled(boolean jumpGate_enabled) {
        this.jumpGate_enabled = jumpGate_enabled;
        return this;
    }

    public UniverseBuilder setJumpGate_basic_duration(int jumpGate_basic_duration) {
        this.jumpGate_basic_duration = jumpGate_basic_duration;
        return this;
    }

    public UniverseBuilder setJumpGate_minimum_duration(int jumpGate_minimum_duration) {
        this.jumpGate_minimum_duration = jumpGate_minimum_duration;
        return this;
    }

    public UniverseBuilder setGalaxies_max(int galaxies_max) {
        this.galaxies_max = galaxies_max;
        return this;
    }

    public UniverseBuilder setPlanets_max(int planets_max) {
        this.planets_max = planets_max;
        return this;
    }

    public UniverseBuilder setSystems_max(int systems_max) {
        this.systems_max = systems_max;
        return this;
    }

    public UniverseBuilder setEspionage_raids(int espionage_raids) {
        this.espionage_raids = espionage_raids;
        return this;
    }

    public UniverseBuilder setWreckage_Enabled(boolean wreckage_enabled) {
        this.wreckage_enabled = wreckage_enabled;
        return this;
    }

    public UniverseBuilder setAks_Enabled(boolean aks_enabled) {
        this.aks_enabled = aks_enabled;
        return this;
    }

    public Universe createUniverse() {
        return new Universe(nameId, defTf, fleetTf, flightSpeed, buildSpeed, donut, rapidFire, bonusFields, language, expeditionEnabled, dM_sign_up_gift, jumpGate_enabled, jumpGate_basic_duration, jumpGate_minimum_duration, galaxies_max, planets_max, systems_max, espionage_raids, wreckage_enabled, aks_enabled, players, name);
    }
}