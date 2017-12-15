package ogamebot.units.building;

import ogamebot.calc.Calculator;
import ogamebot.comp.Cost;
import ogamebot.comp.Requirement;
import ogamebot.units.Effector;
import ogamebot.units.RequireAble;
import ogamebot.units.research.ResearchField;

/**
 *
 */
public enum BuildingType implements RequireAble<Building>, Effector {
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
            return Requirement.of(MOONBASE, 1, ResearchField.HYPERSPACE_TECH, 7);
        }
    },
    SPACESHIPSHIPYARD("Raumschiffswerft", 200, 100, 50, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(ROBOFACTORY, 2);
        }
    },
    METALMINE("Metallmine", 40, 10, 0, 1.5) {
        @Override
        public Requirement getRequirement() {
            return Requirement.NONE;
        }
    },
    CRYSTALMINE("Kristallmine", 30, 15, 0, 1.6) {
        @Override
        public Requirement getRequirement() {
            return Requirement.NONE;
        }
    },
    DEUTSYNTH("Deuteriumsynthetisierer", 150, 50, 0, 1.5) {
        @Override
        public Requirement getRequirement() {
            return Requirement.NONE;
        }
    },
    FUSIONPLANT("Fusionskraftwerk", 500, 200, 100, 1.8) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(ResearchField.ENERGY_TECH, 3, DEUTSYNTH, 5);
        }
    },
    SOLARPLANT("Solarkraftwerk", 50, 20, 0, 1.5) {
        @Override
        public Requirement getRequirement() {
            return Requirement.NONE;
        }
    },
    METALSTORAGE("Metallspeicher", 500, 0, 0, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.NONE;
        }
    },
    CRYSTALSTORAGE("Kristallspeicher", 500, 250, 0, 2) {
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
    ROBOFACTORY("Roboterfabrik", 200, 60, 100, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.NONE;
        }
    },
    RESEARCHLAB("Forschungslabor", 25_000, 50_000, 500, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.NONE;
        }
    },
    NANOFACTORY("Nanitenfabrik", 500_000, 250_000, 50_000, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(ROBOFACTORY, 10, ResearchField.COMPUTER_TECH, 10);
        }
    },
    TERRAFORMER("Terraformer", 25_000, 50_000, 500, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(NANOFACTORY, 1, ResearchField.ENERGY_TECH, 12);
        }
    },
    ALLIANCEDEPOT("Allianzdepot", 10_000, 20_000, 0, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.NONE;
        }
    },
    SPACEDOCK("Raumdock", 40, 0, 10, 5) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(SPACESHIPSHIPYARD, 2);
        }
    },
    ROCKETSILO("Raketensilo", 10_000, 10_000, 500, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(SPACESHIPSHIPYARD, 1);
        }
    };


    private final String name;
    private final int metFactor;
    private final int crysFactor;
    private final int deutFactor;
    private final double base;

    BuildingType(String name, int metFactor, int crysFactor, int deutFactor, double base) {
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
        return getName();
    }
}
