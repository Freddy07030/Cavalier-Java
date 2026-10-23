package com.example.rpg.model;

/** Fast ranged fighter with frequent critical hits. */
public class Archer extends Character {

    static final double CRITICAL_CHANCE = 0.25;

    public Archer(CharacterStats stats) {
        super(stats);
    }

    @Override
    public String getTypeLabel() {
        return "Archer";
    }

    @Override
    public double getCriticalChance() {
        return CRITICAL_CHANCE;
    }

    @Override
    protected void onLevelUp() {
        growStats(8, 2, 1, 2);
    }
}
