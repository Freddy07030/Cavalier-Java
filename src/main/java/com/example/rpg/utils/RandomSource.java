package com.example.rpg.utils;

/**
 * Source of randomness, injected rather than created inside the rules.
 * Tests pass a fixed value to make combat deterministic.
 */
@FunctionalInterface
public interface RandomSource {

    /** A number between 0 (inclusive) and 1 (exclusive). */
    double nextDouble();
}
