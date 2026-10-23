package com.example.rpg.utils;

import java.util.Arrays;

/**
 * Minimal CSV splitter for this project's data file. It assumes no quoted commas, which the data
 * never contains. Using a full CSV library would be overkill for a ten-column file.
 */
public final class CsvParser {

    private CsvParser() {
    }

    /** Splits a line on commas and trims every field. Empty trailing fields are kept. */
    public static String[] split(String line) {
        return Arrays.stream(line.split(",", -1))
                .map(String::trim)
                .toArray(String[]::new);
    }
}
