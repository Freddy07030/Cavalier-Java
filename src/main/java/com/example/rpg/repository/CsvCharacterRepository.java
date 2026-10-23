package com.example.rpg.repository;

import com.example.rpg.exception.DataLoadException;
import com.example.rpg.model.CharacterRecord;
import com.example.rpg.model.CharacterStats;
import com.example.rpg.model.Team;
import com.example.rpg.service.CharacterFactory;
import com.example.rpg.utils.CsvParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Loads characters from CSV once, at construction time. Every row is validated, so a bad file
 * fails at startup with the line number, not in the middle of a battle.
 */
public class CsvCharacterRepository implements CharacterRepository {

    static final int EXPECTED_COLUMNS = 10;

    private final List<CharacterRecord> records;

    public CsvCharacterRepository(Reader reader, CharacterFactory factory) throws DataLoadException {
        Objects.requireNonNull(reader, "reader must not be null");
        Objects.requireNonNull(factory, "factory must not be null");
        this.records = List.copyOf(load(reader, factory));
    }

    @Override
    public List<CharacterRecord> findAll() {
        return records;
    }

    private static List<CharacterRecord> load(Reader reader, CharacterFactory factory) throws DataLoadException {
        List<CharacterRecord> result = new ArrayList<>();
        Set<String> teamNames = new HashSet<>();

        try (BufferedReader buffered = new BufferedReader(reader)) {
            String line;
            int lineNumber = 0;
            while ((line = buffered.readLine()) != null) {
                lineNumber++;
                // Line 1 is the header; blank lines are ignored.
                if (lineNumber == 1 || line.isBlank()) {
                    continue;
                }
                CharacterRecord record = parseLine(line, lineNumber, factory);
                teamNames.add(record.teamName());
                result.add(record);
            }
        } catch (IOException e) {
            throw new DataLoadException("Cannot read character data: " + e.getMessage(), e);
        }

        if (result.isEmpty()) {
            throw new DataLoadException("The data file contains no characters");
        }
        for (String required : List.of(Team.HEROES, Team.MONSTERS)) {
            if (!teamNames.contains(required)) {
                throw new DataLoadException("The data file must contain at least one " + required + " row");
            }
        }
        return result;
    }

    private static CharacterRecord parseLine(String line, int lineNumber, CharacterFactory factory)
            throws DataLoadException {
        String[] fields = CsvParser.split(line);
        if (fields.length != EXPECTED_COLUMNS) {
            throw rowError(lineNumber, "expected " + EXPECTED_COLUMNS + " columns but found " + fields.length);
        }
        try {
            String type = fields[0].toUpperCase();
            String team = fields[9].toUpperCase();
            if (!team.equals(Team.HEROES) && !team.equals(Team.MONSTERS)) {
                throw new IllegalArgumentException("unknown team '" + fields[9] + "'");
            }
            CharacterStats stats = new CharacterStats(
                    fields[1],
                    Integer.parseInt(fields[2]),
                    Integer.parseInt(fields[3]),
                    Integer.parseInt(fields[4]),
                    Integer.parseInt(fields[5]),
                    Integer.parseInt(fields[6]),
                    Integer.parseInt(fields[7]),
                    Integer.parseInt(fields[8]));
            // Building the object here checks the type and the stat rules once, at load time.
            factory.create(type, stats);
            return new CharacterRecord(type, team, stats);
        } catch (IllegalArgumentException e) {
            // NumberFormatException is a subclass, so non-numeric stats land here too.
            throw rowError(lineNumber, e.getMessage(), e);
        }
    }

    private static DataLoadException rowError(int lineNumber, String reason) {
        return new DataLoadException("Invalid data at line " + lineNumber + ": " + reason);
    }

    private static DataLoadException rowError(int lineNumber, String reason, Throwable cause) {
        return new DataLoadException("Invalid data at line " + lineNumber + ": " + reason, cause);
    }
}
