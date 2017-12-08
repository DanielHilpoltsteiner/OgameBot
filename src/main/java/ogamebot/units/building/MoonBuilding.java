package ogamebot.units.building;

import ogamebot.comp.Cost;
import ogamebot.comp.Requirement;
import ogamebot.tools.Calculator;
import ogamebot.units.research.ResearchFields;

/**
 *
 */
public enum MoonBuilding implements BuildingType {
    METALSTORAGE("Metallspeicher", 500, 0, 0, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.NONE;
        }
    },
    CRYSTALSTORAGE("Kristallmine", 500, 250, 0, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.NONE;
        }
    },
    DEUTSTORAGE("Deuteriumtank", 500, 100, 0, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.NONE;
        }
    },
    MACHINEFACTORY("Roboterfabrik", 200, 60, 100, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.NONE;
        }
    },
    MOONBASE("Mondbasis", 10_000, 20_000, 10_000, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.NONE;
        }
    },
    SENSORPHALANX("Sensorphalanx", 10_000, 20_000, 10_000, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(MOONBASE, 1);
        }
    },
    JUMPG_GATE("Sprungtor", 1_000_000, 2_000_000, 1_000_000, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(MOONBASE, 1, ResearchFields.HYPERSPACE_TECH, 7);
        }
    }, SPACESHIPSHIPYARD("Raumschiffswerft", 200, 100, 50, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(MACHINEFACTORY, 2);
        }
    };

    private final String name;
    private final int metFactor;
    private final int crysFactor;
    private final int deutFactor;
    private final int base;

    MoonBuilding(String name, int metFactor, int crysFactor, int deutFactor, int base) {
        this.name = name;
        this.metFactor = metFactor;
        this.crysFactor = crysFactor;
        this.deutFactor = deutFactor;
        this.base = base;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public Cost getCost(int level) {
        return Calculator.simpleCost(metFactor, crysFactor, deutFactor, base, level);
    }

    @Override
    public Building create() {
        return new Building(this);
    }

    @Override
    public String toString() {
        return "MoonBuilding{" +
                "name='" + name + '\'' +
                '}';
    }
}
