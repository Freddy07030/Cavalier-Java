package com.example.rpg.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CharacterTest {

    private static CharacterStats stats(int hp, int attack, int defense, int speed) {
        return new CharacterStats("Test", 1, hp, attack, defense, speed, 0, 0);
    }

    @Test
    void takeDamageNeverDropsHpBelowZero() {
        Monster monster = new Monster(stats(10, 5, 5, 5));

        monster.takeDamage(50);

        assertEquals(0, monster.getCurrentHp());
        assertFalse(monster.isAlive());
    }

    @Test
    void negativeDamageIsRejected() {
        Monster monster = new Monster(stats(10, 5, 5, 5));

        assertThrows(IllegalArgumentException.class, () -> monster.takeDamage(-1));
    }

    @Test
    void healingIsCappedAtMaxHp() {
        Monster monster = new Monster(stats(50, 5, 5, 5));
        monster.takeDamage(10);

        monster.receiveHealing(100);

        assertEquals(50, monster.getCurrentHp());
    }

    @Test
    void constructorRejectsBlankName() {
        CharacterStats blankName = new CharacterStats("  ", 1, 10, 1, 1, 1, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> new Monster(blankName));
    }

    @Test
    void constructorRejectsNonPositiveHp() {
        assertThrows(IllegalArgumentException.class, () -> new Monster(stats(0, 1, 1, 1)));
    }

    @Test
    void gainingEnoughXpLevelsUpAndGrowsStats() {
        Warrior warrior = new Warrior(stats(100, 10, 10, 5));

        int levelsGained = warrior.gainExperience(120);

        assertEquals(1, levelsGained);
        assertEquals(2, warrior.getLevel());
        assertEquals(20, warrior.getExperience());
        assertEquals(115, warrior.getMaxHp());
        assertEquals(13, warrior.getBaseAttack());
    }

    @Test
    void gainingLessXpThanNeededDoesNotLevelUp() {
        Monster monster = new Monster(stats(10, 1, 1, 1));

        assertEquals(0, monster.gainExperience(99));
        assertEquals(1, monster.getLevel());
    }

    @Test
    void warriorRagesBelowThirtyPercentHp() {
        Warrior warrior = new Warrior(stats(100, 20, 10, 5));
        assertEquals(20, warrior.getAttackPower());

        warrior.takeDamage(75);

        assertTrue(warrior.isRaging());
        assertEquals(30, warrior.getAttackPower());
    }

    @Test
    void warriorDoesNotRageAboveThreshold() {
        Warrior warrior = new Warrior(stats(100, 20, 10, 5));

        warrior.takeDamage(50);

        assertFalse(warrior.isRaging());
        assertEquals(20, warrior.getAttackPower());
    }

    @Test
    void archerHasHigherCriticalChanceThanDefaultCharacter() {
        Archer archer = new Archer(stats(90, 10, 5, 12));
        Monster monster = new Monster(stats(90, 10, 5, 12));

        assertEquals(0.25, archer.getCriticalChance(), 0.0001);
        assertEquals(0.05, monster.getCriticalChance(), 0.0001);
    }
}
