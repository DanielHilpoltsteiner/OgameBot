package units.Building;

import comp.RequestAble;
import units.UnitType;

/**
 *
 */
public enum MoonBuilding implements UnitType<Building>, RequestAble {
    CRYSTALSTORAGE("Kristallmine") {
    },
    DEUTSTORAGE("Deuteriumtank") {
    },
    METALSTORAGE("Metallspeicher") {
    },
    MACHINEFACTORY("Roboterfabrik") {
    },
    MOONBASE("Mondbasis") {
    },
    SENSORPHALANX("Sensorphalanx") {
    },
    JUMPG_GATE("Sprungtor") {
    };

    private final String name;

    MoonBuilding(String name) {
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
