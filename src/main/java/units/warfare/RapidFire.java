package units.warfare;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class RapidFire {
    private Map<ShipType, Integer> positive = new HashMap<>();
    private Map<ShipType, Integer> negative = new HashMap<>();

    public int givesVs(ShipType type) {
        return get(positive, type);
    }

    public int takesFrom(ShipType type) {
        return get(negative, type);
    }

    private int get(Map<ShipType, Integer> map, ShipType type) {
        Integer integer = map.get(type);
        return integer == null ? 0 : integer;
    }
}
