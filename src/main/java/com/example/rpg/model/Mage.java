package com.example.rpg.model;

import com.example.rpg.exception.CharacterDeadException;
import com.example.rpg.exception.InsufficientManaException;

import java.util.Objects;

/** Spellcaster that spends mana to heal allies. */
public class Mage extends Character implements Healable {

    static final int HEAL_COST = 20;
    static final int HEAL_AMOUNT = 25;
    private static final int MANA_GAIN_PER_LEVEL = 10;

    private int mana;

    public Mage(CharacterStats stats) {
        super(stats);
        if (stats.mana() < 0) {
            throw new IllegalArgumentException("Mana must not be negative");
        }
        this.mana = stats.mana();
    }

    @Override
    public String getTypeLabel() {
        return "Mage";
    }

    public int getMana() {
        return mana;
    }

    @Override
    public boolean canHeal() {
        return isAlive() && mana >= HEAL_COST;
    }

    @Override
    public void healAlly(Character ally) {
        Objects.requireNonNull(ally, "ally must not be null");
        if (!isAlive()) {
            throw new CharacterDeadException(getName());
        }
        if (!ally.isAlive()) {
            throw new CharacterDeadException(ally.getName());
        }
        if (mana < HEAL_COST) {
            throw new InsufficientManaException(HEAL_COST, mana);
        }
        mana -= HEAL_COST;
        ally.receiveHealing(HEAL_AMOUNT);
    }

    @Override
    protected void onLevelUp() {
        growStats(8, 1, 1, 0);
        mana += MANA_GAIN_PER_LEVEL;
    }
}
