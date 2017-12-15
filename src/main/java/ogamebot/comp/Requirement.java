package ogamebot.comp;

import ogamebot.units.RequireAble;
import ogamebot.units.astroObjects.CelestialBody;
import ogamebot.units.building.Building;
import ogamebot.units.building.BuildingType;
import ogamebot.units.research.Research;
import ogamebot.units.research.ResearchField;
import ogamebot.units.research.ResearchType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class Requirement {
    public final static Requirement NONE = new Requirement();
    private final Map<RequireAble, Integer> requirementTable;

    private Requirement() {
        requirementTable = new HashMap<>();
    }

    private Requirement(Map<RequireAble, Integer> requirementTable) {
        this.requirementTable = requirementTable;
    }

    public static Requirement of(RequireAble k, Integer lvl) {
        return new Requirement(Map.of(k, lvl));
    }

    public static Requirement of(RequireAble k, Integer lvl, RequireAble k1, Integer lvl1) {
        return new Requirement(Map.of(k, lvl, k1, lvl1));
    }

    public static Requirement of(RequireAble k, Integer lvl, RequireAble k1, Integer lvl1, RequireAble k2, Integer lvl2) {
        return new Requirement(Map.of(k, lvl, k1, lvl1, k2, lvl2));
    }

    public static Requirement of(RequireAble k, Integer lvl, RequireAble k1, Integer lvl1, RequireAble k2, Integer lvl2, RequireAble k3, Integer lvl3) {
        return new Requirement(Map.of(k, lvl, k1, lvl1, k2, lvl2, k3, lvl3));
    }

    public static Requirement of(RequireAble k, Integer lvl, RequireAble k1, Integer lvl1, RequireAble k2, Integer lvl2, RequireAble k3, Integer lvl3, RequireAble k4, Integer lvl4) {
        return new Requirement(Map.of(k, lvl, k1, lvl1, k2, lvl2, k3, lvl3, k4, lvl4));
    }

    public boolean check(CelestialBody body) {
        return getRequirements().stream().allMatch(requireAble -> checkLvl(body, requireAble));
    }

    private boolean checkLvl(CelestialBody body, RequireAble requireAble) {
        final int minLevel = get(requireAble);
        int currentLevel = 0;

        if (requireAble instanceof BuildingType) {
            final Building building = body.getBuilding((BuildingType) requireAble);
            currentLevel = building.getCounter();

        } else if (requireAble instanceof ResearchType) {
            final Research research = body.getPlayer().getResearch((ResearchField) requireAble);
            currentLevel = research.getCounter();
        }
        return minLevel < currentLevel;
    }

    public int get(RequireAble able) {
        Integer lvl = requirementTable.get(able);
        return lvl != null ? lvl : 0;
    }

    public Set<RequireAble> getRequirements() {
        return requirementTable.keySet();
    }
}
