package ogamebot.units.research;

import ogamebot.units.Container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class Researches extends Container<Research, ResearchField> {

    public Researches() {
        for (ResearchField field : ResearchField.values()) {
            put(field, field.create());
        }
    }

    public Researches(Collection<Research> researches) {
        final List<ResearchField> allowedTypes = Arrays.asList(ResearchField.values());
        List<ResearchField> allowed = new ArrayList<>(allowedTypes);

        for (Research research : researches) {
            //should not throw anything, BuildingType implements UnitType<Building>
            if (!allowedTypes.contains(research.getType())) {
                throw new IllegalArgumentException();
            }
            allowed.remove(research.getType());
        }
        final List<Research> result = new ArrayList<>(researches);

        if (!allowed.isEmpty()) {
            allowed.forEach(type -> result.add(type.create()));
        }
        result.forEach(research -> put(research.getType(), research));
    }
}
