package com.filmsociety.moviedatabaseapi.exception;

// This class encapsulates an error response message for API responses
public class ErrorResponse {
    private String message; // The error message

    // Constructor to initialize the error message
    public ErrorResponse(String message) {
        this.message = message;
    }

    // Getter for the message
    public String getMessage() {
        return message;
    }

    // Setter for the message
    public void setMessage(String message) {
        this.message = message;
    }
}
