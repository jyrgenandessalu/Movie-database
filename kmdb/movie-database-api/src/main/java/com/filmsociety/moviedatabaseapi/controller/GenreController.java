package com.filmsociety.moviedatabaseapi.controller;

import com.filmsociety.moviedatabaseapi.entity.Genre;
import com.filmsociety.moviedatabaseapi.exception.ImmutableFieldException;
import com.filmsociety.moviedatabaseapi.exception.NotFoundException;
import com.filmsociety.moviedatabaseapi.exception.RelationshipExistsException;
import com.filmsociety.moviedatabaseapi.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/genres") // Base URL for genre-related endpoints
public class GenreController {

    @Autowired
    private GenreService genreService; // Service to handle business logic for genres

    @GetMapping
    public ResponseEntity<?> getAllGenres(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Validate pagination parameters
        if (page < 0 || size <= 0) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Invalid pagination parameters. Page must be >= 0 and size must be > 0.")
            );
        }

        // Fetch paginated list of genres from the service
        Page<Genre> genrePage = genreService.getPaginatedGenres(page, size);
        List<Genre> genres = genrePage.getContent();

        return ResponseEntity.ok(genres); // Return the list of genres
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable Long id) {
        try {
            Genre genre = genreService.getGenreById(id); // Fetch genre by ID
            return ResponseEntity.ok(genre); // Return the found genre
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(null); // Return 404 if not found
        }
    }

    @PostMapping
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) {
        Genre createdGenre = genreService.createGenre(genre); // Create a new genre
        return ResponseEntity.status(201).body(createdGenre); // Return the created genre with status 201
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateGenre(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            // Check if the request is trying to modify the id field
            if (updates.containsKey("id")) {
                throw new ImmutableFieldException("Cannot modify immutable field: id");
            }

            Genre genre = genreService.getGenreById(id); // Fetch genre by ID

            // Apply updates from the request body
            if (updates.containsKey("name")) {
                genre.setName((String) updates.get("name"));
            }

            // Save updated genre
            Genre updatedGenre = genreService.updateGenre(genre);
            return ResponseEntity.ok(updatedGenre); // Return the updated genre

        } catch (ImmutableFieldException e) {
            // Return the error message for immutable field modification
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (NotFoundException e) {
            // Return 404 Not Found with message
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGenre(@PathVariable Long id,
                                              @RequestParam(defaultValue = "false") boolean force) {
        try {
            genreService.deleteGenre(id, force); // Delete genre with optional force parameter
            return ResponseEntity.noContent().build(); // Return 204 No Content
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Return 404 if not found
        } catch (RelationshipExistsException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // Return 409 if relationships exist
        }
    }
}
