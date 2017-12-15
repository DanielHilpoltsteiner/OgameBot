package ogamebot.data.daos;

import gorgon.external.*;
import ogamebot.comp.Player;
import ogamebot.comp.PlayerBuilder;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.research.Research;

import java.util.Collection;
import java.util.List;

import static gorgon.external.Modifier.NOT_NULL;
import static gorgon.external.Ratio.ONE_TO_MANY;
import static gorgon.external.Ratio.ONE_TO_ONE;
import static gorgon.external.Type.*;

/**
 *
 */
public class PlayerDao extends DataTable<Player> {
    private Relation<Player, Integer> points = Relate.build(ONE_TO_ONE, "points", INTEGER, Player::getPoints, NOT_NULL);
    private Relation<Player, Integer> highscore = Relate.build(ONE_TO_ONE, "highscore", INTEGER, Player::getPlayerPlace, NOT_NULL);
    private Relation<Player, Integer> darkMatter = Relate.build(ONE_TO_ONE, "darkMatter", INTEGER, Player::getDarkMatter, NOT_NULL);
    private Relation<Player, String> name = Relate.build(ONE_TO_ONE, "name", TEXT, Player::getName, NOT_NULL);

    private Relation<Player, Boolean> commander = Relate.build(ONE_TO_ONE, "commander", BOOLEAN, Player::hasCommander, NOT_NULL);
    private Relation<Player, Boolean> admiral = Relate.build(ONE_TO_ONE, "admiral", BOOLEAN, Player::hasAdmiral, NOT_NULL);
    private Relation<Player, Boolean> engineer = Relate.build(ONE_TO_ONE, "engineer", BOOLEAN, Player::hasEngineer, NOT_NULL);
    private Relation<Player, Boolean> technocrat = Relate.build(ONE_TO_ONE, "technocrat", BOOLEAN, Player::hasTechnocrat, NOT_NULL);
    private Relation<Player, Boolean> geologist = Relate.build(ONE_TO_ONE, "geologist", BOOLEAN, Player::hasGeologist, NOT_NULL);

    private Relation<Player, Collection<Research>> research = Relate.build(ONE_TO_MANY, Research.class, Type.ID, Player::getResearches, NOT_NULL);
    private Relation<Player, Collection<Planet>> planets = Relate.build(ONE_TO_MANY, Planet.class, Type.ID, Player::getPlanets, NOT_NULL);


    protected PlayerDao() {
        super("PLAYER_TABLE");
    }

    @Override
    public List<Relation<Player, ?>> getOneToOne() {
        return List.of(points, highscore, darkMatter, name, commander, admiral, engineer, technocrat, geologist);
    }

    @Override
    public List<Relation<Player, ?>> getOneToMany() {
        return List.of(research, planets);
    }

    @Override
    public Player getData(Result<Player> result) throws PersistenceException {
        final Integer points = result.get(this.points);
        final Integer highscore = result.get(this.highscore);
        final Integer darkMatter = result.get(this.darkMatter);
        final String name = result.get(this.name);
        final Boolean commander = result.get(this.commander);
        final Boolean admiral = result.get(this.admiral);
        final Boolean engineer = result.get(this.engineer);
        final Boolean technocrat = result.get(this.technocrat);
        final Boolean geologist = result.get(this.geologist);
        final Collection<Research> research = result.get(this.research);
        final Collection<Planet> planets = result.get(this.planets);

        return new PlayerBuilder()
                .setHighscore(highscore)
                .setPoints(points)
                .setDarkMatter(darkMatter)
                .setName(name)
                .setAdmiral(admiral)
                .setTechnocrat(technocrat)
                .setEngineer(engineer)
                .setCommander(commander)
                .setGeologist(geologist)
                .setResearch(research)
                .setPlanets(planets)
                .createPlayer();
    }
}
