package com.example.rpg.service;

import com.example.rpg.model.Archer;
import com.example.rpg.model.Character;
import com.example.rpg.model.CharacterStats;
import com.example.rpg.model.Mage;
import com.example.rpg.model.Monster;
import com.example.rpg.model.Warrior;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Builds the right subclass from a type name in the data file.
 * A lookup table instead of a switch: adding a class means adding one entry here.
 */
public class CharacterFactory {

    private static final Map<String, Function<CharacterStats, Character>> CREATORS = Map.of(
            "WARRIOR", Warrior::new,
            "MAGE", Mage::new,
            "ARCHER", Archer::new,
            "MONSTER", Monster::new);

    public Character create(String type, CharacterStats stats) {
        Objects.requireNonNull(type, "type must not be null");
        Function<CharacterStats, Character> creator = CREATORS.get(type.toUpperCase(Locale.ROOT));
        if (creator == null) {
            throw new IllegalArgumentException("Unknown character type: " + type);
        }
        return creator.apply(stats);
    }
}
