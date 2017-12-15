package ogamebot.data.daos;

import gorgon.external.*;
import ogamebot.units.building.Building;
import ogamebot.units.building.BuildingType;

import java.util.List;

import static gorgon.external.Modifier.NOT_NULL;
import static gorgon.external.Ratio.ONE_TO_ONE;
import static gorgon.external.Type.*;

/**
 *
 */
public class BuildingsDao extends DataTable<Building> {
    private Relation<Building, BuildingType> type = Relate.build(ONE_TO_ONE, "type", BuildingType.class, ENUM, Building::getType, NOT_NULL);
    private Relation<Building, Integer> numbers = Relate.build(ONE_TO_ONE, "level", INTEGER, Building::getCounter, NOT_NULL);
    private Relation<Building, Double> output = Relate.build(ONE_TO_ONE, "output", DOUBLE, Building::getOutput, NOT_NULL);

    protected BuildingsDao() {
        super("BUILDINGS_TABLE");
    }

    @Override
    public List<Relation<Building, ?>> getOneToOne() {
        return List.of(type, numbers, output);
    }

    @Override
    public List<Relation<Building, ?>> getOneToMany() {
        return List.of();
    }

    @Override
    public Building getData(Result<Building> result) throws PersistenceException {
        final BuildingType type = result.get(this.type);
        final Integer numbers = result.get(this.numbers);
        final Double output = result.get(this.output);

        return Building.create(type, numbers, output);
    }
}
