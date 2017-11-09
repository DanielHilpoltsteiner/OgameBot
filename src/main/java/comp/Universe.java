package comp;

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
    private double buildSpeed;

    private boolean donut;
    private boolean rapidFire;
    private int bonusFields;

    private Set<Player> players = new TreeSet<>(Comparator.comparing(Player::getName));
    private StringProperty name = new SimpleStringProperty();

    public Universe(double defTf, double fleetTf, double flightSpeed, double buildSpeed, boolean donut, boolean rapidFire, int bonusFields, String name) {
        this.defTf = defTf;
        this.fleetTf = fleetTf;
        this.flightSpeed = flightSpeed;
        this.buildSpeed = buildSpeed;
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

    public double getBuildSpeed() {
        return buildSpeed;
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
                "defTf=" + defTf +
                ", fleetTf=" + fleetTf +
                ", flightSpeed=" + flightSpeed +
                ", buildSpeed=" + buildSpeed +
                ", donut=" + donut +
                ", rapidFire=" + rapidFire +
                ", bonusFields=" + bonusFields +
                ", players=" + players +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Universe universe = (Universe) o;

        if (Double.compare(universe.getDefTf(), getDefTf()) != 0) return false;
        if (Double.compare(universe.getFleetTf(), getFleetTf()) != 0) return false;
        if (Double.compare(universe.getFlightSpeed(), getFlightSpeed()) != 0) return false;
        if (Double.compare(universe.getBuildSpeed(), getBuildSpeed()) != 0) return false;
        if (isDonut() != universe.isDonut()) return false;
        if (isRapidFire() != universe.isRapidFire()) return false;
        if (getBonusFields() != universe.getBonusFields()) return false;
        return name.get().equals(universe.name.get());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getDefTf());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getFleetTf());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getFlightSpeed());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getBuildSpeed());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (isDonut() ? 1 : 0);
        result = 31 * result + (isRapidFire() ? 1 : 0);
        result = 31 * result + getBonusFields();
        result = 31 * result + name.get().hashCode();
        return result;
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
