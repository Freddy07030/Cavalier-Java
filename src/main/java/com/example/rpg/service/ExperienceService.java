package com.example.rpg.service;

import com.example.rpg.model.Character;
import com.example.rpg.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Rewards the surviving heroes after a victory. */
public class ExperienceService {

    static final int XP_PER_MONSTER_LEVEL = 10;

    /**
     * Each surviving hero receives XP equal to the sum of the defeated monsters' levels times
     * {@link #XP_PER_MONSTER_LEVEL}.
     * @return messages to show the player, including level-ups
     */
    public List<String> awardVictory(Team heroes, Team defeatedMonsters) {
        Objects.requireNonNull(heroes, "heroes must not be null");
        Objects.requireNonNull(defeatedMonsters, "defeatedMonsters must not be null");

        int xp = defeatedMonsters.getMembers().stream()
                .mapToInt(monster -> monster.getLevel() * XP_PER_MONSTER_LEVEL)
                .sum();

        List<String> messages = new ArrayList<>();
        messages.add("Victory! Each surviving hero gains " + xp + " XP.");
        for (Character hero : heroes.getAliveMembers()) {
            if (hero.gainExperience(xp) > 0) {
                messages.add(hero.getName() + " reached level " + hero.getLevel() + "!");
            }
        }
        return messages;
    }
}
