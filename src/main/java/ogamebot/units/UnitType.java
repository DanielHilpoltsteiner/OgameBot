package ogamebot.units;

import ogamebot.comp.Cost;
import ogamebot.comp.Requirement;

/**
 *
 */
public interface UnitType<E> {
    String getName();

    Cost getCost(int level);

    Requirement getRequirement();

    E create();
}
