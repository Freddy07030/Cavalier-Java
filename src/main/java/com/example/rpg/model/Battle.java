package com.example.rpg.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * State of one fight between two teams: who is fighting, the turn counter and the event log.
 * It holds state only. The rules that advance it live in {@code BattleService}.
 */
public class Battle {

    private final Team heroes;
    private final Team monsters;
    private final int maxTurns;
    private final List<String> log = new ArrayList<>();
    private int turn;

    public Battle(Team heroes, Team monsters, int maxTurns) {
        this.heroes = Objects.requireNonNull(heroes, "heroes must not be null");
        this.monsters = Objects.requireNonNull(monsters, "monsters must not be null");
        if (heroes == monsters) {
            throw new IllegalArgumentException("A battle needs two different teams");
        }
        if (maxTurns <= 0) {
            throw new IllegalArgumentException("maxTurns must be positive");
        }
        this.maxTurns = maxTurns;
    }

    public void nextTurn() {
        turn++;
    }

    public boolean isFinished() {
        return heroes.isDefeated() || monsters.isDefeated() || turn >= maxTurns;
    }

    public BattleOutcome getOutcome() {
        if (!isFinished()) {
            return BattleOutcome.IN_PROGRESS;
        }
        if (monsters.isDefeated()) {
            return BattleOutcome.HEROES_WIN;
        }
        if (heroes.isDefeated()) {
            return BattleOutcome.MONSTERS_WIN;
        }
        return BattleOutcome.DRAW;
    }

    public Team teamOf(Character character) {
        if (heroes.contains(character)) {
            return heroes;
        }
        if (monsters.contains(character)) {
            return monsters;
        }
        throw new IllegalArgumentException(character.getName() + " is not taking part in this battle");
    }

    public Team opposingTeamOf(Character character) {
        return teamOf(character) == heroes ? monsters : heroes;
    }

    public void log(String message) {
        log.add(message);
    }

    public List<String> getLog() {
        return Collections.unmodifiableList(log);
    }

    public Team getHeroes() {
        return heroes;
    }

    public Team getMonsters() {
        return monsters;
    }

    public int getTurn() {
        return turn;
    }

    public int getMaxTurns() {
        return maxTurns;
    }
}
