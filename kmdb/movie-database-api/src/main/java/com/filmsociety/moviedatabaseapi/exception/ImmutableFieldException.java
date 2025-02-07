package com.filmsociety.moviedatabaseapi.exception;

// Custom exception for attempts to modify immutable fields
public class ImmutableFieldException extends RuntimeException {
    public ImmutableFieldException(String message) {
        super(message); // Pass message to superclass constructor
    }
}
