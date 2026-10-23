package com.example.rpg.service;

import com.example.rpg.model.Character;
import com.example.rpg.model.Team;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/** Decides who acts in which order: fastest first, ties broken by name for a stable result. */
public class TurnOrderService {

    private static final Comparator<Character> ORDER =
            Comparator.<Character>comparingInt(Character::getSpeed)
                    .reversed()
                    .thenComparing(Character::getName);

    public List<Character> computeTurnOrder(Team heroes, Team monsters) {
        return Stream.concat(heroes.getAliveMembers().stream(), monsters.getAliveMembers().stream())
                .sorted(ORDER)
                .toList();
    }
}
