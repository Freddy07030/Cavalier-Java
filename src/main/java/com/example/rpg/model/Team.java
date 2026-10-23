package com.example.rpg.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** A named group of fighters that act together. */
public class Team {

    public static final String HEROES = "HEROES";
    public static final String MONSTERS = "MONSTERS";

    private final String name;
    private final List<Character> members = new ArrayList<>();

    public Team(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Team name must not be blank");
        }
        this.name = name;
    }

    public void add(Character character) {
        members.add(Objects.requireNonNull(character, "character must not be null"));
    }

    public String getName() {
        return name;
    }

    public List<Character> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public List<Character> getAliveMembers() {
        return members.stream().filter(Character::isAlive).toList();
    }

    public boolean isDefeated() {
        return members.stream().noneMatch(Character::isAlive);
    }

    public boolean contains(Character character) {
        return members.contains(character);
    }

    /** The living member with the lowest HP ratio below the threshold, used to pick heal targets. */
    public Optional<Character> findMostInjured(double hpRatioThreshold) {
        return getAliveMembers().stream()
                .filter(c -> c.getHpRatio() < hpRatioThreshold)
                .min(Comparator.comparingDouble(Character::getHpRatio));
    }

    /** The living member with the fewest HP, used as the default attack target. */
    public Optional<Character> findWeakestAlive() {
        return getAliveMembers().stream()
                .min(Comparator.comparingInt(Character::getCurrentHp));
    }
}
