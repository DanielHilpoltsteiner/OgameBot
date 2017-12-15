package ogamebot.units.research;

import ogamebot.calc.Calculator;
import ogamebot.comp.Cost;
import ogamebot.comp.Requirement;
import ogamebot.units.building.BuildingType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 *
 */
public enum ResearchField implements ResearchType {
    ENERGY_TECH("Energietechnik", 0, 400, 200, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(BuildingType.RESEARCHLAB, 1);
        }
    },
    LASER_TECH("Lasertechnik", 100, 50, 0, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(BuildingType.RESEARCHLAB, 1, ENERGY_TECH, 2);
        }
    },
    ION_TECH("Ionentechnik", 500, 150, 50, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(BuildingType.RESEARCHLAB, 4, LASER_TECH, 5, ENERGY_TECH, 4);
        }
    },
    PLASMA_TECH("Plasmatechnik", 1_000, 2_000, 500, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(BuildingType.RESEARCHLAB, 4, LASER_TECH, 10, ION_TECH, 5, ENERGY_TECH, 8);
        }
    },
    HYPERSPACE_TECH("Hyperraumtechnik", 0, 2_000, 1_000, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(BuildingType.RESEARCHLAB, 7, ENERGY_TECH, 5, SHIELD_TECH, 5);
        }
    },
    COMBUSTION_ENGINE("Verbrennungstriebwerk", 200, 0, 300, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(BuildingType.RESEARCHLAB, 1, ENERGY_TECH, 1);
        }
    },
    IMPULSE_ENGINGE("Impulstriebwerk", 1_000, 2_000, 300, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(BuildingType.RESEARCHLAB, 2, ENERGY_TECH, 1);
        }
    },
    HYPERSPACE_DRIVE("Hyperraumantrieb", 5_000, 10_000, 3_000, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(BuildingType.RESEARCHLAB, 7, HYPERSPACE_TECH, 3);
        }
    },
    SPIONAGE_TECH("Spionagetechnik", 100, 500, 100, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(BuildingType.RESEARCHLAB, 3);
        }
    },
    COMPUTER_TECH("Computertechnik", 0, 200, 300, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(BuildingType.RESEARCHLAB, 1);
        }
    },
    ASTROPHYSICS("Astrophysik", 0, 0, 0, 0) {
        @Override
        public Cost getCost(int level) {
            final BigInteger metal = getPartialResult(level, 40);
            final BigInteger crystal = getPartialResult(level, 80);
            final BigInteger deut = getPartialResult(level, 40);

            return new Cost(metal, crystal, deut);
        }

        @Override
        public Requirement getRequirement() {
            return Requirement.of(BuildingType.RESEARCHLAB, 3, SPIONAGE_TECH, 4, IMPULSE_ENGINGE, 3);
        }

        private BigInteger getPartialResult(int level, int factor) {
            final BigInteger exp = Calculator.simpleExp(factor, 1.75, level - 1);

            final BigDecimal add = new BigDecimal(exp).add(BigDecimal.valueOf(0.5));
            final BigInteger roundDown = add.setScale(0, RoundingMode.DOWN).toBigInteger();

            return roundDown.multiply(BigInteger.valueOf(100));
        }
    },
    INTERGALACTIC_RESEARCHNETWORK("Intergalaktisches Forschungsnetzwerk", 120_000, 200_000, 80_000, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(BuildingType.RESEARCHLAB, 10, COMPUTER_TECH, 8, HYPERSPACE_TECH, 8);
        }
    },
    GRAVITON_RESEARCH("Gravitonforschung", true, 100_000, 3) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(BuildingType.RESEARCHLAB, 12);
        }
    },
    WEAPON_TECH("Waffentechnik", 400, 200, 0, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(BuildingType.RESEARCHLAB, 4);
        }
    },
    SHIELD_TECH("Schildtechnik", 100, 300, 0, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(BuildingType.RESEARCHLAB, 6, ENERGY_TECH, 3);
        }
    },
    SHIP_PLATING("Raumschiffpanzerung", 500, 0, 0, 2) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(BuildingType.RESEARCHLAB, 2);
        }
    };


    private final String name;
    private final int metFactor;
    private final int crysFactor;
    private final int deutFactor;
    private final double base;
    private int energyFactor;
    private boolean needsEnergy = false;

    ResearchField(String name, int metFactor, int crysFactor, int deutFactor, double base) {
        this.name = name;
        this.metFactor = metFactor;
        this.crysFactor = crysFactor;
        this.deutFactor = deutFactor;
        this.base = base;
    }


    ResearchField(String name, boolean needsEnergy, int energyFactor, int base) {
        this.needsEnergy = needsEnergy;
        this.name = name;
        this.energyFactor = energyFactor;
        this.base = base;
        metFactor = 0;
        crysFactor = 0;
        deutFactor = 0;
    }

    public BigInteger getEnergyCost(int level) {
        if (!needsEnergy) {
            throw new IllegalStateException();
        } else {
            return Calculator.simpleExp(energyFactor, base, level);
        }
    }

    public boolean isNeedsEnergy() {
        return needsEnergy;
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
    public Research create() {
        return new Research(this);
    }


    @Override
    public String toString() {
        return "ResearchField{" +
                "name='" + name + '\'' +
                '}';
    }
}
