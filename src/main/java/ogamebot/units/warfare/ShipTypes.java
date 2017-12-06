package ogamebot.units.warfare;

import ogamebot.comp.Cost;
import ogamebot.units.UnitType;

/**
 *
 */
public interface ShipTypes extends UnitType<Ship>, WarfareType {
    @Override
    Cost getCost(int number);
}
