package com.filmsociety.moviedatabaseapi.service;

import com.filmsociety.moviedatabaseapi.entity.Genre;
import com.filmsociety.moviedatabaseapi.entity.Movie;
import com.filmsociety.moviedatabaseapi.exception.NotFoundException;
import com.filmsociety.moviedatabaseapi.exception.RelationshipExistsException;
import com.filmsociety.moviedatabaseapi.repository.GenreRepository;
import com.filmsociety.moviedatabaseapi.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository; // Repository for Genre entity

    @Autowired
    private MovieRepository movieRepository; // Repository for Movie entity

    // Fetch all genres
    public List<Genre> getAllGenres() {
        return genreRepository.findAll(); // Get all genres from the database
    }

    // Get a genre by its ID
    public Genre getGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre not found with id: " + id)); // Handle not found
    }

    // Create a new genre
    public Genre createGenre(Genre genre) {
        return genreRepository.save(genre); // Save new genre to the database
    }

    // Update an existing genre
    public Genre updateGenre(Genre genre) {
        // Ensure that the genre exists
        if (!genreRepository.existsById(genre.getId())) {
            throw new NotFoundException("Genre not found with id: " + genre.getId()); // Handle not found
        }
        return genreRepository.save(genre); // Save updated genre
    }

    // Delete a genre with an optional 'force' flag
    public void deleteGenre(Long id, boolean force) {
        Genre genre = getGenreById(id); // Fetch genre by ID

        // Find movies associated with the genre
        List<Movie> moviesWithGenre = movieRepository.findByGenres_Id(genre.getId());

        if (!moviesWithGenre.isEmpty() && !force) {
            throw new RelationshipExistsException("Cannot delete genre. It is associated with movies."); // Handle relationship exists
        }

        if (force) {
            // Remove the association between the genre and the movies
            for (Movie movie : moviesWithGenre) {
                movie.getGenres().remove(genre); // Remove genre from each movie
                movieRepository.save(movie); // Save the updated movie
            }
        }

        genreRepository.delete(genre); // Now delete the genre
    }

    // Get paginated genres
    public Page<Genre> getPaginatedGenres(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // Create pageable object
        return genreRepository.findAll(pageable); // Fetch paginated genres
    }
}
