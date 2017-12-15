package ogamebot.calc;

import ogamebot.comp.Player;
import ogamebot.units.astroObjects.CelestialBody;
import ogamebot.units.astroObjects.Moon;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.building.Building;
import ogamebot.units.building.BuildingType;
import ogamebot.units.research.ResearchField;

import java.util.function.Supplier;

/**
 *
 */
public class ProductionCalc {

    /**
     * Calculates the production of an eligible building/mine
     * in resource per hour.
     *
     * @param body  astronomical body to calculate the production from
     * @param level level of the mine
     * @param type  buildingType, only METALMINE, CRYSTALMINE or DEUTSYNTH are allowed
     * @return production of an planet, or zero if it is a moon
     * @throws IllegalArgumentException if the type is not a mine or the body
     *                                  is neither a moon or a planet or the level is negative
     */
    public static double getProduction(CelestialBody body, int level, BuildingType type) {
        if (level < 0) {
            throw new IllegalArgumentException();
        }

        if (body instanceof Moon) {
            return 0;
        } else if (body instanceof Planet) {
            if (type == BuildingType.METALMINE) {
                return metProduction((Planet) body, level);
            } else if (type == BuildingType.CRYSTALMINE) {
                return crysProduction((Planet) body, level);
            } else if (type == BuildingType.DEUTSYNTH) {
                return deutProduction((Planet) body, level);
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static double metProduction(Planet planet, int level) {
        return production(planet, BuildingType.METALMINE, level, planet::getMetalBooster, 30, 30, 1);
    }

    private static double crysProduction(Planet planet, int level) {
        return production(planet, BuildingType.CRYSTALMINE, level, planet::getCrystalBooster, 15, 10, 0.66);
    }

    private static double deutProduction(Planet planet, int level) {
        final Building building = planet.getBuilding(BuildingType.DEUTSYNTH);

        double product = 10 * level;
        final double expo = Math.pow(1.1, level);
        product *= expo;
        product *= building.getOutput();

        double tempInfluence = 0.004 * planet.getMaxT();
        tempInfluence = 1.44 - tempInfluence;
        product *= tempInfluence;

        final Player player = planet.getPlayer();

        if (player.hasGeologist()) {
            product *= 1.1;
        }
        product *= planet.getDeutBooster();
        product = Math.floor(product);

        final int researchLvl = player.getResearch(ResearchField.PLASMA_TECH).getCounter();
        double researchBoost = researchLvl * 0.33;
        researchBoost += 100;
        researchBoost /= 100;

        product *= researchBoost;
        final double universeSpeed = player.getUniverse().getEconomySpeed();
        return product * universeSpeed;
    }

    private static double production(Planet planet, BuildingType building, int level, Supplier<Double> boosterSuppl, int baseProd, int baseProdFactor, double plasmaBonus) {
        final Building mine = planet.getBuilding(building);

        double product = baseProdFactor * level;
        final double expo = Math.pow(1.1, level);
        product *= expo;
        product *= mine.getOutput();

        final Player player = planet.getPlayer();

        if (player.hasGeologist()) {
            product *= 1.1;
        }

        product *= boosterSuppl.get();
        product += baseProd;
        product = Math.floor(product);

        final int researchLvl = player.getResearch(ResearchField.PLASMA_TECH).getCounter();
        double researchBoost = researchLvl * plasmaBonus;
        researchBoost += 100;
        researchBoost /= 100;

        product *= researchBoost;
        final double universeSpeed = player.getUniverse().getEconomySpeed();
        return product * universeSpeed;
    }

}
