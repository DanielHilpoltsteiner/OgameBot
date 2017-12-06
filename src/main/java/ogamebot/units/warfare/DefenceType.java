package ogamebot.units.warfare;

import ogamebot.comp.Cost;
import ogamebot.comp.Requirement;
import ogamebot.tools.Calculator;

import java.util.Map;

import static ogamebot.units.building.PlanetBuilding.SPACESHIPSHIPYARD;
import static ogamebot.units.research.ResearchFields.*;
import static ogamebot.units.warfare.ShipType.*;

/**
 *
 */
public enum DefenceType implements DefenseMachinery {
    ROCKET_SENTRY("Raketenwerfer", 2_000, 0, 0, 2_000, 20, 80) {
        @Override
        public RapidFire getRapidFire() {
            Map<ShipType, Integer> takes = Map.of(BOMBER, 20, CRUISER, 10, DEATH_STAR, 200);
            Map<WarfareType, Integer> deals = Map.of();
            return new RapidFire(takes, deals);
        }


        @Override
        public Requirement getRequirement() {
            return Requirement.of(SPACESHIPSHIPYARD, 1);
        }
    },
    LIGHT_LASER("Leichtes Lasergeschütz", 1_500, 500, 0, 2_000, 25, 100) {
        @Override
        public RapidFire getRapidFire() {
            Map<ShipType, Integer> takes = Map.of();
            Map<WarfareType, Integer> deals = Map.of();
            return new RapidFire(takes, deals);
        }


        @Override
        public Requirement getRequirement() {
            return Requirement.of(SPACESHIPSHIPYARD, 2, ENERGY_TECH, 1, LASER_TECH, 3);
        }
    },
    HEAVY_LASER("Schweres Lasergeschütz", 6_000, 2_000, 0, 8_000, 100, 250) {
        @Override
        public RapidFire getRapidFire() {
            Map<ShipType, Integer> takes = Map.of(BOMBER, 10, DEATH_STAR, 100);
            Map<WarfareType, Integer> deals = Map.of();
            return new RapidFire(takes, deals);
        }


        @Override
        public Requirement getRequirement() {
            return Requirement.of(SPACESHIPSHIPYARD, 4, ENERGY_TECH, 3, LASER_TECH, 6);
        }
    },
    GAUSS_CANNON("Gaußkanone", 20_000, 15_000, 2_000, 35_000, 200, 1_100) {
        @Override
        public RapidFire getRapidFire() {
            Map<ShipType, Integer> takes = Map.of(DEATH_STAR, 50);
            Map<WarfareType, Integer> deals = Map.of();
            return new RapidFire(takes, deals);
        }


        @Override
        public Requirement getRequirement() {
            return Requirement.of(SPACESHIPSHIPYARD, 6, ENERGY_TECH, 6, WEAPON_TECH, 3, SHIELD_TECH, 1);
        }
    },
    ION_SENTRY("Ionengeschütz", 2_000, 6_000, 0, 8_000, 500, 150) {
        @Override
        public RapidFire getRapidFire() {
            Map<ShipType, Integer> takes = Map.of(BOMBER, 10, DEATH_STAR, 100);
            Map<WarfareType, Integer> deals = Map.of();
            return new RapidFire(takes, deals);
        }


        @Override
        public Requirement getRequirement() {
            return Requirement.of(SPACESHIPSHIPYARD, 4, ION_TECH, 4);
        }
    },
    PLASMA_SENTRY("Plasmawerfer", 50_000, 50_000, 50_000, 100_000, 300, 3_000) {
        @Override
        public RapidFire getRapidFire() {
            return RapidFire.NONE;
        }


        @Override
        public Requirement getRequirement() {
            return Requirement.of(SPACESHIPSHIPYARD, 8, PLASMA_TECH, 7);
        }
    },
    SMALL_SHIELD("Kleine Schildkuppel", 5_000, 5_000, 0, 20_000, 2_000, 1) {
        @Override
        public RapidFire getRapidFire() {
            return RapidFire.NONE;
        }


        @Override
        public Requirement getRequirement() {
            return Requirement.of(SPACESHIPSHIPYARD, 1, SHIELD_TECH, 2);
        }
    },
    GREAT_SHIELD("Große Schildkuppel", 50_000, 50_000, 0, 100_000, 10_000, 1) {
        @Override
        public RapidFire getRapidFire() {
            return RapidFire.NONE;
        }

        @Override
        public Requirement getRequirement() {
            return Requirement.of(SPACESHIPSHIPYARD, 6, SHIELD_TECH, 6);
        }
    },;


    private final String name;
    private final int metCost;
    private final int crysCost;
    private final int deutCost;
    private final int structure;
    private final int shield;
    private final int attack;

    DefenceType(String name, int metCost, int crysCost, int deutCost, int structure, int shield, int attack) {
        this.name = name;
        this.metCost = metCost;
        this.crysCost = crysCost;
        this.deutCost = deutCost;
        this.structure = structure;
        this.shield = shield;
        this.attack = attack;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Cost getCost(int number) {
        return Calculator.simpleMassCost(metCost, crysCost, deutCost, number);
    }

    @Override
    public DefenceUnit create() {
        return new DefenceUnit(getRapidFire(), structure, shield, attack, this);
    }
}
