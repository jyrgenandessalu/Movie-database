package com.filmsociety.moviedatabaseapi.exception;

// Import necessary classes
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// This class handles global exceptions for REST controllers
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Logger to log error messages
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Method to handle validation exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>(); // Map to hold field errors

        // Extract validation error messages
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage()); // Map field name to error message
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // Return errors with 400 status
    }

    // Method to handle not found exceptions
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        logger.error("Not Found: {}", ex.getMessage()); // Log the not found message
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); // Return message with 404 status
    }

}
