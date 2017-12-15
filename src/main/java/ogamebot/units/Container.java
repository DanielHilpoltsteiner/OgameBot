package ogamebot.units;

import ogamebot.comp.UpgradeAble;

import java.util.*;

/**
 *
 */
public abstract class Container<E extends UpgradeAble, R extends UnitType<E>> implements UnitContainer<E, R> {

    private Map<R, E> map = new HashMap<>();

    protected void put(R type, E e) {
        map.put(type, e);
    }

    @Override
    public Set<R> getTypes() {
        return new HashSet<>(map.keySet());
    }

    @Override
    public E getValue(R type) {
        return map.get(type);
    }

    @Override
    public Collection<E> getValues() {
        return new ArrayList<>(map.values());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "map=" + map +
                '}';
    }
}
