package com.example.rpg.model;

/** Result of a battle. An enum because the set of outcomes is fixed. */
public enum BattleOutcome {
    HEROES_WIN("Heroes win!"),
    MONSTERS_WIN("Monsters win!"),
    DRAW("Draw: the turn limit was reached."),
    IN_PROGRESS("The battle is still in progress.");

    private final String description;

    BattleOutcome(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
