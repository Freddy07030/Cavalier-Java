package com.example.rpg.exception;

/**
 * Base class for errors raised by the game rules (invalid action, dead character, ...).
 * Unchecked on purpose: these are programming or player-input errors the menu handles centrally.
 */
public class RpgException extends RuntimeException {

    public RpgException(String message) {
        super(message);
    }
}
