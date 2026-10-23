package com.example.rpg.model;

import java.util.Objects;

/**
 * Base type of every fighter (heroes and monsters).
 *
 * Holds the state and rules shared by all fighters. Subclasses define what makes them different:
 * their label, how they grow on level-up, and any special combat behaviour.
 *
 * Named {@code Character} as in the subject. It shadows {@link java.lang.Character} inside
 * this project, so other classes import it explicitly.
 */
public abstract class Character {

    private static final double DEFAULT_CRITICAL_CHANCE = 0.05;
    private static final int XP_PER_LEVEL_FACTOR = 100;

    private final String name;
    private int level;
    private int maxHp;
    private int currentHp;
    private int attack;
    private int defense;
    private int speed;
    private int experience;
    private Weapon weapon;

    protected Character(CharacterStats stats) {
        validate(stats);
        this.name = stats.name().trim();
        this.level = stats.level();
        this.maxHp = stats.hp();
        this.currentHp = stats.hp();
        this.attack = stats.attack();
        this.defense = stats.defense();
        this.speed = stats.speed();
        this.weapon = stats.weaponBonus() > 0
                ? new Weapon("Weapon", stats.weaponBonus())
                : Weapon.NONE;
    }

    private static void validate(CharacterStats stats) {
        Objects.requireNonNull(stats, "stats must not be null");
        if (stats.name() == null || stats.name().isBlank()) {
            throw new IllegalArgumentException("Character name must not be blank");
        }
        if (stats.level() < 1) {
            throw new IllegalArgumentException("Level must be at least 1");
        }
        if (stats.hp() <= 0) {
            throw new IllegalArgumentException("HP must be greater than 0");
        }
        if (stats.attack() < 0 || stats.defense() < 0 || stats.speed() < 0 || stats.weaponBonus() < 0) {
            throw new IllegalArgumentException("Attack, defense, speed and weapon bonus must not be negative");
        }
    }

    /** Class label shown to the player, e.g. "Warrior". */
    public abstract String getTypeLabel();

    /**
     * Applies the stat gains of one level-up. Each class grows differently, which is why this is
     * abstract: the base class cannot know the right numbers.
     */
    protected abstract void onLevelUp();

    /** Attack value used in damage calculation. Subclasses may override it to add special rules. */
    public int getAttackPower() {
        return attack + weapon.getAttackBonus();
    }

    /** Probability (0 to 1) of a critical hit on each attack. */
    public double getCriticalChance() {
        return DEFAULT_CRITICAL_CHANCE;
    }

    public void takeDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Damage must not be negative");
        }
        currentHp = Math.max(0, currentHp - amount);
    }

    /** Restores HP, never above the maximum. */
    public void receiveHealing(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Healing must not be negative");
        }
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    public boolean isAlive() {
        return currentHp > 0;
    }

    public double getHpRatio() {
        return (double) currentHp / maxHp;
    }

    public void equip(Weapon newWeapon) {
        this.weapon = Objects.requireNonNull(newWeapon, "weapon must not be null");
    }

    /**
     * Adds experience and levels up as many times as the total allows.
     * @return the number of levels gained by this call
     */
    public int gainExperience(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Experience must not be negative");
        }
        experience += amount;
        int levelsGained = 0;
        while (experience >= xpRequiredForNextLevel()) {
            experience -= xpRequiredForNextLevel();
            level++;
            onLevelUp();
            levelsGained++;
        }
        return levelsGained;
    }

    public int xpRequiredForNextLevel() {
        return level * XP_PER_LEVEL_FACTOR;
    }

    /** Helper for subclasses: raises the stats and heals by the same HP amount. */
    protected void growStats(int hpGain, int attackGain, int defenseGain, int speedGain) {
        maxHp += hpGain;
        currentHp += hpGain;
        attack += attackGain;
        defense += defenseGain;
        speed += speedGain;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getBaseAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public int getExperience() {
        return experience;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, lvl %d) HP %d/%d", name, getTypeLabel(), level, currentHp, maxHp);
    }
}
