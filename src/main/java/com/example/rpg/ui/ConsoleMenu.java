package com.example.rpg.ui;

import com.example.rpg.exception.RpgException;
import com.example.rpg.model.Battle;
import com.example.rpg.model.BattleOutcome;
import com.example.rpg.model.CharacterRecord;
import com.example.rpg.model.Team;
import com.example.rpg.service.BattleService;
import com.example.rpg.service.ExperienceService;
import com.example.rpg.service.RosterBuilder;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main loop of the console game. It drives the menu and catches game errors so that a bad action
 * shows a message instead of crashing the program.
 */
public class ConsoleMenu {

    static final int MAX_TURNS = 50;

    private final List<CharacterRecord> records;
    private final RosterBuilder rosterBuilder;
    private final BattleService battleService;
    private final ExperienceService experienceService;
    private final BattleView view;
    private final Scanner scanner;

    public ConsoleMenu(List<CharacterRecord> records,
                       RosterBuilder rosterBuilder,
                       BattleService battleService,
                       ExperienceService experienceService,
                       BattleView view,
                       Scanner scanner) {
        this.records = records;
        this.rosterBuilder = rosterBuilder;
        this.battleService = battleService;
        this.experienceService = experienceService;
        this.view = view;
        this.scanner = scanner;
    }

    public void run() {
        boolean running = true;
        while (running && scanner.hasNextLine()) {
            view.printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> view.printRoster(records);
                case "2" -> startBattle();
                case "0" -> running = false;
                default -> view.printMessage("Unknown option: " + choice);
            }
        }
        view.printMessage("Goodbye.");
    }

    private void startBattle() {
        try {
            // Fresh teams each time: characters killed in a previous battle come back at full HP.
            Map<String, Team> teams = rosterBuilder.buildTeams(records);
            Team heroes = teams.get(Team.HEROES);
            Team monsters = teams.get(Team.MONSTERS);

            Battle battle = new Battle(heroes, monsters, MAX_TURNS);
            BattleOutcome outcome = battleService.run(battle);

            view.printLog(battle.getLog());
            view.printOutcome(outcome);
            if (outcome == BattleOutcome.HEROES_WIN) {
                view.printMessages(experienceService.awardVictory(heroes, monsters));
            }
        } catch (RpgException | IllegalArgumentException e) {
            view.printMessage("Battle error: " + e.getMessage());
        }
    }
}
