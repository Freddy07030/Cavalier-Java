package com.example.rpg.ui;

import com.example.rpg.model.BattleOutcome;
import com.example.rpg.model.CharacterRecord;
import com.example.rpg.model.CharacterStats;

import java.io.PrintStream;
import java.util.List;

/** Console output only. It formats what the rules produced and contains no game logic. */
public class BattleView {

    private final PrintStream out;

    public BattleView() {
        this(System.out);
    }

    public BattleView(PrintStream out) {
        this.out = out;
    }

    public void printMenu() {
        out.println();
        out.println("=== RPG Battle Simulator ===");
        out.println("1. Show characters");
        out.println("2. Start battle (heroes vs monsters)");
        out.println("0. Quit");
        out.print("> ");
    }

    public void printRoster(List<CharacterRecord> records) {
        for (CharacterRecord record : records) {
            CharacterStats s = record.stats();
            out.printf("%-8s %-10s %-8s Lv%-3d HP %-4d ATK %-3d DEF %-3d SPD %-3d MANA %-3d WEAPON +%d%n",
                    record.teamName(), s.name(), record.type(), s.level(), s.hp(),
                    s.attack(), s.defense(), s.speed(), s.mana(), s.weaponBonus());
        }
    }

    public void printLog(List<String> log) {
        log.forEach(out::println);
    }

    public void printOutcome(BattleOutcome outcome) {
        out.println();
        out.println("Result: " + outcome.getDescription());
    }

    public void printMessages(List<String> messages) {
        messages.forEach(out::println);
    }

    public void printMessage(String message) {
        out.println(message);
    }
}
