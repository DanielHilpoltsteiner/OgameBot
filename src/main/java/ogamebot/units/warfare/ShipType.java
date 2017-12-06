package ogamebot.units.warfare;

import ogamebot.comp.Cost;
import ogamebot.comp.Requirement;
import ogamebot.tools.Calculator;

import java.util.HashMap;
import java.util.Map;

import static ogamebot.units.building.PlanetBuilding.SPACESHIPSHIPYARD;
import static ogamebot.units.research.ResearchFields.*;
import static ogamebot.units.warfare.DefenceType.*;

/**
 *
 */
public enum ShipType implements ShipTypes {
    LIGHT_FIGHTER("Leichter Jäger", 3_000, 1_000, 0, 4_000, 10, 50, 12_500, 50, 20) {
        private RapidFire rapidFire;private Requirement requirement;

        @Override
        public RapidFire getRapidFire() {
            if (rapidFire == null) {
                Map<ShipType, Integer> takes = Map.of(CRUISER, 6, DEATH_STAR, 200);
                Map<WarfareType, Integer> deals = Map.of(SPY_SONDE, 5, SOLAR_SATELLITE, 5);
                rapidFire = new RapidFire(takes, deals);

            }
            return rapidFire;
        }

        @Override
        public Requirement getRequirement() {
            if (requirement == null) {
                requirement = Requirement.of(SPACESHIPSHIPYARD, 1, COMBUSTION_ENGINE, 1);

            }
            return requirement;

        }
    },
    HEAVY_FIGHTER("Schwerer Jäger", 6_000, 1_000, 0, 10_000, 25, 150, 10_000, 100, 75) {
        private RapidFire rapidFire;private Requirement requirement;

        @Override
        public RapidFire getRapidFire() {
            if (rapidFire == null) {
                Map<ShipType, Integer> takes = Map.of(BATTLE_CRUISER, 4, DEATH_STAR, 100);
                Map<WarfareType, Integer> deals = Map.of(SPY_SONDE, 5, SOLAR_SATELLITE, 5, SMALL_TRANSPORTER, 3);
                rapidFire = new RapidFire(takes, deals);

            }
            return rapidFire;
        }

        @Override
        public Requirement getRequirement() {
            if (requirement == null) {
                requirement = Requirement.of(SPACESHIPSHIPYARD, 3, IMPULSE_ENGINGE, 2, SHIP_PLATING, 2);

            }
            return requirement;
        }
    },
    CRUISER("Kreuzer", 20_000, 7_000, 2_000, 27_000, 50, 400, 15_000, 800, 300) {
        private RapidFire rapidFire;private Requirement requirement;

        @Override
        public RapidFire getRapidFire() {
            if (rapidFire == null) {
                Map<ShipType, Integer> takes = Map.of(BATTLE_CRUISER, 4, DEATH_STAR, 33);
                Map<WarfareType, Integer> deals = Map.of(SPY_SONDE, 5, SOLAR_SATELLITE, 5, LIGHT_FIGHTER, 6, ROCKET_SENTRY, 10);
                rapidFire = new RapidFire(takes, deals);
            }
            return rapidFire;
        }

        @Override
        public Requirement getRequirement() {
            if (requirement == null) {
                requirement = Requirement.of(SPACESHIPSHIPYARD, 5, IMPULSE_ENGINGE, 4, ION_TECH, 2);

            }

            return requirement;
        }
    },
    BATTLESHIP("Schlachtschiff", 45_000, 15_000, 0, 60_000, 200, 1_000, 10_000, 1_500, 500) {
        private RapidFire rapidFire;private Requirement requirement;

        @Override
        public RapidFire getRapidFire() {
            if (rapidFire == null) {
                Map<ShipType, Integer> takes = Map.of(BATTLE_CRUISER, 7, DEATH_STAR, 30);
                Map<WarfareType, Integer> deals = Map.of(SPY_SONDE, 5, SOLAR_SATELLITE, 5);
                rapidFire = new RapidFire(takes, deals);

            }
            return rapidFire;
        }

        @Override
        public Requirement getRequirement() {
            if (requirement == null) {
                requirement = Requirement.of(SPACESHIPSHIPYARD, 7, HYPERSPACE_DRIVE, 4);

            }
            return requirement;
        }
    },
    BATTLE_CRUISER("Schlachtkreuzer", 30_000, 40_000, 15_000, 70_000, 400, 700, 10_000, 750, 250) {
        private RapidFire rapidFire;private Requirement requirement;

        @Override
        public RapidFire getRapidFire() {
            if (rapidFire == null) {
                Map<ShipType, Integer> takes = Map.of();
                Map<WarfareType, Integer> deals = Map.of();
                rapidFire = new RapidFire(takes, deals);

            }
            return rapidFire;
        }

        @Override
        public Requirement getRequirement() {
            if (requirement == null) {
                requirement = Requirement.of(SPACESHIPSHIPYARD, 8, HYPERSPACE_DRIVE, 5, HYPERSPACE_TECH, 5, LASER_TECH, 12);

            }
            return requirement;
        }
    },
    BOMBER("Bomber", 50_000, 25_000, 15_000, 75_000, 500, 1_000, 5_000, 500, 1_000) {
        private RapidFire rapidFire;private Requirement requirement;

        @Override
        public RapidFire getRapidFire() {
            if (rapidFire == null) {
                Map<ShipType, Integer> takes = Map.of(DEATH_STAR, 25);
                Map<WarfareType, Integer> deals = Map.of(SPY_SONDE, 5, SOLAR_SATELLITE, 5, ROCKET_SENTRY, 20, LIGHT_LASER, 20, HEAVY_LASER, 20, ION_SENTRY, 10);
                rapidFire = new RapidFire(takes, deals);

            }
            return rapidFire;
        }

        @Override
        public Requirement getRequirement() {
            if (requirement == null) {
                requirement = Requirement.of(SPACESHIPSHIPYARD, 8, IMPULSE_ENGINGE, 6, PLASMA_TECH, 5);

            }
            return requirement;
        }
    },
    DESTROYER("Zerstörer", 60_000, 50_000, 15_000, 110_000, 500, 2000, 5_000, 2_000, 1_000) {
        private RapidFire rapidFire;private Requirement requirement;

        @Override
        public RapidFire getRapidFire() {
            if (rapidFire == null) {
                Map<ShipType, Integer> takes = Map.of();
                Map<WarfareType, Integer> deals = Map.of();
                rapidFire = new RapidFire(takes, deals);

            }
            return rapidFire;
        }

        @Override
        public Requirement getRequirement() {
            if (requirement == null) {
                requirement = Requirement.of(SPACESHIPSHIPYARD, 9, HYPERSPACE_DRIVE, 6, HYPERSPACE_TECH, 5);

            }
            return requirement;
        }
    },
    DEATH_STAR("Todesstern", 5_000_000, 4_000_000, 1_000_000, 9_000_000, 50_000, 200_000, 100, 1_000_000, 1) {
        private RapidFire rapidFire;private Requirement requirement;

        @Override
        public RapidFire getRapidFire() {
            if (rapidFire == null) {
                Map<ShipType, Integer> takes = Map.of();
                Map<WarfareType, Integer> deals = new HashMap<>();
                deals.put(SMALL_TRANSPORTER, 250);
                deals.put(BIG_TRANSPORTER, 250);
                deals.put(LIGHT_FIGHTER, 200);
                deals.put(HEAVY_FIGHTER, 100);
                deals.put(CRUISER, 33);
                deals.put(BATTLESHIP, 30);
                deals.put(COLONYSHIP, 250);
                deals.put(RECYCLER, 250);
                deals.put(SPY_SONDE, 1250);
                deals.put(SOLAR_SATELLITE, 1250);
                deals.put(BOMBER, 25);
                deals.put(DESTROYER, 5);
                deals.put(BATTLE_CRUISER, 15);
                deals.put(ROCKET_SENTRY, 200);
                deals.put(LIGHT_LASER, 200);
                deals.put(HEAVY_LASER, 100);
                deals.put(GAUSS_CANNON, 50);
                deals.put(ION_SENTRY, 100);
                rapidFire = new RapidFire(takes, deals);
            }
            return rapidFire;
        }

        @Override
        public Requirement getRequirement() {
            if (requirement == null) {
                requirement = Requirement.of(SPACESHIPSHIPYARD, 12, GRAVITON_RESEARCH, 1, HYPERSPACE_TECH, 6, HYPERSPACE_DRIVE, 7);
            }
            return requirement;
        }
    },
    SMALL_TRANSPORTER("Kleiner Transporter", 2_000, 2_000, 0, 4_000, 10, 5, 10_000, 5_000, 20, true) {
        private RapidFire rapidFire;private Requirement requirement;

        @Override
        public RapidFire getRapidFire() {
            if (rapidFire == null) {
                Map<ShipType, Integer> takes = Map.of(BATTLE_CRUISER, 3, HEAVY_FIGHTER, 3, DEATH_STAR, 250);
                Map<WarfareType, Integer> deals = Map.of(SPY_SONDE, 5, SOLAR_SATELLITE, 5);
                rapidFire = new RapidFire(takes, deals);

            }
            return rapidFire;
        }

        @Override
        public Requirement getRequirement() {
            if (requirement == null) {
                requirement = Requirement.of(SPACESHIPSHIPYARD, 2, COMBUSTION_ENGINE, 2);

            }
            return requirement;
        }
    },
    BIG_TRANSPORTER("Großer Transporter", 6_000, 6_000, 0, 12_000, 25, 5, 7_500, 25_000, 50, true) {
        private RapidFire rapidFire;private Requirement requirement;

        @Override
        public RapidFire getRapidFire() {
            if (rapidFire == null) {
                Map<ShipType, Integer> takes = Map.of(BATTLE_CRUISER, 3, DEATH_STAR, 250);
                Map<WarfareType, Integer> deals = Map.of(SPY_SONDE, 5, SOLAR_SATELLITE, 5);
                rapidFire = new RapidFire(takes, deals);

            }
            return rapidFire;
        }

        @Override
        public Requirement getRequirement() {
            if (requirement == null) {
                requirement = Requirement.of(SPACESHIPSHIPYARD, 4, COMBUSTION_ENGINE, 6);

            }
            return requirement;
        }
    },
    COLONYSHIP("Kolonieschiff", 10_000, 20_000, 10_000, 30_000, 100, 50, 2_500, 7_500, 1000, true) {
        private RapidFire rapidFire;private Requirement requirement;

        @Override
        public RapidFire getRapidFire() {
            if (rapidFire == null) {
                Map<ShipType, Integer> takes = Map.of(DEATH_STAR, 250);
                Map<WarfareType, Integer> deals = Map.of(SPY_SONDE, 5, SOLAR_SATELLITE, 5);
                rapidFire = new RapidFire(takes, deals);

            }
            return rapidFire;
        }

        @Override
        public Requirement getRequirement() {
            if (requirement == null) {
                requirement = Requirement.of(SPACESHIPSHIPYARD, 4, IMPULSE_ENGINGE, 3);

            }
            return requirement;
        }
    },
    RECYCLER("Recycler", 10_000, 6_000, 2_000, 16_000, 10, 1, 2_000, 20_000, 300, true) {
        private RapidFire rapidFire;private Requirement requirement;

        @Override
        public RapidFire getRapidFire() {
            if (rapidFire == null) {
                Map<ShipType, Integer> takes = Map.of();
                Map<WarfareType, Integer> deals = Map.of();
                rapidFire = new RapidFire(takes, deals);
            }
            return rapidFire;
        }

        @Override
        public Requirement getRequirement() {
            if (requirement == null) {
                requirement = Requirement.of(SPACESHIPSHIPYARD, 4, COMBUSTION_ENGINE, 6, SHIELD_TECH, 2);

            }
            return requirement;
        }
    },
    SPY_SONDE("Spionagesonde", 0, 1_000, 0, 1_000, 0.01, 0.01, 100_000_000, 0, 1, true) {
        private RapidFire rapidFire;private Requirement requirement;

        @Override
        public RapidFire getRapidFire() {
            if (rapidFire == null) {
                Map<ShipType, Integer> takes = new HashMap<>();
                for (ShipType shipType : ShipType.values()) {
                    if (shipType == SPY_SONDE || shipType == SOLAR_SATELLITE) {
                        continue;
                    }
                    if (shipType == DEATH_STAR) {
                        takes.put(shipType, 1250);
                    } else {
                        takes.put(shipType, 5);
                    }
                }
                Map<WarfareType, Integer> deals = Map.of();
                rapidFire = new RapidFire(takes, deals);

            }
            return rapidFire;
        }

        @Override
        public Requirement getRequirement() {
            if (requirement == null) {
                requirement = Requirement.of(SPACESHIPSHIPYARD, 3, COMBUSTION_ENGINE, 3, SPIONAGE_TECH, 2);
            }
            return requirement;
        }
    },
    SOLAR_SATELLITE("Solarsatellit", 0, 2_000, 500, 2_000, 1, 1, 0, 0, 0, true) {
        private RapidFire rapidFire;
        private Requirement requirement;

        @Override
        public RapidFire getRapidFire() {
            if (rapidFire == null) {
                Map<ShipType, Integer> takes = new HashMap<>();
                for (ShipType shipType : ShipType.values()) {

                    if (shipType == SPY_SONDE || shipType == SOLAR_SATELLITE) {
                        continue;
                    }
                    if (shipType == DEATH_STAR) {
                        takes.put(shipType, 1250);
                    } else {
                        takes.put(shipType, 5);
                    }
                }
                Map<WarfareType, Integer> deals = Map.of();
                rapidFire = new RapidFire(takes, deals);

            }
            return rapidFire;
        }

        @Override
        public Requirement getRequirement() {
            if (requirement == null) {
                requirement = Requirement.of(SPACESHIPSHIPYARD, 1);
            }
            return requirement;
        }
    };

    private final String name;
    private final int metalCost;
    private final int crystalCost;
    private final int deutCost;
    private final int structurePoints;
    private final double shield;
    private final double attack;
    private final int speed;
    private final int capacity;
    private final int consumption;
    private boolean civil = false;

    ShipType(String name, int metalCost, int crystalCost, int deutCost, int structurePoints, double shield, double attack, int speed, int capacity, int consumption, boolean civil) {
        this.name = name;
        this.metalCost = metalCost;
        this.crystalCost = crystalCost;
        this.deutCost = deutCost;
        this.structurePoints = structurePoints;
        this.shield = shield;
        this.attack = attack;
        this.speed = speed;
        this.capacity = capacity;
        this.consumption = consumption;
        this.civil = civil;
    }


    ShipType(String name, int metalCost, int crystalCost, int deutCost, int structurePoints, double shield, double attack, int speed, int capacity, int consumption) {
        this.name = name;
        this.metalCost = metalCost;
        this.crystalCost = crystalCost;
        this.deutCost = deutCost;
        this.structurePoints = structurePoints;
        this.shield = shield;
        this.attack = attack;
        this.speed = speed;
        this.capacity = capacity;
        this.consumption = consumption;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Cost getCost(int number) {
        return Calculator.simpleMassCost(metalCost, crystalCost, deutCost, number);
    }

    @Override
    public Ship create() {
        return new Ship(getRapidFire(), structurePoints, shield, attack, speed, capacity, consumption, this);
    }
}
