package com.example.rpg.model;

/**
 * Capability of characters that can restore another character's HP.
 *
 * An interface rather than a method on {@link Character}: only some classes heal, and a warrior
 * should not expose a heal method it cannot use (Interface Segregation).
 */
public interface Healable {

    /** True when the healer currently has the resources (mana, life) to cast a heal. */
    boolean canHeal();

    void healAlly(Character ally);
}
