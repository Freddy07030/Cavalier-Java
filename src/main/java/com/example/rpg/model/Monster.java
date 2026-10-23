package com.example.rpg.model;

/** Enemy with no special ability. Uses the default rules of {@link Character}. */
public class Monster extends Character {

    public Monster(CharacterStats stats) {
        super(stats);
    }

    @Override
    public String getTypeLabel() {
        return "Monster";
    }

    @Override
    protected void onLevelUp() {
        growStats(5, 1, 1, 1);
    }
}
