package com.example.rpg.exception;

/** Thrown when a spell is cast without enough mana. Carries the numbers so the UI can explain why. */
public class InsufficientManaException extends RpgException {

    private final int required;
    private final int available;

    public InsufficientManaException(int required, int available) {
        super("Not enough mana: requires " + required + ", available " + available);
        this.required = required;
        this.available = available;
    }

    public int getRequired() {
        return required;
    }

    public int getAvailable() {
        return available;
    }
}
