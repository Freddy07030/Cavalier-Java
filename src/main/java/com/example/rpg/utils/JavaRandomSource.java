package com.example.rpg.utils;

import java.util.Random;

/** Production implementation backed by {@link Random}. */
public class JavaRandomSource implements RandomSource {

    private final Random random;

    public JavaRandomSource() {
        this(new Random());
    }

    public JavaRandomSource(Random random) {
        this.random = random;
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }
}
