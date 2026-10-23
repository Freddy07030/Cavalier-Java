package com.example.rpg.exception;

/** Thrown when a defeated character tries to act, or is targeted as if alive. */
public class CharacterDeadException extends RpgException {

    public CharacterDeadException(String characterName) {
        super(characterName + " is defeated and cannot take part in this action");
    }
}
