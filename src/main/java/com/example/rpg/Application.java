package com.example.rpg;

import com.example.rpg.exception.DataLoadException;
import com.example.rpg.model.CharacterRecord;
import com.example.rpg.repository.CharacterRepository;
import com.example.rpg.repository.CsvCharacterRepository;
import com.example.rpg.service.BattleService;
import com.example.rpg.service.CharacterFactory;
import com.example.rpg.service.DamageCalculator;
import com.example.rpg.service.ExperienceService;
import com.example.rpg.service.RosterBuilder;
import com.example.rpg.service.StandardDamageFormula;
import com.example.rpg.service.TurnOrderService;
import com.example.rpg.ui.BattleView;
import com.example.rpg.ui.ConsoleMenu;
import com.example.rpg.utils.JavaRandomSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point. Only wires the objects together and starts the menu; no game logic lives here.
 * Usage: {@code java -cp target/classes com.example.rpg.Application [path/to/characters.csv]}.
 * Without an argument, the bundled resource file is used.
 */
public class Application {

    private static final String DEFAULT_DATA_RESOURCE = "/characters.csv";

    public static void main(String[] args) {
        try {
            CharacterFactory factory = new CharacterFactory();
            CharacterRepository repository = openRepository(args, factory);
            List<CharacterRecord> records = repository.findAll();

            new ConsoleMenu(
                    records,
                    new RosterBuilder(factory),
                    new BattleService(
                            new DamageCalculator(new StandardDamageFormula(), new JavaRandomSource()),
                            new TurnOrderService()),
                    new ExperienceService(),
                    new BattleView(),
                    new Scanner(System.in)
            ).run();
        } catch (DataLoadException | IOException e) {
            System.err.println("Could not load character data: " + e.getMessage());
            System.exit(1);
        }
    }

    private static CharacterRepository openRepository(String[] args, CharacterFactory factory)
            throws DataLoadException, IOException {
        if (args.length > 0) {
            Reader reader = Files.newBufferedReader(Path.of(args[0]), StandardCharsets.UTF_8);
            return new CsvCharacterRepository(reader, factory);
        }
        InputStream resource = Application.class.getResourceAsStream(DEFAULT_DATA_RESOURCE);
        if (resource == null) {
            throw new DataLoadException("Default data file " + DEFAULT_DATA_RESOURCE + " is missing");
        }
        return new CsvCharacterRepository(new InputStreamReader(resource, StandardCharsets.UTF_8), factory);
    }
}
