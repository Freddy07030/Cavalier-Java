package com.example.rpg.service;

import com.example.rpg.exception.CharacterDeadException;
import com.example.rpg.exception.InvalidActionException;
import com.example.rpg.model.Battle;
import com.example.rpg.model.BattleOutcome;
import com.example.rpg.model.Character;
import com.example.rpg.model.CharacterStats;
import com.example.rpg.model.Monster;
import com.example.rpg.model.Team;
import com.example.rpg.model.Warrior;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BattleServiceTest {

    // Roll of 0.99 means no critical hits, so the outcomes are predictable.
    private final BattleService service = new BattleService(
            new DamageCalculator(new StandardDamageFormula(), () -> 0.99),
            new TurnOrderService());

    private static Team team(String name, Character... members) {
        Team team = new Team(name);
        for (Character member : members) {
            team.add(member);
        }
        return team;
    }

    @Test
    void heroesWinAgainstAWeakMonster() {
        Team heroes = team(Team.HEROES,
                new Warrior(new CharacterStats("Aldric", 1, 100, 20, 10, 10, 0, 0)));
        Team monsters = team(Team.MONSTERS,
                new Monster(new CharacterStats("Slime", 1, 10, 1, 1, 1, 0, 0)));
        Battle battle = new Battle(heroes, monsters, 50);

        BattleOutcome outcome = service.run(battle);

        assertEquals(BattleOutcome.HEROES_WIN, outcome);
        assertTrue(monsters.isDefeated());
        assertTrue(battle.getLog().stream().anyMatch(line -> line.contains("Slime is defeated")));
    }

    @Test
    void battleIsADrawWhenTurnLimitIsReached() {
        // Each side deals 1 damage per action and has 100 HP, so both survive 50 turns.
        Team heroes = team(Team.HEROES,
                new Monster(new CharacterStats("A", 1, 100, 10, 10, 5, 0, 0)));
        Team monsters = team(Team.MONSTERS,
                new Monster(new CharacterStats("B", 1, 100, 10, 10, 5, 0, 0)));
        Battle battle = new Battle(heroes, monsters, 50);

        assertEquals(BattleOutcome.DRAW, service.run(battle));
        assertEquals(50, battle.getTurn());
        assertFalse(heroes.isDefeated());
        assertFalse(monsters.isDefeated());
    }

    @Test
    void attackingAnAllyIsRejected() {
        Monster hero1 = new Monster(new CharacterStats("H1", 1, 50, 5, 5, 5, 0, 0));
        Monster hero2 = new Monster(new CharacterStats("H2", 1, 50, 5, 5, 5, 0, 0));
        Monster enemy = new Monster(new CharacterStats("E", 1, 50, 5, 5, 5, 0, 0));
        Battle battle = new Battle(team(Team.HEROES, hero1, hero2), team(Team.MONSTERS, enemy), 10);

        assertThrows(InvalidActionException.class, () -> service.attack(battle, hero1, hero2));
    }

    @Test
    void deadAttackerCannotAct() {
        Monster hero = new Monster(new CharacterStats("H", 1, 50, 5, 5, 5, 0, 0));
        Monster enemy = new Monster(new CharacterStats("E", 1, 50, 5, 5, 5, 0, 0));
        Battle battle = new Battle(team(Team.HEROES, hero), team(Team.MONSTERS, enemy), 10);
        hero.takeDamage(1000);

        assertThrows(CharacterDeadException.class, () -> service.attack(battle, hero, enemy));
    }

    @Test
    void battleRequiresTwoDifferentTeams() {
        Team team = team(Team.HEROES);

        assertThrows(IllegalArgumentException.class, () -> new Battle(team, team, 10));
    }
}
