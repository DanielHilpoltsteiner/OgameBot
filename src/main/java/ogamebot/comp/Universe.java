package ogamebot.comp;

import gorgon.external.DataAccess;
import gorgon.external.GorgonEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import ogamebot.data.daos.UniverseDao;
import ogamebot.gui.main.displayTree.TreeAble;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.TreeSet;

/**
 *
 */
@DataAccess(UniverseDao.class)
public class Universe implements TreeAble, GorgonEntry {
    private double defTf;
    private double fleetTf;
    private double flightSpeed;
    private double economySpeed;

    private boolean donut;
    private boolean rapidFire;
    private int bonusFields;

    private int nameId;
    private String language;
    private boolean expeditionEnabled;
    private int dM_sign_Up_gift;
    private boolean jumpGate_Enabled;
    private int jumpGate_basic_duration;
    private int jumpGate_minimum_duration;
    private int galaxies_max;
    private int planets_max;
    private int systems_max;
    private int espionage_raids;
    private boolean wreckage_Enabled;
    private boolean aks_Enabled;


    private ObservableSet<Player> players = FXCollections.observableSet(new TreeSet<>(Comparator.comparing(Player::getName)));
    private StringProperty name = new SimpleStringProperty();

    public Universe(double defTf, double fleetTf, double flightSpeed, double buildSpeed, boolean donut, boolean rapidFire, int bonusFields, String name, Collection<Player> players) {
        this(defTf, fleetTf, flightSpeed, buildSpeed, donut, rapidFire, bonusFields, name);
        this.players.addAll(players);
    }

    public Universe(double defTf, double fleetTf, double flightSpeed, double buildSpeed, boolean donut, boolean rapidFire, int bonusFields, String name) {
        tools.Condition.check().positive(defTf, fleetTf, flightSpeed, bonusFields, buildSpeed).notEmpty(name);
        this.defTf = defTf;
        this.fleetTf = fleetTf;
        this.flightSpeed = flightSpeed;
        this.economySpeed = buildSpeed;
        this.donut = donut;
        this.rapidFire = rapidFire;
        this.bonusFields = bonusFields;
        this.name.set(name);
    }

    Universe(int nameId, double defTf, double fleetTf, double flightSpeed, double economySpeed, boolean donut, boolean rapidFire, int bonusFields, String language, boolean expeditionEnabled, int dM_sign_Up_gift, boolean jumpGate_Enabled, int jumpGate_basic_duration, int jumpGate_minimum_duration, int galaxies_max, int planets_max, int systems_max, int espionage_raids, boolean wreckage_Enabled, boolean aks_Enabled, Collection<Player> players, String name) {
        this.nameId = nameId;
        this.defTf = defTf;
        this.fleetTf = fleetTf;
        this.flightSpeed = flightSpeed;
        this.economySpeed = economySpeed;
        this.donut = donut;
        this.rapidFire = rapidFire;
        this.bonusFields = bonusFields;
        this.language = language;
        this.expeditionEnabled = expeditionEnabled;
        this.dM_sign_Up_gift = dM_sign_Up_gift;
        this.jumpGate_Enabled = jumpGate_Enabled;
        this.jumpGate_basic_duration = jumpGate_basic_duration;
        this.jumpGate_minimum_duration = jumpGate_minimum_duration;
        this.galaxies_max = galaxies_max;
        this.planets_max = planets_max;
        this.systems_max = systems_max;
        this.espionage_raids = espionage_raids;
        this.wreckage_Enabled = wreckage_Enabled;
        this.aks_Enabled = aks_Enabled;
        this.players.addAll(players);
        this.name.set(name);
    }

    public void addPlayer(Player player) {
        Objects.requireNonNull(player);
        if (!players.contains(player)) {
            players.add(player);
        }
    }

    public void removePlayer(Player player) {
        Objects.requireNonNull(player);
        players.remove(player);
    }

    public int getNameId() {
        return nameId;
    }

    public double getDefTf() {
        return defTf;
    }

    public double getFleetTf() {
        return fleetTf;
    }

    public double getFlightSpeed() {
        return flightSpeed;
    }

    public double getEconomySpeed() {
        return economySpeed;
    }

    public boolean isDonut() {
        return donut;
    }

    public boolean isRapidFire() {
        return rapidFire;
    }

    public int getBonusFields() {
        return bonusFields;
    }

    public ObservableSet<Player> getPlayers() {
        return players;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isExpeditionEnabled() {
        return expeditionEnabled;
    }

    public int getdM_sign_Up_gift() {
        return dM_sign_Up_gift;
    }

    public boolean isJumpGate_Enabled() {
        return jumpGate_Enabled;
    }

    public int getJumpGate_basic_duration() {
        return jumpGate_basic_duration;
    }

    public int getJumpGate_minimum_duration() {
        return jumpGate_minimum_duration;
    }

    public int getGalaxies_max() {
        return galaxies_max;
    }

    public int getPlanets_max() {
        return planets_max;
    }

    public int getSystems_max() {
        return systems_max;
    }

    public int getEspionage_raids() {
        return espionage_raids;
    }

    public boolean isWreckage_Enabled() {
        return wreckage_Enabled;
    }

    public boolean isAks_Enabled() {
        return aks_Enabled;
    }

    @Override
    public String toString() {
        return "Universe{" +
                "nameId=" + nameId +
                ", name=" + name.get() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Universe universe = (Universe) o;

        return name.equals(universe.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public int compareTo(GorgonEntry gorgonEntry) {
        if (gorgonEntry == null) return -1;
        if (gorgonEntry == this) return 0;
        if (gorgonEntry.getClass() != getClass()) return -1;

        Universe universe = (Universe) gorgonEntry;
        return universe.getNameId() - getNameId();
    }
}
