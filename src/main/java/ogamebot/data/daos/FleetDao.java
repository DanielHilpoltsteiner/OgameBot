package ogamebot.data.daos;

import gorgon.external.*;
import ogamebot.units.warfare.ShipType;
import ogamebot.units.warfare.Ships;

import java.util.List;

import static gorgon.external.Modifier.NOT_NULL;
import static gorgon.external.Ratio.ONE_TO_ONE;
import static gorgon.external.Type.ENUM;
import static gorgon.external.Type.INTEGER;


/**
 *
 */
public class FleetDao extends DataTable<Ships> {
    private Relation<Ships, ShipType> type = Relate.build(ONE_TO_ONE, "type", ShipType.class, ENUM, Ships::getType, NOT_NULL);
    private Relation<Ships, Integer> numbers = Relate.build(ONE_TO_ONE, "numbers", INTEGER, Ships::getNumbers, NOT_NULL);

    protected FleetDao() {
        super("FLEET_TABLE");
    }

    @Override
    public List<Relation<Ships, ?>> getOneToOne() {
        return List.of(type, numbers);
    }

    @Override
    public List<Relation<Ships, ?>> getOneToMany() {
        return List.of();
    }

    @Override
    public Ships getData(Result<Ships> result) throws PersistenceException {
        final ShipType type = result.get(this.type);
        final Integer numbers = result.get(this.numbers);

        return Ships.create(type, numbers);
    }
}
