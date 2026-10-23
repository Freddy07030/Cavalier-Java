package com.example.rpg.model;

/**
 * A weapon owned by a character. Modelled as a separate object (composition) rather than a
 * subclass, because a weapon is something a character has, not something a character is.
 */
public final class Weapon {

    public static final Weapon NONE = new Weapon("Bare hands", 0);

    private final String name;
    private final int attackBonus;

    public Weapon(String name, int attackBonus) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Weapon name must not be blank");
        }
        if (attackBonus < 0) {
            throw new IllegalArgumentException("Weapon attack bonus must not be negative");
        }
        this.name = name;
        this.attackBonus = attackBonus;
    }

    public String getName() {
        return name;
    }

    public int getAttackBonus() {
        return attackBonus;
    }
}
