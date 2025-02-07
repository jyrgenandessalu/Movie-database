package com.filmsociety.moviedatabaseapi.controller;

import com.filmsociety.moviedatabaseapi.entity.Actor;
import com.filmsociety.moviedatabaseapi.exception.ImmutableFieldException;
import com.filmsociety.moviedatabaseapi.exception.NotFoundException;
import com.filmsociety.moviedatabaseapi.exception.ErrorResponse;
import com.filmsociety.moviedatabaseapi.exception.RelationshipExistsException;
import com.filmsociety.moviedatabaseapi.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/actors") // Base URL for actor-related endpoints
public class ActorController {

    @Autowired
    private ActorService actorService; // Service to handle business logic for actors

    @GetMapping
    public ResponseEntity<?> getAllActors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Validate pagination parameters
        if (page < 0 || size <= 0) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Invalid pagination parameters. Page must be >= 0 and size must be > 0.")
            );
        }

        // Fetch paginated list of actors from the service
        Page<Actor> actorPage = actorService.getPaginatedActors(page, size);
        List<Actor> actors = actorPage.getContent();

        return ResponseEntity.ok(actors); // Return the list of actors
    }

    // Retrieve actors filtered by name with error handling
    @GetMapping(params = "name")
    public ResponseEntity<List<Actor>> getActorsByName(@RequestParam String name) {
        List<Actor> actors = actorService.getActorsByName(name);
        // Handle case where no actors are found
        if (actors.isEmpty()) {
            throw new NotFoundException("No actors found with name: " + name);
        }
        return ResponseEntity.ok(actors); // Return the list of actors found
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable Long id) {
        try {
            Actor actor = actorService.getActorById(id); // Fetch actor by ID
            return ResponseEntity.ok(actor); // Return the found actor
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(null); // Return 404 if not found
        }
    }

    @PostMapping
    public ResponseEntity<Actor> createActor(@jakarta.validation.Valid @RequestBody Actor actor) {
        Actor createdActor = actorService.createActor(actor); // Create a new actor
        return ResponseEntity.status(201).body(createdActor); // Return the created actor with status 201
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateActor(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            Actor actor = actorService.getActorById(id); // Fetch actor by ID

            // Check if the request is trying to modify the id field
            if (updates.containsKey("id")) {
                throw new ImmutableFieldException("Cannot modify immutable field: id");
            }

            // Apply updates from the request body
            if (updates.containsKey("name")) {
                actor.setName((String) updates.get("name"));
            }
            if (updates.containsKey("birthDate")) {
                actor.setBirthDate(LocalDate.parse((String) updates.get("birthDate"))); // Parse and set birthdate
            }

            // Save updated actor
            Actor updatedActor = actorService.createActor(actor);
            return ResponseEntity.ok(updatedActor); // Return the updated actor

        } catch (ImmutableFieldException e) {
            // Return 400 Bad Request with the immutable field error message
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        } catch (NotFoundException e) {
            // Return 404 Not Found with message
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        } catch (DateTimeParseException e) {
            // Handle birthdate format error
            return ResponseEntity.status(400).body(Map.of("message", "Invalid birthdate format: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteActor(@PathVariable Long id,
                                              @RequestParam(defaultValue = "false") boolean force) {
        try {
            actorService.deleteActor(id, force); // Delete actor with optional force parameter
            return ResponseEntity.noContent().build(); // Return 204 No Content
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Return 404 if not found
        } catch (RelationshipExistsException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // Return 409 if relationships exist
        }
    }

    // Global exception handler for validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Collecting all error messages
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = new ErrorResponse("Invalid input data: " + errorMessage);
        return ResponseEntity.badRequest().body(errorResponse); // Return 400 Bad Request with error messages
    }

    // Custom exception handler for DateTimeParseException
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> handleDateTimeParseException(DateTimeParseException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Invalid birthdate format: " + ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse); // Return 400 Bad Request with error message
    }
}
