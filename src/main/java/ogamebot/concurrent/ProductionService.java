package ogamebot.concurrent;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import ogamebot.comp.Production;
import tools.Condition;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ProductionService extends ScheduledService<Boolean> {
    private final List<Production> productions;

    public ProductionService(List<Production> productions) {
        this.productions = new ArrayList<>(productions);
    }

    public void addProduction(Production production) {
        Condition.check().nonNull(production);
        productions.add(production);
    }

    @Override
    protected Task<Boolean> createTask() {
        return null;
    }
}
