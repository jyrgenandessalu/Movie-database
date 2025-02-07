package com.filmsociety.moviedatabaseapi.exception;

// Custom exception for cases where a relationship already exists
public class RelationshipExistsException extends RuntimeException {
    public RelationshipExistsException(String message) {
        super(message); // Pass message to superclass constructor
    }
}
