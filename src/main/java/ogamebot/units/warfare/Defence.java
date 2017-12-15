package ogamebot.units.warfare;

import ogamebot.units.Container;
import ogamebot.units.astroObjects.CelestialBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class Defence extends Container<DefenceUnit, DefenceType> {
    private CelestialBody owner;

    public Defence() {
        for (DefenceType defenceType : DefenceType.values()) {
            put(defenceType, defenceType.create());
        }
    }

    public Defence(CelestialBody owner, Collection<DefenceUnit> defences) {
        List<DefenceType> types = new ArrayList<>(Arrays.asList(DefenceType.values()));

        for (DefenceUnit defence : defences) {
            final DefenceType type = defence.getType();
            put(type, defence);
            types.remove(type);
        }
        types.forEach(type -> put(type, type.create()));
        this.owner = owner;
    }

    public Defence(CelestialBody owner) {
        this();
        this.owner = owner;
    }

    public CelestialBody getOwner() {
        return owner;
    }

    public void setOwner(CelestialBody owner) {
        this.owner = owner;
    }
}
