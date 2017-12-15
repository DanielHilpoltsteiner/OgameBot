package ogamebot.data.daos;

import gorgon.external.*;
import ogamebot.units.warfare.DefenceType;
import ogamebot.units.warfare.DefenceUnit;

import java.util.List;

import static gorgon.external.Modifier.NOT_NULL;
import static gorgon.external.Ratio.ONE_TO_ONE;
import static gorgon.external.Type.ENUM;
import static gorgon.external.Type.INTEGER;


/**
 *
 */
public class DefenceDao extends DataTable<DefenceUnit> {
    private Relation<DefenceUnit, DefenceType> type = Relate.build(ONE_TO_ONE, "type", DefenceType.class, ENUM, DefenceUnit::getType, NOT_NULL);
    private Relation<DefenceUnit, Integer> numbers = Relate.build(ONE_TO_ONE, "numbers", INTEGER, DefenceUnit::getNumbers, NOT_NULL);

    protected DefenceDao() {
        super("DEFENCE_TABLE");
    }

    @Override
    public List<Relation<DefenceUnit, ?>> getOneToOne() {
        return List.of(type, numbers);
    }

    @Override
    public List<Relation<DefenceUnit, ?>> getOneToMany() {
        return List.of();
    }

    @Override
    public DefenceUnit getData(Result<DefenceUnit> result) throws PersistenceException {
        final DefenceType type = result.get(this.type);
        final Integer numbers = result.get(this.numbers);

        return DefenceUnit.create(type, numbers);
    }
}
