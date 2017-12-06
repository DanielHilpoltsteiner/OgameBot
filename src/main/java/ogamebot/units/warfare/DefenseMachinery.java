package ogamebot.units.warfare;

import ogamebot.comp.Cost;
import ogamebot.units.UnitType;

/**
 *
 */
public interface DefenseMachinery extends UnitType<DefenceUnit>, WarfareType {
    @Override
    Cost getCost(int number);
}
