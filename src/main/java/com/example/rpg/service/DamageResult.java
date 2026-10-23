package com.example.rpg.service;

/** Outcome of one damage roll: the amount and whether it was a critical hit. */
public record DamageResult(int amount, boolean critical) {
}
