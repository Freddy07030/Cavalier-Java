package com.example.rpg.exception;

/** Thrown when a character attempts an action the rules do not allow, such as attacking an ally. */
public class InvalidActionException extends RpgException {

    public InvalidActionException(String message) {
        super(message);
    }
}
