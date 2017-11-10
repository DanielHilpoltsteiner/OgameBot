package concurrent;

import comp.Production;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.util.List;

/**
 *
 */
public class ProductionService extends ScheduledService<Boolean> {
    private final List<Production> productions;

    public ProductionService(List<Production> productions) {
        this.productions = productions;
    }

    @Override
    protected Task<Boolean> createTask() {
        return null;
    }
}
