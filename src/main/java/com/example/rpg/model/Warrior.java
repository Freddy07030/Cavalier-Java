package com.example.rpg.model;

/** Sturdy melee fighter. Enters "Rage" when wounded, which boosts its attack. */
public class Warrior extends Character {

    static final double RAGE_HP_THRESHOLD = 0.3;
    static final double RAGE_MULTIPLIER = 1.5;

    public Warrior(CharacterStats stats) {
        super(stats);
    }

    @Override
    public String getTypeLabel() {
        return "Warrior";
    }

    @Override
    public int getAttackPower() {
        int base = super.getAttackPower();
        return isRaging() ? (int) Math.round(base * RAGE_MULTIPLIER) : base;
    }

    public boolean isRaging() {
        return isAlive() && getHpRatio() < RAGE_HP_THRESHOLD;
    }

    @Override
    protected void onLevelUp() {
        growStats(15, 3, 3, 0);
    }
}
