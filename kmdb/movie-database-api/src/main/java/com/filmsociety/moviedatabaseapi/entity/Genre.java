package com.filmsociety.moviedatabaseapi.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.HashSet;
import java.util.Set;

@Entity // Indicates that this class is a JPA entity
public class Genre {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate the ID
    private Long id;

    @Column(nullable = false, unique = true) // Non-nullable and unique field for genre name
    private String name;

    @ManyToMany(mappedBy = "genres") // Define many-to-many relationship with movies
    @JsonBackReference // Prevent infinite recursion
    private Set<Movie> movies = new HashSet<>(); // Set of movies associated with this genre

    // Default constructor
    public Genre() {}

    // Parameterized constructor
    public Genre(String name) {
        this.name = name; // Sets the genre's name
    }

    // Getter for id (no setter to make it immutable)
    public Long getId() {
        return id; // Returns the genre's ID
    }

    // Getter and setter for name
    public String getName() {
        return name; // Returns the genre's name
    }

    public void setName(String name) {
        this.name = name; // Sets the genre's name
    }

    // Getter and setter for movies
    public Set<Movie> getMovies() {
        return movies; // Returns the set of movies associated with this genre
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies; // Sets the movies for this genre
    }
}
