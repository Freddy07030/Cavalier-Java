package com.example.rpg.service;

import com.example.rpg.model.CharacterStats;
import com.example.rpg.model.Monster;
import com.example.rpg.model.Team;
import com.example.rpg.model.Warrior;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExperienceServiceTest {

    private final ExperienceService service = new ExperienceService();

    @Test
    void survivingHeroesGainXpAndLevelUp() {
        Warrior hero = new Warrior(new CharacterStats("Aldric", 1, 100, 18, 15, 8, 0, 0));
        Team heroes = new Team(Team.HEROES);
        heroes.add(hero);
        Team defeated = new Team(Team.MONSTERS);
        // Level 10 monster gives 10 * 10 = 100 XP, exactly enough for level 2.
        defeated.add(new Monster(new CharacterStats("Ogre", 10, 50, 5, 5, 5, 0, 0)));

        var messages = service.awardVictory(heroes, defeated);

        assertEquals(2, hero.getLevel());
        assertTrue(messages.contains("Aldric reached level 2!"));
    }

    @Test
    void deadHeroesGainNoXp() {
        Warrior hero = new Warrior(new CharacterStats("Aldric", 1, 100, 18, 15, 8, 0, 0));
        hero.takeDamage(1000);
        Team heroes = new Team(Team.HEROES);
        heroes.add(hero);
        Team defeated = new Team(Team.MONSTERS);
        defeated.add(new Monster(new CharacterStats("Ogre", 10, 50, 5, 5, 5, 0, 0)));

        service.awardVictory(heroes, defeated);

        assertEquals(1, hero.getLevel());
        assertEquals(0, hero.getExperience());
    }
}
