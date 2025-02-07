package com.filmsociety.moviedatabaseapi.service;

import com.filmsociety.moviedatabaseapi.entity.Actor;
import com.filmsociety.moviedatabaseapi.entity.Movie;
import com.filmsociety.moviedatabaseapi.exception.NotFoundException;
import com.filmsociety.moviedatabaseapi.exception.RelationshipExistsException;
import com.filmsociety.moviedatabaseapi.repository.ActorRepository;
import com.filmsociety.moviedatabaseapi.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service // Marks this class as a service layer component
public class ActorService {

    @Autowired
    private ActorRepository actorRepository; // Repository for Actor entity

    @Autowired
    private MovieRepository movieRepository; // Repository for Movie entity

    // Get all actors
    public List<Actor> getAllActors() {
        return actorRepository.findAll(); // Fetch all actors from the database
    }

    // Get paginated actors
    public Page<Actor> getPaginatedActors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // Create pageable object
        return actorRepository.findAll(pageable); // Paginate actors
    }

    // Search actors by name (case-insensitive)
    public List<Actor> getActorsByName(String name) {
        return actorRepository.findByNameContainingIgnoreCase(name); // Search for actors
    }

    // Get a single actor by their ID
    public Actor getActorById(Long id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Actor not found with id: " + id)); // Handle not found
    }

    // Create a new actor
    public Actor createActor(Actor actor) {
        return actorRepository.save(actor); // Save new actor to the database
    }

    // Delete an actor with an optional 'force' flag
    public void deleteActor(Long id, boolean force) {
        Actor actor = getActorById(id); // Fetch actor by ID
        Set<Movie> movies = actor.getMovies(); // Get movies associated with the actor

        if (force) {
            // Detach actor from movies if force deletion is requested
            for (Movie movie : movies) {
                movie.getActors().remove(actor); // Remove actor from each movie
                movieRepository.save(movie); // Persist the change in each movie
            }
            actorRepository.delete(actor); // Delete the actor
        } else {
            // Prevent deletion if actor is still in use
            if (!movies.isEmpty()) {
                throw new RelationshipExistsException("Cannot delete actor. They are still associated with movies."); // Handle relationship exists
            }
            actorRepository.delete(actor); // Delete actor if not associated with any movie
        }
    }
}
