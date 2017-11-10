package units.Building;

import comp.RequestAble;
import units.UnitType;

/**
 *
 */
public enum PlanetBuilding implements UnitType<Building>, RequestAble {
    FUSIONPLANT("Fusionskraftwerk") {
    },
    SOLARPLANT("Solarkraftwerk") {
    },
    CRYSTALMINE("Kristallmine") {
    },
    DEUTSYNTH("Deuteriumsynthetisierer") {
    },
    METALMINE("Metallmine") {
    },
    CRYSTALSTORAGE("Kristallmine") {
    },
    DEUTSTORAGE("Deuteriumtank") {
    },
    METALSTORAGE("Metallspeicher") {
    },
    TERRAFORMER("Kristallspeicher") {
    },
    NANOFACTORY("Nanitenfabrik") {
    },
    ROBOFACTORY("Roboterfabrik") {
    },
    SPACESHIPSHIPYARD("Raumschiffswerft") {
    },
    RESEARCHLAB("Forschungslabor") {
    },
    ALLIANCEDEPOT("Allianzdepot") {
    },
    SPACEDOCK("Raumdock") {
    };

    private final String name;

    PlanetBuilding(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Building create() {
        return new SimpleBuilding();
    }
}
