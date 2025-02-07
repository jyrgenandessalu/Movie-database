package com.filmsociety.moviedatabaseapi.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity // Indicates that this class is a JPA entity
public class Movie {
    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate the ID
    private Long id;

    @NotNull(message = "Title cannot be null") // Validation to ensure title is not null
    @Size(min = 1, message = "Title must have at least 1 character") // Validation for title length
    private String title;

    private int releaseYear; // Movie's release year
    private int duration; // Movie's duration in minutes

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Define many-to-many relationship with genres
    @JoinTable(
        name = "movie_genre", // Join table name
        joinColumns = @JoinColumn(name = "movie_id"), // Foreign key in movie_genre referencing movie
        inverseJoinColumns = @JoinColumn(name = "genre_id") // Foreign key in movie_genre referencing genre
    )
    @JsonIgnoreProperties("movies") // Prevent circular references
    private Set<Genre> genres = new HashSet<>(); // Set of genres associated with this movie

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Define many-to-many relationship with actors
    @JoinTable(
        name = "movie_actor", // Join table name
        joinColumns = @JoinColumn(name = "movie_id"), // Foreign key in movie_actor referencing movie
        inverseJoinColumns = @JoinColumn(name = "actor_id") // Foreign key in movie_actor referencing actor
    )
    @JsonIgnoreProperties("movies") // Prevent circular references
    private Set<Actor> actors = new HashSet<>(); // Set of actors associated with this movie

    public Movie() {} // Default constructor

    public Movie(String title, int releaseYear, int duration) { // Parameterized constructor
        this.title = title; // Sets the movie's title
        this.releaseYear = releaseYear; // Sets the movie's release year
        this.duration = duration; // Sets the movie's duration
    }

    public Long getId() {
        return id; // Returns the movie's ID
    }

    public void setId(Long id) {
        this.id = id; // Sets the movie's ID
    }

    public String getTitle() {
        return title; // Returns the movie's title
    }

    public void setTitle(String title) {
        this.title = title; // Sets the movie's title
    }

    public Integer getReleaseYear() {
        return releaseYear; // Returns the movie's release year
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear; // Sets the movie's release year
    }

    public Integer getDuration() {
        return duration; // Returns the movie's duration
    }

    public void setDuration(Integer duration) {
        this.duration = duration; // Sets the movie's duration
    }

    public Set<Genre> getGenres() {
        return genres; // Returns the set of genres associated with the movie
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres; // Sets the genres for this movie
    }

    public Set<Actor> getActors() {
        return actors; // Returns the set of actors associated with the movie
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors; // Sets the actors for this movie
    }
}
