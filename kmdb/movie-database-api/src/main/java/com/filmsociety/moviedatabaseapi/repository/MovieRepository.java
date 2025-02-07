package com.filmsociety.moviedatabaseapi.repository;

import com.filmsociety.moviedatabaseapi.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository interface for Movie entity, extending JpaRepository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Method to find movies by genre ID
    List<Movie> findByGenres_Id(Long genreId);

    // Method to find movies by release year
    List<Movie> findByReleaseYear(int releaseYear);

    // Method to find movies by title using case-insensitive search
    List<Movie> findByTitleContainingIgnoreCase(String title);

    // Method to find movies by actor ID without pagination
    List<Movie> findByActors_Id(Long actorId);

    // Method to find movies by actor ID with pagination
    Page<Movie> findByActors_Id(Long actorId, Pageable pageable);
}
