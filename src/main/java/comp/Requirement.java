package comp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class Requirement {
    private Map<RequestAble, Integer> requirementTable = new HashMap<>();

    public void put(RequestAble requestAble, int lvl) {
        requirementTable.put(requestAble, lvl);
    }

    public int get(RequestAble able) {
        Integer integer = requirementTable.get(able);
        return integer != null ? integer : 0;
    }

    public Set<RequestAble> getRequirements() {
        return requirementTable.keySet();
    }
}
