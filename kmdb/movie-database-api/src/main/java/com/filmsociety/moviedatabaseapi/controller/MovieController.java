package com.filmsociety.moviedatabaseapi.controller;

// Import necessary classes and packages
import com.filmsociety.moviedatabaseapi.entity.Movie;
import com.filmsociety.moviedatabaseapi.entity.Actor;
import com.filmsociety.moviedatabaseapi.exception.NotFoundException;
import com.filmsociety.moviedatabaseapi.exception.ErrorResponse;
import com.filmsociety.moviedatabaseapi.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController // Indicates that this class is a REST controller
@RequestMapping("/api/movies") // Maps requests to /api/movies to this controller
public class MovieController {

    @Autowired // Automatically injects the MovieService bean
    private MovieService movieService;

    @GetMapping // Handles GET requests to retrieve all movies
    public ResponseEntity<?> getAllMovies(
            @RequestParam(defaultValue = "0") int page, // Pagination: default page is 0
            @RequestParam(defaultValue = "10") int size, // Pagination: default size is 10
            @RequestParam(required = false) Long genre, // Optional genre filter
            @RequestParam(required = false) Long actor) { // Optional actor filter
        // Validate pagination parameters
        if (page < 0 || size <= 0) { // Check if page and size are valid
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Invalid pagination parameters. Page must be >= 0 and size must be > 0."));
        }

        List<Movie> movies; // Declare a list to hold the retrieved movies

        if (actor != null) { // If actor ID is provided
            // Fetch movies by actor ID if provided
            movies = movieService.getMoviesByActorWithoutPagination(actor);
            if (movies.isEmpty()) { // Check if no movies found
                throw new NotFoundException("No movies found for actor ID: " + actor);
            }
        } else if (genre != null) { // If genre ID is provided
            // Fetch movies by genre if specified
            movies = movieService.getMoviesByGenre(genre);
            if (movies.isEmpty()) { // Check if no movies found
                throw new NotFoundException("No movies found for genre ID: " + genre);
            }
        } else { // If no filters are provided
            // Fetch all movies if no actor or genre is specified
            movies = movieService.getAllMovies(page, size);
        }

        return ResponseEntity.ok(movies); // Return the list of movies
    }

    @GetMapping("/search") // Handles GET requests to search movies by title
    public ResponseEntity<List<Movie>> searchMoviesByTitle(@RequestParam String title) {
        List<Movie> movies = movieService.searchMoviesByTitle(title); // Search for movies with the specified title
        return ResponseEntity.ok(movies); // Return the list of found movies
    }

    @GetMapping("/{id}") // Handles GET requests to retrieve a movie by ID
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movie = movieService.getMovieById(id); // Fetch movie by ID
        return movie.map(ResponseEntity::ok) // If found, return it with status 200
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND) // If not found, return status 404
                        .body(null));
    }

    @PostMapping // Handles POST requests to create a new movie
    public ResponseEntity<?> createMovie(@Valid @RequestBody Movie movie) { // Validate incoming movie data
        Movie createdMovie = movieService.createMovie(movie); // Create the movie
        return new ResponseEntity<>(createdMovie, HttpStatus.CREATED); // Return created movie with status 201
    }

    @GetMapping("/genre/{genreId}") // Handles GET requests to retrieve movies by genre ID
    public ResponseEntity<List<Movie>> getMoviesByGenre(@PathVariable Long genreId) {
        List<Movie> movies = movieService.getMoviesByGenre(genreId); // Fetch movies by genre ID
        if (movies.isEmpty()) { // Check if no movies found
            throw new NotFoundException("No movies found for genre ID: " + genreId);
        }
        return ResponseEntity.ok(movies); // Return the list of movies
    }

    @GetMapping("/year/{releaseYear}") // Handles GET requests to retrieve movies by release year
    public ResponseEntity<List<Movie>> getMoviesByReleaseYear(@PathVariable int releaseYear) {
        List<Movie> movies = movieService.getMoviesByReleaseYear(releaseYear); // Fetch movies by release year
        if (movies.isEmpty()) { // Check if no movies found
            throw new NotFoundException("No movies found for release year: " + releaseYear);
        }
        return ResponseEntity.ok(movies); // Return the list of movies
    }

    @GetMapping("/actor/{actorId}") // Handles GET requests to retrieve movies by actor ID
    public ResponseEntity<?> getMoviesByActor(
            @PathVariable Long actorId,
            @RequestParam(defaultValue = "0") int page, // Pagination: default page is 0
            @RequestParam(defaultValue = "10") int size) { // Pagination: default size is 10
        // Validate pagination parameters
        if (page < 0 || size <= 0) { // Check if page and size are valid
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Invalid pagination parameters. Page must be >= 0 and size must be > 0."));
        }
    
        List<Movie> movies = movieService.getMoviesByActor(actorId, page, size); // Fetch movies by actor ID with pagination
        if (movies.isEmpty()) { // Check if no movies found
            throw new NotFoundException("No movies found with actor ID: " + actorId);
        }
        return ResponseEntity.ok(movies); // Return the list of movies
    }

    @GetMapping("/{movieId}/actors") // Handles GET requests to retrieve actors in a specific movie
    public ResponseEntity<List<Actor>> getActorsInMovie(@PathVariable Long movieId) {
        List<Actor> actors = movieService.getActorsInMovie(movieId); // Fetch actors for the specified movie
        if (actors.isEmpty()) { // Check if no actors found
            throw new NotFoundException("No actors found for movie ID: " + movieId);
        }
        return ResponseEntity.ok(actors); // Return the list of actors
    }

    @DeleteMapping("/{id}") // Handles DELETE requests to delete a movie by ID
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        try {
            boolean isDeleted = movieService.deleteMovie(id); // Attempt to delete the movie
            if (!isDeleted) { // Check if deletion was unsuccessful
                return ResponseEntity.notFound().build(); // Return 404 if not found
            }
            return ResponseEntity.noContent().build(); // Return 204 No Content if deletion was successful
        } catch (NotFoundException e) { // Handle case where movie is not found
            return ResponseEntity.status(404).body(e.getMessage()); // Return 404 with error message
        }
    }

    // PATCH endpoint to update a movie's actors (adding and removing)
    @PatchMapping("/{id}") // Handles PATCH requests to update a movie's actors by ID
    public ResponseEntity<?> updateMovieActors(@PathVariable Long id, @RequestBody Map<String, List<Long>> actorIdsMap) {
        // Validate if the request body contains an 'id' field
        if (actorIdsMap.containsKey("id")) { // Check if 'id' is included in the request body
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("ID cannot be modified.")); // Return error if 'id' is present
        }

        List<Long> actorIds = actorIdsMap.get("actors"); // Extract actor IDs from the request body

        // Check if the actors list is empty or not provided
        if (actorIds == null) { // If actor IDs are not provided
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Actor IDs must be provided.")); // Return error
        }

        try {
            Movie updatedMovie = movieService.updateMovieActors(id, actorIds); // Update the movie's actors
            return ResponseEntity.ok(updatedMovie); // Returns the updated movie object
        } catch (NotFoundException e) { // Handle case where movie is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 if not found
        }
    }
}
