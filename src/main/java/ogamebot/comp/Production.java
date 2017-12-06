package ogamebot.comp;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.building.Building;
import ogamebot.units.building.PlanetBuilding;
import ogamebot.units.units.Resource;
import tools.Condition;

import java.time.Duration;

/**
 *
 */
public class Production {
    private final Building metalMine;
    private final Building crystalMine;
    private final Building deutSynth;
    private final Planet planet;

    private IntegerProperty basicMetProd = new SimpleIntegerProperty(90);
    private IntegerProperty basicCrysProd = new SimpleIntegerProperty(45);
    private IntegerProperty basicDeutProd = new SimpleIntegerProperty(0);

    private DoubleProperty metProd = new SimpleDoubleProperty(90);
    private DoubleProperty crysProd = new SimpleDoubleProperty(45);
    private DoubleProperty deutProd = new SimpleDoubleProperty(0);

    public Production(Planet planet) {
        Condition.check().nonNull(planet);
        this.planet = planet;
        this.metalMine = planet.getBuilding(PlanetBuilding.METALMINE);
        this.crystalMine = planet.getBuilding(PlanetBuilding.CRYSTALMINE);
        this.deutSynth = planet.getBuilding(PlanetBuilding.DEUTSYNTH);
    }


    public static Resource forDuration(Planet planet, int metLevel, int crysLevel, int deutLevel, Duration duration) {
        Condition.check().positive(metLevel, crysLevel, deutLevel);
        final long seconds = duration.getSeconds();

        final double metalMineProduction = PlanetBuilding.METALMINE.getProduction(planet, metLevel);
        final double crystalMineProduction = PlanetBuilding.CRYSTALMINE.getProduction(planet, crysLevel);
        final double deutSynthProduction = PlanetBuilding.DEUTSYNTH.getProduction(planet, deutLevel);

        final double producedMet = calcProducedResource(metalMineProduction, seconds);
        final double producedCrys = calcProducedResource(crystalMineProduction, seconds);
        final double producedDeut = calcProducedResource(deutSynthProduction, seconds);

        return new Resource((long) producedMet, (long) producedCrys, (long) producedDeut);
    }

    private static double calcProducedResource(double production, long seconds) {
        double factor = seconds / 3600;
        return production * factor;
    }

    public Resource forDuration(Duration duration) {
        final long seconds = duration.getSeconds();

        final double producedMet = getProducedMet(seconds, PlanetBuilding.METALMINE);
        final double producedCrys = getProducedMet(seconds, PlanetBuilding.CRYSTALMINE);
        final double producedDeut = getProducedMet(seconds, PlanetBuilding.DEUTSYNTH);

        return new Resource((long) producedMet, (long) producedCrys, (long) producedDeut);
    }

    private double getProducedMet(long seconds, PlanetBuilding type) {
        final Building metalMine = planet.getBuilding(type);
        final double metalmineProduction = PlanetBuilding.METALMINE.getProduction(planet, metalMine.getCounter());
        return calcProducedResource(metalmineProduction, seconds);
    }

    public DoubleProperty metProdProperty() {
        return metProd;
    }

    public DoubleProperty crysProdProperty() {
        return crysProd;
    }

    public int getMetProd() {
        return 0;
    }

    public int getCrysProd() {
        return 0;
    }

    public int getDeutProd() {
        return 0;
    }

    public DoubleProperty deutProdProperty() {
        return deutProd;
    }
}
