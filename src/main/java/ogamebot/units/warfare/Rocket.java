package ogamebot.units.warfare;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import ogamebot.comp.Cost;
import ogamebot.comp.UpgradeAble;
import ogamebot.units.UnitType;

/**
 *
 */
public class Rocket extends MobileWarfareUnit {
    private final Rockets rockets;
    private final ReadOnlyStringProperty name;

    Rocket(int structurePoints, int shieldStrength, int attackPower, Rockets rockets) {
        super(RapidFire.NONE, structurePoints, shieldStrength, attackPower, 0, 0, 0);
        this.rockets = rockets;
        name = new SimpleStringProperty(rockets.getName());
    }

    @Override
    public UnitType<?> getType() {
        return rockets;
    }

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public ReadOnlyStringProperty nameProperty() {
        return name;
    }

    @Override
    public int compareTo(UpgradeAble o) {
        return 0;
    }

    @Override
    public Cost getCost() {
        return rockets.getCost(getCounter());
    }
}
