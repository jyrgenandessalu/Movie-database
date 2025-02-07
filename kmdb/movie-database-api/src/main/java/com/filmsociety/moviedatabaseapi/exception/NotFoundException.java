package com.filmsociety.moviedatabaseapi.exception;

// Custom exception for resource not found scenarios
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message); // Pass message to superclass constructor
    }
}
