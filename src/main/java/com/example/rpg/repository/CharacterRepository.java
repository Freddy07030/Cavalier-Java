package com.example.rpg.repository;

import com.example.rpg.model.CharacterRecord;

import java.util.List;

/**
 * Source of character data. The game depends on this interface, not on CSV (Dependency Inversion),
 * so a JSON or database version could be added without changing the game code.
 */
public interface CharacterRepository {

    List<CharacterRecord> findAll();
}
