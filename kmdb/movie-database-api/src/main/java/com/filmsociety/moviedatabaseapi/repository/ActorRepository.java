package com.filmsociety.moviedatabaseapi.repository;

import com.filmsociety.moviedatabaseapi.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository interface for Actor entity, extending JpaRepository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    // Method to find actors by name with case-insensitive search
    List<Actor> findByNameContainingIgnoreCase(String name);
    Actor findByName(String name);
}
