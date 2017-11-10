package units.research;

import comp.Cost;
import comp.RequestAble;
import comp.Requirement;

import java.time.Duration;

/**
 *
 */
public class Research implements RequestAble {
    private int level;
    private Cost cost;
    private Duration duration;
    private Requirement requirement;

    public int getLevel() {
        return level;
    }

    public Cost getCost() {
        return cost;
    }

    public Duration getDuration() {
        return duration;
    }
}
