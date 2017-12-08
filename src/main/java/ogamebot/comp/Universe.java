package ogamebot.comp;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.*;

/**
 *
 */
public class Universe implements GameEntity {
    private double defTf;
    private double fleetTf;
    private double flightSpeed;
    private double economySpeed;

    private boolean donut;
    private boolean rapidFire;
    private int bonusFields;

    private Set<Player> players = new TreeSet<>(Comparator.comparing(Player::getName));
    private StringProperty name = new SimpleStringProperty();

    public Universe() {

    }

    public Universe(double defTf, double fleetTf, double flightSpeed, double buildSpeed, boolean donut, boolean rapidFire, int bonusFields, String name) {
        this.defTf = defTf;
        this.fleetTf = fleetTf;
        this.flightSpeed = flightSpeed;
        this.economySpeed = buildSpeed;
        this.donut = donut;
        this.rapidFire = rapidFire;
        this.bonusFields = bonusFields;
        this.name.set(name);
    }

    public void addPlayer(Player player) {
        Objects.requireNonNull(player);
        players.add(player);
    }

    public void deletePlayer(Player player) {
        Objects.requireNonNull(player);
        players.remove(player);
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

    public Set<Player> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return "Universe{" +
                "name=" + name +
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
    public String getText() {
        return name.get();
    }

    @Override
    public List<GameEntity> getChildren() {
        return new ArrayList<>(players);
    }

    public StringProperty nameProperty() {
        return name;
    }
}
