package com.tokmakov.exception;

public class InvalidUuidFormatException extends RuntimeException {
    public InvalidUuidFormatException(String uuid) {
        super("UUID '" + uuid + "' has invalid format");
    }
}
