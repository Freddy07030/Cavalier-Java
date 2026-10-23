package com.example.rpg.service;

/**
 * Strategy for turning attack, defense and the critical flag into a damage number.
 * Pluggable so the balance can change without editing the battle code (Open/Closed).
 */
public interface DamageFormula {

    int compute(int attackPower, int defense, boolean critical);
}
