package com.filmsociety.moviedatabaseapi.repository;

import com.filmsociety.moviedatabaseapi.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository interface for Genre entity, extending JpaRepository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByName(String name);
    // No additional methods defined; basic CRUD operations are inherited
}
