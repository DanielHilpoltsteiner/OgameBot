package ogamebot.tools;

import ogamebot.comp.Cost;
import ogamebot.units.UnitType;
import ogamebot.units.astroObjects.CelestialBody;
import ogamebot.units.astroObjects.Moon;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.building.Building;
import ogamebot.units.building.BuildingType;
import ogamebot.units.building.MoonBuilding;
import ogamebot.units.building.PlanetBuilding;
import ogamebot.units.research.ResearchType;
import ogamebot.units.warfare.DefenseMachinery;
import ogamebot.units.warfare.ShipTypes;
import tools.Condition;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Duration;

/**
 *
 */
public class DurationCalc {
    private final CelestialBody body;
    private final UnitType<?> type;
    private int precision = 5;

    public DurationCalc(CelestialBody celestialBody, UnitType<?> unitType) {
        Condition.check().nonNull(celestialBody, unitType);
        this.body = celestialBody;
        this.type = unitType;
    }

    public DurationCalc(CelestialBody celestialBody, UnitType<?> unitType, int precision) {
        this(celestialBody, unitType);
        this.precision = precision;
    }

    public static Duration calculateDuration(UnitType<?> type, int counter, CelestialBody body) {
        if (type instanceof BuildingType) {
            int machLvl = 0;
            int naniLevel = 0;

            if (body instanceof Planet) {
                Building machFactory = body.getBuilding(PlanetBuilding.ROBOFACTORY);
                Building nani = body.getBuilding(PlanetBuilding.NANOFACTORY);

                machLvl = machFactory.getCounter();
                naniLevel = nani.getCounter();
            } else if (body instanceof Moon) {
                machLvl = body.getBuilding(MoonBuilding.MACHINEFACTORY).getCounter();
            }


            return getBuildingDuration(machLvl, naniLevel, body, 5, type, counter);
        } else if (type instanceof DefenseMachinery || type instanceof ShipTypes) {
            int nanoLvl = 0;
            final int shipYardLvl;

            if (body instanceof Moon) {
                final Building shipYard = body.getBuilding(MoonBuilding.SPACESHIPSHIPYARD);
                shipYardLvl = shipYard.getCounter();
            } else {
                final Building shipYard = body.getBuilding(PlanetBuilding.SPACESHIPSHIPYARD);
                final Building nanoFactory = body.getBuilding(PlanetBuilding.NANOFACTORY);

                shipYardLvl = shipYard.getCounter();
                nanoLvl = nanoFactory.getCounter();
            }

            return getMachineDuration(shipYardLvl, nanoLvl, counter);
        } else if (type instanceof ResearchType) {
            final Building researchLab = body.getBuilding(PlanetBuilding.RESEARCHLAB);
            final int labLevel = researchLab.getCounter();

            return getResearchDuration(labLevel, counter);
        }
        return Duration.ofMillis(0);
    }

    private static Duration getResearchDuration(int labLevel, int counter) {
        return Duration.ofHours(0);
    }

    private static Duration getMachineDuration(int shipYardLvl, int nanoLvl, int counter) {
        return Duration.ofHours(0);
    }

    private static Duration getBuildingDuration(int machLvl, int naniLevel, CelestialBody body, int precision, UnitType<?> type, int level) {
        BigDecimal value = getBuildingValue(type, precision, level);

        BigDecimal roboRed = BigDecimal.valueOf(machLvl + 1);
        BigDecimal naniRed = BigDecimal.valueOf(2).pow(naniLevel);

        double speed = body.getPlayer().getUniverse().getBuildSpeed();


        final BigDecimal firstDivide = value.divide(roboRed, precision, RoundingMode.CEILING);
        final BigDecimal secondDivide = firstDivide.divide(naniRed, precision, RoundingMode.CEILING);
        BigDecimal millis = secondDivide.divide(BigDecimal.valueOf(speed), precision, RoundingMode.CEILING);

        return Duration.ofSeconds(millis.longValue());
    }

    private static BigDecimal getBuildingValue(UnitType<?> type, int precision, int level) {
        BigInteger cost = getCostSum(type, level);
        final BigDecimal step1 = new BigDecimal(cost).multiply(BigDecimal.valueOf(1.44));
        int step2 = 4 - level + 1;
        int step3 = Math.max(step2, 1);
        return step1.divide(BigDecimal.valueOf(step3), precision, RoundingMode.CEILING);
    }

    private static BigInteger getCostSum(UnitType<?> type, int level) {
        Cost cost = type.getCost(level);

        BigInteger crystal = cost.getCrystal();
        BigInteger metal = cost.getMetal();
        return crystal.add(metal);
    }

    public Duration calc() {
        if (type instanceof BuildingType) {
            int machLvl = 0;
            int naniLevel = 0;

            if (body instanceof Planet) {
                Building machFactory = body.getBuilding(PlanetBuilding.ROBOFACTORY);
                Building nani = body.getBuilding(PlanetBuilding.NANOFACTORY);

                machLvl = machFactory.getCounter();
                naniLevel = nani.getCounter();
            } else if (body instanceof Moon) {
                machLvl = body.getBuilding(MoonBuilding.MACHINEFACTORY).getCounter();
            }

            final int level = body.getBuilding((UnitType<Building>) type).getCounter();

            return getBuildingDuration(machLvl, naniLevel, body, precision, type, level);

        } else if (type instanceof DefenseMachinery || type instanceof ShipTypes) {

            final Building shipYard = body.getBuilding(PlanetBuilding.SPACESHIPSHIPYARD);
            final Building nanoFactory = body.getBuilding(PlanetBuilding.NANOFACTORY);

            final int shipYardLvl = shipYard.getCounter();
            final int nanoLvl = nanoFactory.getCounter();

            return getMachineDuration(shipYardLvl, nanoLvl, 0);

        } else if (type instanceof ResearchType) {

            final Building researchLab = body.getBuilding(PlanetBuilding.RESEARCHLAB);
            final int labLevel = researchLab.getCounter();

            return getResearchDuration(labLevel, 0);
        }
        return Duration.ofMillis(0);
    }
}
