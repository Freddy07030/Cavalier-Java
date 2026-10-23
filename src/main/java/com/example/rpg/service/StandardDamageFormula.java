package com.example.rpg.service;

/**
 * Damage = attack - defense, with a floor of 1 so that no attack is ever wasted.
 * A critical hit doubles the result.
 */
public class StandardDamageFormula implements DamageFormula {

    static final int MINIMUM_DAMAGE = 1;
    static final int CRITICAL_MULTIPLIER = 2;

    @Override
    public int compute(int attackPower, int defense, boolean critical) {
        int base = Math.max(MINIMUM_DAMAGE, attackPower - defense);
        return critical ? base * CRITICAL_MULTIPLIER : base;
    }
}
