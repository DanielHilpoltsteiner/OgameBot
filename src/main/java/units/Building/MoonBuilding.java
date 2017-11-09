package units.Building;

import units.UnitType;

/**
 *
 */
public enum MoonBuilding implements UnitType<Building> {
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
