package com.tokmakov.exception;

public class CoordinatesOutOfBoundsException extends RuntimeException {
    public CoordinatesOutOfBoundsException(int x, int y, int fieldSize) {
        super("Coordinates (" + x + ", " + y + ") are outside the field bounds [0.." + (fieldSize - 1) + "]");
    }
}
