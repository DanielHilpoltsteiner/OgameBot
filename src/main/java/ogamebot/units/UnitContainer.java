package ogamebot.units;

import ogamebot.comp.UpgradeAble;

import java.util.Collection;
import java.util.Set;

/**
 *
 */
public interface UnitContainer<E extends UpgradeAble, R extends UnitType<E>> {
    E getValue(R type);

    Collection<E> getValues();

    Set<R> getTypes();
}
