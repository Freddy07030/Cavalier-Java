package com.example.rpg.model;

/**
 * One row of the data file, before any Character object exists.
 * Keeping the raw data lets every battle build fresh characters with full HP.
 */
public record CharacterRecord(String type, String teamName, CharacterStats stats) {
}
