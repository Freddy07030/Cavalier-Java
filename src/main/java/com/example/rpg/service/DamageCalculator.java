package com.example.rpg.service;

import com.example.rpg.model.Character;
import com.example.rpg.utils.RandomSource;

import java.util.Objects;

/** Rolls for critical hits and applies the damage formula. */
public class DamageCalculator {

    private final DamageFormula formula;
    private final RandomSource random;

    public DamageCalculator(DamageFormula formula, RandomSource random) {
        this.formula = Objects.requireNonNull(formula, "formula must not be null");
        this.random = Objects.requireNonNull(random, "random must not be null");
    }

    public DamageResult calculate(Character attacker, Character target) {
        boolean critical = random.nextDouble() < attacker.getCriticalChance();
        int amount = formula.compute(attacker.getAttackPower(), target.getDefense(), critical);
        return new DamageResult(amount, critical);
    }
}
