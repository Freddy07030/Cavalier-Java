package com.example.rpg.model;

/**
 * Immutable bundle of the raw numbers that describe a character.
 * A record keeps the constructors of the character classes short and the data immutable.
 */
public record CharacterStats(
        String name,
        int level,
        int hp,
        int attack,
        int defense,
        int speed,
        int mana,
        int weaponBonus) {
}
