package com.filmsociety.moviedatabaseapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity // Indicates that this class is a JPA entity
public class Actor {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate the ID
    private Long id;

    @Column(nullable = false) // Non-nullable field
    private String name;

    @Column(nullable = false)
    @Past(message = "Birth date must be in the past")  // Validate birth date
    private LocalDate birthDate; // Actor's birth date

    @ManyToMany(mappedBy = "actors", fetch = FetchType.LAZY) // Define many-to-many relationship with movies
    @JsonIgnore // Prevent circular reference when serializing Actor
    private Set<Movie> movies = new HashSet<>(); // Set of movies the actor has participated in

    // Default constructor
    public Actor() {}

    // Parameterized constructor
    public Actor(String name, LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    // Getters and Setters
    public Long getId() {
        return id; // Returns the actor's ID
    }

    // Setter for id omitted to keep it immutable
    public String getName() {
        return name; // Returns the actor's name
    }

    public void setName(String name) {
        this.name = name; // Sets the actor's name
    }

    public LocalDate getBirthDate() {
        return birthDate; // Returns the actor's birth date
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate; // Sets the actor's birth date
    }

    public Set<Movie> getMovies() {
        return movies; // Returns the set of movies associated with the actor
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies; // Sets the movies for this actor
    }
}
