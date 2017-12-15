package ogamebot.units.warfare;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class RapidFire {
    public static RapidFire NONE = new RapidFire();

    private final Map<ShipType, Integer> takes;
    private final Map<WarfareType, Integer> deals;

    public RapidFire() {
        deals = new HashMap<>();
        takes = new HashMap<>();
    }

    public RapidFire(Map<ShipType, Integer> takes, Map<WarfareType, Integer> deals) {
        if (takes == null) {
            this.takes = new HashMap<>();
        } else {
            this.takes = takes;
        }
        if (deals == null) {
            this.deals = new HashMap<>();
        } else {
            this.deals = deals;
        }
    }

    public Set<WarfareType> dealsTo() {
        return deals.keySet();
    }

    public Set<ShipType> takesFrom() {
        return takes.keySet();
    }

    public int givesVs(WarfareType type) {
        Integer integer = deals.get(type);
        return integer == null ? 0 : integer;
    }

    public int takesFrom(ShipType type) {
        return get(takes, type);
    }

    private int get(Map<ShipType, Integer> map, ShipType type) {
        Integer integer = map.get(type);
        return integer == null ? 0 : integer;
    }
}
