package com.example.rpg.service;

import com.example.rpg.model.Archer;
import com.example.rpg.model.CharacterStats;
import com.example.rpg.model.Monster;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DamageCalculatorTest {

    private final DamageCalculator noCritical = new DamageCalculator(new StandardDamageFormula(), () -> 0.99);

    @Test
    void damageIsAttackMinusDefense() {
        Monster attacker = new Monster(new CharacterStats("A", 1, 50, 12, 5, 5, 0, 0));
        Monster target = new Monster(new CharacterStats("B", 1, 50, 5, 4, 5, 0, 0));

        DamageResult result = noCritical.calculate(attacker, target);

        assertEquals(8, result.amount());
        assertFalse(result.critical());
    }

    @Test
    void damageIsAtLeastOneWhenDefenseIsHigher() {
        Monster attacker = new Monster(new CharacterStats("A", 1, 50, 1, 5, 5, 0, 0));
        Monster target = new Monster(new CharacterStats("B", 1, 50, 5, 100, 5, 0, 0));

        assertEquals(1, noCritical.calculate(attacker, target).amount());
    }

    @Test
    void criticalHitDoublesDamage() {
        DamageCalculator alwaysCritical = new DamageCalculator(new StandardDamageFormula(), () -> 0.0);
        Archer archer = new Archer(new CharacterStats("Sylvan", 1, 95, 14, 8, 14, 0, 0));
        Monster target = new Monster(new CharacterStats("Goblin", 1, 60, 10, 5, 9, 0, 0));

        DamageResult result = alwaysCritical.calculate(archer, target);

        assertTrue(result.critical());
        assertEquals(18, result.amount());
    }

    @Test
    void noCriticalWhenRollIsAboveChance() {
        DamageCalculator roll90 = new DamageCalculator(new StandardDamageFormula(), () -> 0.9);
        Archer archer = new Archer(new CharacterStats("Sylvan", 1, 95, 14, 8, 14, 0, 0));
        Monster target = new Monster(new CharacterStats("Goblin", 1, 60, 10, 5, 9, 0, 0));

        DamageResult result = roll90.calculate(archer, target);

        assertFalse(result.critical());
        assertEquals(9, result.amount());
    }
}
