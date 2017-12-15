package ogamebot.units.warfare;

import ogamebot.calc.Calculator;
import ogamebot.comp.Cost;
import ogamebot.comp.Requirement;

import static ogamebot.units.building.BuildingType.ROCKETSILO;
import static ogamebot.units.building.BuildingType.SPACESHIPSHIPYARD;
import static ogamebot.units.research.ResearchField.IMPULSE_ENGINGE;

/**
 *
 */
public enum Rockets implements Machine {
    INTERCEPTOR_MISSILE("Abfangrakete", 8_000, 0, 2_000, 8_000, 1, 1) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(SPACESHIPSHIPYARD, 1, ROCKETSILO, 2);
        }
    },
    INTERPLANETARY_MISSILE("Interplanetarrakete", 12_500, 2_500, 10_000, 15_000, 1, 12_000) {
        @Override
        public Requirement getRequirement() {
            return Requirement.of(SPACESHIPSHIPYARD, 1, ROCKETSILO, 4, IMPULSE_ENGINGE, 1);
        }
    },;

    private final String name;
    private final int metCost;
    private final int crystalCost;
    private final int deutCost;
    private final int structure;
    private final int shield;
    private final int attack;


    Rockets(String name, int metCost, int crystalCost, int deutCost, int structure, int shield, int attack) {
        this.name = name;
        this.metCost = metCost;
        this.crystalCost = crystalCost;
        this.deutCost = deutCost;
        this.structure = structure;
        this.shield = shield;
        this.attack = attack;
    }

    public String getName() {
        return name;
    }

    @Override
    public Cost getCost(int number) {
        return Calculator.simpleMassCost(metCost, crystalCost, deutCost, number);
    }


    public WarfareUnit create() {
        return new Rocket(structure, shield, attack, this);
    }

}
