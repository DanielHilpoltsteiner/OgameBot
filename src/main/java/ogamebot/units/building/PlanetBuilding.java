package ogamebot.units.building;

import ogamebot.comp.Cost;
import ogamebot.comp.Player;
import ogamebot.comp.Requirement;
import ogamebot.tools.Calculator;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.research.ResearchFields;

/**
 *
 */
public enum PlanetBuilding implements BuildingType {
    METALMINE("Metallmine", 40, 10, 0, 1.5) {
        @Override
        public Requirement getRequirement() {
            return Requirement.NONE;
        }

        public double getProduction(Planet planet, int level) {
            return Calculator.production(planet, this, level, planet::getMetalBooster, 30, 30, 1);
        }
    },
    CRYSTALMINE("Kristallmine", 30, 15, 0, 1.6) {
        @Override
        public Requirement getRequirement() {
            return Requirement.NONE;
        }

        public double getProduction(Planet planet, int level) {
            return Calculator.production(planet, this, level, planet::getMetalBooster, 15, 10, 0.66);
        }
    },
    DEUTSYNTH("Deuteriumsynthetisierer", 150, 50, 0, 1.5) {
        @Override
        public Requirement getRequirement() {
            return Requirement.NONE;
        }

        @Override
        public double getProduction(Planet planet, int level) {
            final Building building = planet.getBuilding(this);

            double product = 10 * level;
            final double expo = Math.pow(1.1, level);
            product *= expo;
            product *= building.getOutput();

            double tempInfluence = 0.004 * planet.getMaxT();
            tempInfluence = 1.44 - tempInfluence;
            product *= tempInfluence;

            final Player player = planet.getPlayer();

            if (player.hasGeologist()) {
                product *= 1.1;
            }
            product *= planet.getDeutBooster();
            product = Math.floor(product);

            final int researchLvl = player.getResearch(ResearchFields.PLASMA_TECH).getCounter();
            double researchBoost = researchLvl * 0.33;
            researchBoost += 100;
            researchBoost /= 100;

            product *= researchBoost;
            final double universeSpeed = player.getUniverse().getEconomySpeed();
            return product * universeSpeed;
        }
    },
    FUSIONPLANT("Fusionskraftwerk", 500, 200, 100, 1.8) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(ResearchFields.ENERGY_TECH, 3, DEUTSYNTH, 5);
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
    SPACESHIPSHIPYARD("Raumschiffswerft", 200, 100, 50, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(ROBOFACTORY, 2);
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
            return Requirement.of(ROBOFACTORY, 10, ResearchFields.COMPUTER_TECH, 10);
        }
    },
    TERRAFORMER("Kristallspeicher", 25_000, 50_000, 500, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(NANOFACTORY, 1, ResearchFields.ENERGY_TECH, 12);
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
    private final int metalFactor;
    private final int crysFactor;
    private final int deutFactor;
    private final double base;

    PlanetBuilding(String name, int metalFactor, int crysFactor, int deutFactor, double base) {
        this.name = name;
        this.metalFactor = metalFactor;
        this.crysFactor = crysFactor;
        this.deutFactor = deutFactor;
        this.base = base;
    }

    /**
     * Production of an Resource per Hour.
     *
     * @param planet
     * @return
     */
    public double getProduction(Planet planet, int level) {
        return 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Cost getCost(int level) {
        return Calculator.simpleCost(metalFactor, crysFactor, deutFactor, base, level);
    }

    @Override
    public Building create() {
        return new Building(this);
    }

    @Override
    public String toString() {
        return "PlanetBuilding{" +
                "name='" + name + '\'' +
                '}';
    }
}
