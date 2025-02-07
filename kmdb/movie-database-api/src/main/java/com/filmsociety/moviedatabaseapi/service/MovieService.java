package com.filmsociety.moviedatabaseapi.service;

import com.filmsociety.moviedatabaseapi.entity.Movie;
import com.filmsociety.moviedatabaseapi.entity.Actor;
import com.filmsociety.moviedatabaseapi.entity.Genre;
import com.filmsociety.moviedatabaseapi.exception.NotFoundException;
import com.filmsociety.moviedatabaseapi.repository.MovieRepository;
import com.filmsociety.moviedatabaseapi.repository.ActorRepository;
import com.filmsociety.moviedatabaseapi.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository; // Repository for Movie entity

    @Autowired
    private ActorRepository actorRepository; // Repository for Actor entity

    @Autowired
    private GenreRepository genreRepository; // Repository for Genre entity

    // Fetch all movies with pagination
    public List<Movie> getAllMovies(int page, int size) {
        // Handle pagination
        Pageable pageable = PageRequest.of(page, size);
        return movieRepository.findAll(pageable).getContent(); // Get paginated movie list
    }

    // Get a movie by its ID
    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id); // Fetch movie by ID
    }

    // Create a new movie
    public Movie createMovie(Movie movie) {
        // Fetch and attach actors to the persistence context
        Set<Actor> attachedActors = new HashSet<>();
        for (Actor actor : movie.getActors()) {
            if (actor.getId() != null) {
                // Fetch the actor from the database (attach it)
                Actor existingActor = actorRepository.findById(actor.getId())
                        .orElseThrow(() -> new NotFoundException("Actor not found with ID: " + actor.getId()));
                attachedActors.add(existingActor); // Attach existing actor
            } else {
                // If actor is new (no ID), let it be persisted
                attachedActors.add(actor);
            }
        }
        movie.setActors(attachedActors); // Set attached actors
        
        // Fetch and attach genres to the persistence context
        Set<Genre> attachedGenres = new HashSet<>();
        for (Genre genre : movie.getGenres()) {
            if (genre.getId() != null) {
                // Fetch the genre from the database (attach it)
                Genre existingGenre = genreRepository.findById(genre.getId())
                        .orElseThrow(() -> new NotFoundException("Genre not found with ID: " + genre.getId()));
                attachedGenres.add(existingGenre); // Attach existing genre
            } else {
                // If genre is new (no ID), let it be persisted
                attachedGenres.add(genre);
            }
        }
        movie.setGenres(attachedGenres); // Set attached genres

        // Now save the movie with the attached actors and genres
        return movieRepository.save(movie); // Persist new movie
    }

    // Get movies by genre ID
    public List<Movie> getMoviesByGenre(Long genreId) {
        return movieRepository.findByGenres_Id(genreId); // Fetch movies associated with genre
    }

    // Get movies released in a specific year
    public List<Movie> getMoviesByReleaseYear(int releaseYear) {
        return movieRepository.findByReleaseYear(releaseYear); // Fetch movies by release year
    }

    // Search movies by title
    public List<Movie> searchMoviesByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title); // Search for movies by title
    }

    // Get movies by actor ID with pagination
    public List<Movie> getMoviesByActor(Long actorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return movieRepository.findByActors_Id(actorId, pageable).getContent(); // Fetch movies for the actor
    }

    // Get movies by actor ID without pagination
    public List<Movie> getMoviesByActorWithoutPagination(Long actorId) {
        return movieRepository.findByActors_Id(actorId); // Fetch movies for the actor without pagination
    }

    // Get actors in a specific movie
    public List<Actor> getActorsInMovie(Long movieId) {
        Optional<Movie> movie = movieRepository.findById(movieId); // Fetch movie by ID
        if (movie.isPresent()) {
            return List.copyOf(movie.get().getActors()); // Return actors in the movie
        } else {
            throw new NotFoundException("Movie not found with ID: " + movieId); // Handle not found
        }
    }

    // Update the actors in a movie
    public Movie updateMovieActors(Long movieId, List<Long> actorIds) {
        // Fetch the movie by ID
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie not found with ID: " + movieId));

        // Fetch and attach the actors to the movie
        Set<Actor> attachedActors = new HashSet<>();
        for (Long actorId : actorIds) {
            Actor actor = actorRepository.findById(actorId)
                    .orElseThrow(() -> new NotFoundException("Actor not found with ID: " + actorId));
            attachedActors.add(actor); // Attach existing actor
        }
        movie.setActors(attachedActors); // Set attached actors

        return movieRepository.save(movie); // Persist updated movie
    }

    // Delete a movie by ID
    public boolean deleteMovie(Long id) {
        Optional<Movie> movie = movieRepository.findById(id); // Fetch movie by ID
        if (movie.isPresent()) {
            movieRepository.deleteById(id); // Delete movie
            return true;
        } else {
            throw new NotFoundException("Movie not found with ID: " + id); // Handle not found
        }
    }
}
