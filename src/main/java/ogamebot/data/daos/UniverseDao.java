package ogamebot.data.daos;

import gorgon.external.*;
import ogamebot.comp.Player;
import ogamebot.comp.Universe;
import ogamebot.comp.UniverseBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static gorgon.external.Modifier.NOT_NULL;
import static gorgon.external.Ratio.ONE_TO_ONE;
import static gorgon.external.Type.DOUBLE;

/**
 *
 */
public class UniverseDao extends DataTable<Universe> {
    private Relation<Universe, Double> defToTf = Relate.build(ONE_TO_ONE, "defToTF", DOUBLE, Universe::getDefTf, NOT_NULL);
    private Relation<Universe, Double> fleetToTf = Relate.build(ONE_TO_ONE, "fleetToTf", DOUBLE, Universe::getFleetTf, NOT_NULL);
    private Relation<Universe, Double> flightSpeed = Relate.build(ONE_TO_ONE, "flightSpeed", DOUBLE, Universe::getFlightSpeed, NOT_NULL);
    private Relation<Universe, Double> economySpeed = Relate.build(ONE_TO_ONE, "economySpeed", DOUBLE, Universe::getEconomySpeed, NOT_NULL);

    private Relation<Universe, Boolean> donut = Relate.build(ONE_TO_ONE, "donut", Type.BOOLEAN, Universe::isDonut, NOT_NULL);
    private Relation<Universe, Boolean> rapidFire = Relate.build(ONE_TO_ONE, "rapidFire", Type.BOOLEAN, Universe::isRapidFire, NOT_NULL);

    private Relation<Universe, Boolean> expedEnabled = Relate.build(ONE_TO_ONE, "expeditionEnabled", Type.BOOLEAN, Universe::isExpeditionEnabled, NOT_NULL);
    private Relation<Universe, Boolean> aksEnabled = Relate.build(ONE_TO_ONE, "aksEnabled", Type.BOOLEAN, Universe::isAks_Enabled, NOT_NULL);
    private Relation<Universe, Boolean> wreckageEnabled = Relate.build(ONE_TO_ONE, "wreckageEnabled", Type.BOOLEAN, Universe::isWreckage_Enabled, NOT_NULL);
    private Relation<Universe, Boolean> jumpGateEnabled = Relate.build(ONE_TO_ONE, "jumpGateEnabled", Type.BOOLEAN, Universe::isJumpGate_Enabled, NOT_NULL);

    private Relation<Universe, Integer> bonusFields = Relate.build(ONE_TO_ONE, "bonusFields", Type.INTEGER, Universe::getBonusFields, NOT_NULL);
    private Relation<Universe, Integer> dmGift = Relate.build(ONE_TO_ONE, "dmGift", Type.INTEGER, Universe::getdM_sign_Up_gift, NOT_NULL);
    private Relation<Universe, Integer> jumpGateMinDur = Relate.build(ONE_TO_ONE, "gateMinDuration", Type.INTEGER, Universe::getJumpGate_minimum_duration);
    private Relation<Universe, Integer> jumpGateBasicDur = Relate.build(ONE_TO_ONE, "gateBasicDuration", Type.INTEGER, Universe::getJumpGate_basic_duration, NOT_NULL);
    private Relation<Universe, Integer> max_galaxy = Relate.build(ONE_TO_ONE, "maxGalaxies", Type.INTEGER, Universe::getGalaxies_max, NOT_NULL);
    private Relation<Universe, Integer> maxSystem = Relate.build(ONE_TO_ONE, "maxSystem", Type.INTEGER, Universe::getSystems_max, NOT_NULL);
    private Relation<Universe, Integer> maxPlanets = Relate.build(ONE_TO_ONE, "maxPlanets", Type.INTEGER, Universe::getPlanets_max, NOT_NULL);
    private Relation<Universe, Integer> espionageRaids = Relate.build(ONE_TO_ONE, "espionageRaids", Type.INTEGER, Universe::getEspionage_raids, NOT_NULL);

    private Relation<Universe, Integer> nameId = Relate.build(ONE_TO_ONE, "universeId", Type.INTEGER, Universe::getNameId, NOT_NULL);
    private Relation<Universe, String> language = Relate.build(ONE_TO_ONE, "language", Type.TEXT, Universe::getLanguage, NOT_NULL);
    private Relation<Universe, String> name = Relate.build(ONE_TO_ONE, "name", Type.TEXT, Universe::getName, NOT_NULL);

    private Relation<Universe, Collection<Player>> player = Relate.build(Ratio.ONE_TO_MANY, Player.class, Type.ID, Universe::getPlayers, NOT_NULL);

    protected UniverseDao() {
        super("UNIVERSE_TABLE");
    }

    @Override
    public List<Relation<Universe, ?>> getOneToOne() {
        List<Relation<Universe, ?>> relations = new ArrayList<>();
        relations.add(defToTf);
        relations.add(fleetToTf);
        relations.add(flightSpeed);
        relations.add(economySpeed);
        relations.add(donut);
        relations.add(rapidFire);
        relations.add(expedEnabled);
        relations.add(aksEnabled);
        relations.add(wreckageEnabled);
        relations.add(jumpGateEnabled);
        relations.add(bonusFields);
        relations.add(dmGift);
        relations.add(jumpGateMinDur);
        relations.add(jumpGateBasicDur);
        relations.add(max_galaxy);
        relations.add(maxSystem);
        relations.add(maxPlanets);
        relations.add(espionageRaids);
        relations.add(nameId);
        relations.add(language);
        relations.add(name);
        return relations;
    }

    @Override
    public List<Relation<Universe, ?>> getOneToMany() {
        return List.of(player);
    }

    @Override
    public Universe getData(Result<Universe> result) throws PersistenceException {
        final Double defToTf = result.get(this.defToTf);
        final Double fleetToTf = result.get(this.fleetToTf);
        final Double flightSpeed = result.get(this.flightSpeed);
        final Double economySpeed = result.get(this.economySpeed);

        final Boolean donut = result.get(this.donut);
        final Boolean rapidFire = result.get(this.rapidFire);
        final Boolean expedEnabled = result.get(this.expedEnabled);
        final Boolean aksEnabled = result.get(this.aksEnabled);
        final Boolean wreckageEnabled = result.get(this.wreckageEnabled);
        final Boolean jumpGateEnabled = result.get(this.jumpGateEnabled);

        final Integer bonusFields = result.get(this.bonusFields);
        final Integer dmGift = result.get(this.dmGift);
        final Integer jumpGateMinDur = result.get(this.jumpGateMinDur);
        final Integer jumpGateBasicDur = result.get(this.jumpGateBasicDur);
        final Integer max_galaxy = result.get(this.max_galaxy);
        final Integer maxSystem = result.get(this.maxSystem);
        final Integer maxPlanets = result.get(this.maxPlanets);
        final Integer espionageRaids = result.get(this.espionageRaids);
        final Integer nameId = result.get(this.nameId);

        final String language = result.get(this.language);
        final String name = result.get(this.name);

        final Collection<Player> players = result.get(player);

        final Universe universe =
                new UniverseBuilder()
                        .setDefTf(defToTf)
                        .setFleetTf(fleetToTf)
                        .setFlightSpeed(flightSpeed)
                        .setEconomySpeed(economySpeed)
                        .setDonut(donut)
                        .setRapidFire(rapidFire)
                        .setBonusFields(bonusFields)
                        .setName(name)
                        .setPlayers(players)
                        .setAks_Enabled(aksEnabled)
                        .setExpeditionEnabled(expedEnabled)
                        .setWreckage_Enabled(wreckageEnabled)
                        .setJumpGate_Enabled(jumpGateEnabled)
                        .setJumpGate_minimum_duration(jumpGateMinDur)
                        .setJumpGate_basic_duration(jumpGateBasicDur)
                        .setdM_sign_Up_gift(dmGift)
                        .setGalaxies_max(max_galaxy)
                        .setSystems_max(maxSystem)
                        .setPlanets_max(maxPlanets)
                        .setEspionage_raids(espionageRaids)
                        .setNameId(nameId)
                        .setLanguage(language)
                        .createUniverse();

        players.forEach(player -> player.setUniverse(universe));
        return universe;
    }
}
