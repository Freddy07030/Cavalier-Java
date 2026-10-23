package com.example.rpg.model;

import com.example.rpg.exception.CharacterDeadException;
import com.example.rpg.exception.InsufficientManaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MageTest {

    private static Mage mage(int mana) {
        return new Mage(new CharacterStats("Lyra", 1, 80, 6, 6, 10, mana, 0));
    }

    private static Warrior wounded(int damage) {
        Warrior ally = new Warrior(new CharacterStats("Aldric", 1, 100, 18, 15, 8, 0, 0));
        ally.takeDamage(damage);
        return ally;
    }

    @Test
    void healRestoresHpAndSpendsMana() {
        Mage mage = mage(60);
        Warrior ally = wounded(40);

        mage.healAlly(ally);

        assertEquals(85, ally.getCurrentHp());
        assertEquals(40, mage.getMana());
    }

    @Test
    void healNeverExceedsMaxHp() {
        Mage mage = mage(60);
        Warrior ally = wounded(10);

        mage.healAlly(ally);

        assertEquals(100, ally.getCurrentHp());
    }

    @Test
    void healWithoutEnoughManaThrowsAndCannotHeal() {
        Mage mage = mage(10);
        Warrior ally = wounded(40);

        assertFalse(mage.canHeal());
        InsufficientManaException ex = assertThrows(InsufficientManaException.class,
                () -> mage.healAlly(ally));
        assertEquals(20, ex.getRequired());
        assertEquals(10, ex.getAvailable());
        assertEquals(60, ally.getCurrentHp());
    }

    @Test
    void healingDeadAllyThrows() {
        Mage mage = mage(60);
        Warrior ally = wounded(1000);

        assertThrows(CharacterDeadException.class, () -> mage.healAlly(ally));
    }

    @Test
    void mageWithEnoughManaCanHeal() {
        assertTrue(mage(20).canHeal());
    }
}
