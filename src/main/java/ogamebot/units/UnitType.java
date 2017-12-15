package ogamebot.units;

import ogamebot.comp.Cost;
import ogamebot.comp.Requirement;
import ogamebot.comp.UpgradeAble;

/**
 *
 */
public interface UnitType<E extends UpgradeAble> {
    String getName();

    Cost getCost(int level);

    Requirement getRequirement();

    E create();

}
