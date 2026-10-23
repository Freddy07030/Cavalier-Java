package com.example.rpg.service;

import com.example.rpg.model.Character;
import com.example.rpg.model.CharacterStats;
import com.example.rpg.model.Monster;
import com.example.rpg.model.Team;
import com.example.rpg.model.Warrior;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TurnOrderServiceTest {

    private final TurnOrderService service = new TurnOrderService();

    private static Character unit(String name, int speed) {
        return new Monster(new CharacterStats(name, 1, 50, 5, 5, speed, 0, 0));
    }

    private static List<String> names(List<Character> order) {
        return order.stream().map(Character::getName).toList();
    }

    @Test
    void fasterCharactersActFirstAndTiesAreSortedByName() {
        Team heroes = new Team(Team.HEROES);
        heroes.add(new Warrior(new CharacterStats("Aldric", 1, 100, 18, 15, 8, 0, 0)));
        heroes.add(unit("Sylvan", 14));
        Team monsters = new Team(Team.MONSTERS);
        monsters.add(unit("Zed", 14));
        monsters.add(unit("Goblin", 9));

        List<String> order = names(service.computeTurnOrder(heroes, monsters));

        assertEquals(List.of("Sylvan", "Zed", "Goblin", "Aldric"), order);
    }

    @Test
    void deadCharactersAreExcludedFromTheTurnOrder() {
        Team heroes = new Team(Team.HEROES);
        Character fallen = unit("Fallen", 20);
        fallen.takeDamage(1000);
        heroes.add(fallen);
        heroes.add(unit("Alive", 5));
        Team monsters = new Team(Team.MONSTERS);
        monsters.add(unit("Goblin", 9));

        List<String> order = names(service.computeTurnOrder(heroes, monsters));

        assertEquals(List.of("Goblin", "Alive"), order);
    }
}
