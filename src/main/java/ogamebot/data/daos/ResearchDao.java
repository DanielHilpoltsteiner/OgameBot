package ogamebot.data.daos;

import gorgon.external.*;
import ogamebot.units.research.Research;
import ogamebot.units.research.ResearchField;

import java.util.List;

import static gorgon.external.Modifier.NOT_NULL;
import static gorgon.external.Ratio.ONE_TO_ONE;
import static gorgon.external.Type.ENUM;
import static gorgon.external.Type.INTEGER;

/**
 *
 */
public class ResearchDao extends DataTable<Research> {
    private Relation<Research, ResearchField> type = Relate.build(ONE_TO_ONE, "type", ResearchField.class, ENUM, Research::getType, NOT_NULL);
    private Relation<Research, Integer> level = Relate.build(ONE_TO_ONE, "level", INTEGER, Research::getCounter, NOT_NULL);


    protected ResearchDao() {
        super("RESEARCH_TABLE");
    }

    @Override
    public List<Relation<Research, ?>> getOneToOne() {
        return List.of(type, level);
    }

    @Override
    public List<Relation<Research, ?>> getOneToMany() {
        return List.of();
    }

    @Override
    public Research getData(Result<Research> result) throws PersistenceException {
        final ResearchField type = result.get(this.type);
        final Integer level = result.get(this.level);

        return Research.create(type, level);
    }
}
