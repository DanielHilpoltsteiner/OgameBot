package ogamebot.data.daos;

import gorgon.external.*;
import ogamebot.units.astroObjects.Moon;
import ogamebot.units.astroObjects.MoonBuilder;
import ogamebot.units.building.Building;
import ogamebot.units.warfare.DefenceUnit;
import ogamebot.units.warfare.Ships;

import java.util.Collection;
import java.util.List;

import static gorgon.external.Modifier.NOT_NULL;
import static gorgon.external.Ratio.ONE_TO_MANY;
import static gorgon.external.Ratio.ONE_TO_ONE;
import static gorgon.external.Type.*;

/**
 *
 */
public class MoonDao extends DataTable<Moon> {
    private Relation<Moon, String> name = Relate.build(ONE_TO_ONE, "name", TEXT, Moon::getName, NOT_NULL);
    private Relation<Moon, Integer> maxT = Relate.build(ONE_TO_ONE, "maxT", INTEGER, Moon::getMaxT, NOT_NULL);
    private Relation<Moon, Integer> fields = Relate.build(ONE_TO_ONE, "basicFields", INTEGER, Moon::getFields, NOT_NULL);

    private Relation<Moon, Collection<DefenceUnit>> defences = Relate.build(ONE_TO_MANY, DefenceUnit.class, ID, Moon::getDefences, NOT_NULL);
    private Relation<Moon, Collection<Ships>> ships = Relate.build(ONE_TO_MANY, Ships.class, ID, Moon::getShips, NOT_NULL);
    private Relation<Moon, Collection<Building>> buildings = Relate.build(ONE_TO_MANY, Building.class, ID, Moon::getBuildings, NOT_NULL);


    protected MoonDao() {
        super("MOON_TABLE");
    }

    @Override
    public List<Relation<Moon, ?>> getOneToOne() {
        return List.of(name, maxT, fields);
    }

    @Override
    public List<Relation<Moon, ?>> getOneToMany() {
        return List.of(defences, ships);
    }

    @Override
    public Moon getData(Result<Moon> result) throws PersistenceException {
        final String name = result.get(this.name);
        final Integer maxT = result.get(this.maxT);
        final Integer fields = result.get(this.fields);

        final Collection<DefenceUnit> defences = result.get(this.defences);
        final Collection<Ships> ships = result.get(this.ships);
        final Collection<Building> buildings = result.get(this.buildings);

        return new MoonBuilder()
                .setName(name)
                .setMaxT(maxT)
                .setFields(fields)
                .setDefences(defences)
                .setShips(ships)
                .setBuildings(buildings)
                .createMoon();
    }
}
