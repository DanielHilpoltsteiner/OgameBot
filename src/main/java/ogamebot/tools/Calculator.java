package ogamebot.tools;

import ogamebot.comp.Cost;
import ogamebot.comp.Player;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.building.Building;
import ogamebot.units.building.PlanetBuilding;
import ogamebot.units.research.ResearchFields;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.function.Supplier;

/**
 *
 */
public abstract class Calculator {

    public static BigDecimal simpleExp(double base, int exp) {
        return BigDecimal.valueOf(base).pow(exp);
    }

    public static Cost simpleCost(int metFactor, int crysFactor, int deutFactor, double base, int level) {
        BigInteger metal = simpleExp(metFactor, base, level);
        BigInteger crystal = simpleExp(crysFactor, base, level);
        BigInteger deut = simpleExp(deutFactor, base, level);

        return new Cost(metal, crystal, deut);
    }

    public static BigInteger simpleExp(int factor, double base, int level) {
        return factor == 0 ? BigInteger.ZERO : getBigInteger(factor, base, level).toBigInteger();
    }

    public static BigInteger simpleExpUpFloor(int factor, double base, int level) {
        return factor == 0 ? BigInteger.ZERO : getBigInteger(factor, base, level).setScale(0, RoundingMode.CEILING).toBigInteger();
    }

    private static BigDecimal getBigInteger(int factor, double base, int level) {
        return level == 0 ?
                BigDecimal.valueOf(factor).multiply(BigDecimal.valueOf(base))
                :
                BigDecimal.valueOf(factor).multiply(BigDecimal.valueOf(base).pow(level));
    }

    public static Cost simpleMassCost(int metalCost, int crystalCost, int deutCost, int number) {
        final BigInteger metal = BigInteger.valueOf(metalCost).multiply(BigInteger.valueOf(number));
        final BigInteger crystal = BigInteger.valueOf(crystalCost).multiply(BigInteger.valueOf(number));
        final BigInteger deut = BigInteger.valueOf(deutCost).multiply(BigInteger.valueOf(number));

        return new Cost(metal, crystal, deut);
    }

    public static double production(Planet planet, PlanetBuilding building, int level, Supplier<Double> boosterSuppl, int baseProd, int baseProdFactor, double plasmaBonus) {
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

        final int researchLvl = player.getResearch(ResearchFields.PLASMA_TECH).getCounter();
        double researchBoost = researchLvl * plasmaBonus;
        researchBoost += 100;
        researchBoost /= 100;

        product *= researchBoost;
        final double universeSpeed = player.getUniverse().getBuildSpeed();
        return product * universeSpeed;
    }
}
