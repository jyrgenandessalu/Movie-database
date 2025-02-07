package com.filmsociety.moviedatabaseapi.exception;

// Custom exception for invalid data
public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message); // Pass message to superclass constructor
    }
}
