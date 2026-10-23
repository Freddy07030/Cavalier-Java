package com.example.rpg.service;

import com.example.rpg.model.CharacterRecord;
import com.example.rpg.model.Team;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Turns the stored records into fresh teams of live Character objects.
 * Called before each battle so that characters start with full HP.
 */
public class RosterBuilder {

    private final CharacterFactory factory;

    public RosterBuilder(CharacterFactory factory) {
        this.factory = Objects.requireNonNull(factory, "factory must not be null");
    }

    public Map<String, Team> buildTeams(List<CharacterRecord> records) {
        Map<String, Team> teams = new LinkedHashMap<>();
        for (CharacterRecord record : records) {
            teams.computeIfAbsent(record.teamName(), Team::new)
                    .add(factory.create(record.type(), record.stats()));
        }
        return teams;
    }
}
