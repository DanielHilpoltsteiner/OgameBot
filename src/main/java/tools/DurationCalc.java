package tools;

import comp.Cost;
import comp.CostAble;
import units.Building.Building;
import units.Building.PlanetBuilding;
import units.Building.facilities.accelerator.MachineFactory;
import units.Building.facilities.accelerator.NanoFactory;
import units.astroObjects.Planet;
import units.research.Research;
import units.warfare.Machine;

import java.time.Duration;

/**
 *
 */
public class DurationCalc {
    private final Planet planet;
    private final CostAble costAble;

    public DurationCalc(Planet planet, CostAble costAble) {
        Condition.check().nonNull(planet, costAble);
        this.planet = planet;
        this.costAble = costAble;
    }

    public Duration calc() {
        if (costAble instanceof Building) {
            MachineFactory machFactory = (MachineFactory) planet.getBuilding(PlanetBuilding.ROBOFACTORY);
            NanoFactory nani = (NanoFactory) planet.getBuilding(PlanetBuilding.NANOFACTORY);

            int machLvl = machFactory.getLevel();
            int naniLevel = nani.getLevel();

            return getBuildingDuration(machLvl, naniLevel);
        } else if (costAble instanceof Machine) {


        } else if (costAble instanceof Research) {

        }
        return Duration.ofMillis(0);
    }

    private Duration getBuildingDuration(int machLvl, int naniLevel) {
        double value = getBuildingValue();
        double machRed = machLvl + 1;
        double naniRed = Math.pow(2, naniLevel);
        double speed = planet.getPlayer().getUniverse().getBuildSpeed();

        double millis = value / machRed / naniRed / speed;
        return Duration.ofSeconds((long) millis);
    }

    private double getBuildingValue() {
        int cost = getCostSum();
        double step1 = cost * 1.44;
        double step2 = 4 - costAble.getLevel() + 1;
        double step3 = Math.max(step2, 1);
        return step1 / step3;
    }

    private int getCostSum() {
        Cost cost = costAble.getCost();

        int crystal = cost.getResource().getCrystal();
        int metal = cost.getResource().getMetal();
        return crystal + metal;
    }
}
