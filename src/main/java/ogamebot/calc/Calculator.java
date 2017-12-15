package ogamebot.calc;

import ogamebot.comp.Cost;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

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
}
