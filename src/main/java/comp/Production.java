package comp;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import tools.Condition;
import units.Building.Building;
import units.Building.PlanetBuilding;
import units.astroObjects.Planet;

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

    public int getMetProd() {
        return 0;
    }

    public int getCrysProd() {
        return 0;
    }

    public int getDeutProd() {
        return 0;
    }
}
