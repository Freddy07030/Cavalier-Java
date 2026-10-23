package com.example.rpg.exception;

/**
 * Checked exception: loading data is an external-resource operation that callers must handle
 * (missing file, malformed row). Checked so the compiler forces the caller to deal with it.
 */
public class DataLoadException extends Exception {

    public DataLoadException(String message) {
        super(message);
    }

    public DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
