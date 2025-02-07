package com.filmsociety.moviedatabaseapi.datainitializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.filmsociety.moviedatabaseapi.entity.Actor;
import com.filmsociety.moviedatabaseapi.entity.Genre;
import com.filmsociety.moviedatabaseapi.entity.Movie;
import com.filmsociety.moviedatabaseapi.repository.ActorRepository;
import com.filmsociety.moviedatabaseapi.repository.GenreRepository;
import com.filmsociety.moviedatabaseapi.repository.MovieRepository;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;

    public DataInitializer(GenreRepository genreRepository, ActorRepository actorRepository, MovieRepository movieRepository) {
        this.genreRepository = genreRepository;
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Sample genres
        if (genreRepository.count() == 0) {
            Genre genre1 = new Genre("Action");
            Genre genre2 = new Genre("Drama");
            Genre genre3 = new Genre("Comedy");
            Genre genre4 = new Genre("Horror");
            Genre genre5 = new Genre("Sci-fi");

            genreRepository.saveAll(Arrays.asList(genre1, genre2, genre3, genre4, genre5));
        }

        // Sample actors
        if (actorRepository.count() == 0) {
            Actor actor1 = new Actor("Keanu Reeves", LocalDate.of(1955, 9, 2));
            Actor actor2 = new Actor("Leonardo DiCaprio", LocalDate.of(1974, 11, 11));
            Actor actor3 = new Actor("Tom Hanks", LocalDate.of(1956, 7, 9));
            Actor actor4 = new Actor("Scarlett Johansson", LocalDate.of(1984, 11, 22));
            Actor actor5 = new Actor("Brad Pitt", LocalDate.of(1963, 12, 18));
            Actor actor6 = new Actor("Meryl Streep", LocalDate.of(1949, 6, 22));
            Actor actor7 = new Actor("Denzel Washington", LocalDate.of(1954, 12, 28));
            Actor actor8 = new Actor("Jennifer Lawrence", LocalDate.of(1990, 8, 15));
            Actor actor9 = new Actor("Robert Downey Jr.", LocalDate.of(1965, 4, 4));
            Actor actor10 = new Actor("Angelina Jolie", LocalDate.of(1975, 6, 4));
            Actor actor11 = new Actor("Matthew McConaughey", LocalDate.of(1969, 11, 4));
            Actor actor12 = new Actor("Emma Stone", LocalDate.of(1988, 11, 6));
            Actor actor13 = new Actor("Ryan Gosling", LocalDate.of(1980, 11, 12));
            Actor actor14 = new Actor("Tom Hardy", LocalDate.of(1977, 9, 15));
            Actor actor15 = new Actor("Hugh Jackman", LocalDate.of(1968, 10, 12));

            actorRepository.saveAll(Arrays.asList(actor1, actor2, actor3, actor4, actor5, actor6, actor7, actor8, actor9, actor10, actor11, actor12, actor13, actor14, actor15));
        }

        // Sample movies with relationships
        if (movieRepository.count() == 0) {
            // Fetch all genres
            Genre action = genreRepository.findByName("Action");
            Genre drama = genreRepository.findByName("Drama");
            Genre comedy = genreRepository.findByName("Comedy");
            Genre horror = genreRepository.findByName("Horror");
            Genre sciFi = genreRepository.findByName("Sci-fi");

            // Fetch all actors
            Actor keanu = actorRepository.findByName("Keanu Reeves");
            Actor leo = actorRepository.findByName("Leonardo DiCaprio");
            Actor tomHanks = actorRepository.findByName("Tom Hanks");
            Actor scarlett = actorRepository.findByName("Scarlett Johansson");
            Actor brad = actorRepository.findByName("Brad Pitt");
            Actor meryl = actorRepository.findByName("Meryl Streep");
            Actor denzel = actorRepository.findByName("Denzel Washington");
            Actor jennifer = actorRepository.findByName("Jennifer Lawrence");
            Actor robert = actorRepository.findByName("Robert Downey Jr.");
            Actor angelina = actorRepository.findByName("Angelina Jolie");
            @SuppressWarnings("unused")
            Actor matthew = actorRepository.findByName("Matthew McConaughey");
            @SuppressWarnings("unused")
            Actor emma = actorRepository.findByName("Emma Stone");
            Actor ryan = actorRepository.findByName("Ryan Gosling");
            Actor tomHardy = actorRepository.findByName("Tom Hardy");
            Actor hugh = actorRepository.findByName("Hugh Jackman");

            // Create movies with associated genres and actors
            Movie movie1 = new Movie("The Matrix", 1999, 136);
            movie1.getGenres().add(action);
            movie1.getGenres().add(sciFi);
            movie1.getActors().add(keanu);

            Movie movie2 = new Movie("Inception", 2010, 148);
            movie2.getGenres().add(action);
            movie2.getGenres().add(drama);
            movie2.getGenres().add(sciFi);
            movie2.getActors().add(leo);

            Movie movie3 = new Movie("Forrest Gump", 1994, 142);
            movie3.getGenres().add(drama);
            movie3.getGenres().add(comedy);
            movie3.getActors().add(tomHanks);

            Movie movie4 = new Movie("Lost in Translation", 2003, 102);
            movie4.getGenres().add(comedy);
            movie4.getGenres().add(drama);
            movie4.getActors().add(scarlett);

            Movie movie5 = new Movie("Fight Club", 1999, 139);
            movie5.getGenres().add(drama);
            movie5.getGenres().add(comedy);
            movie5.getActors().add(brad);

            Movie movie6 = new Movie("The Devil Wears Prada", 2006, 109);
            movie6.getGenres().add(comedy);
            movie6.getGenres().add(drama);
            movie6.getActors().add(meryl);

            Movie movie7 = new Movie("Training Day", 2001, 122);
            movie7.getGenres().add(action);
            movie7.getGenres().add(drama);
            movie7.getActors().add(denzel);

            Movie movie8 = new Movie("Silver Linings Playbook", 2012, 122);
            movie8.getGenres().add(comedy);
            movie8.getGenres().add(drama);
            movie8.getActors().add(jennifer);

            Movie movie9 = new Movie("Iron Man", 2008, 126);
            movie9.getGenres().add(action);
            movie9.getGenres().add(sciFi);
            movie9.getActors().add(robert);

            Movie movie10 = new Movie("Maleficent", 2014, 97);
            movie10.getGenres().add(drama);
            movie10.getActors().add(angelina);

            Movie movie11 = new Movie("The Wolf of Wall Street", 2013, 180);
            movie11.getGenres().add(drama);
            movie11.getGenres().add(comedy);
            movie11.getActors().add(leo);

            Movie movie12 = new Movie("La La Land", 2016, 128);
            movie12.getGenres().add(comedy);
            movie12.getGenres().add(drama);
            movie12.getActors().add(ryan);

            Movie movie13 = new Movie("Mad Max: Fury Road", 2015, 120);
            movie13.getGenres().add(action);
            movie13.getGenres().add(sciFi);
            movie13.getActors().add(tomHardy);

            Movie movie14 = new Movie("The Greatest Showman", 2017, 105);
            movie14.getGenres().add(drama);
            movie14.getActors().add(hugh);

            Movie movie15 = new Movie("Get Out", 2017, 104);
            movie15.getGenres().add(action);
            movie15.getGenres().add(horror);

            Movie movie16 = new Movie("A Quiet Place", 2018, 90);
            movie16.getGenres().add(horror);
            movie16.getGenres().add(sciFi);

            Movie movie17 = new Movie("Her", 2013, 126);
            movie17.getGenres().add(drama);
            movie17.getGenres().add(sciFi);
            movie17.getActors().add(scarlett);

            Movie movie18 = new Movie("Catch Me If You Can", 2002, 141);
            movie18.getGenres().add(drama);
            movie18.getGenres().add(action);
            movie18.getActors().add(leo);
            movie18.getActors().add(tomHanks);

            Movie movie19 = new Movie("The Intern", 2015, 121);
            movie19.getGenres().add(comedy);
            movie19.getGenres().add(drama);

            Movie movie20 = new Movie("The Conjuring", 2013, 112);
            movie20.getGenres().add(horror);
            movie20.getGenres().add(action);

            // Save all movies
            movieRepository.saveAll(Arrays.asList(movie1, movie2, movie3, movie4, movie5, movie6,
                movie7, movie8, movie9, movie10, movie11, movie12, movie13, movie14, movie15,
                movie16, movie17, movie18, movie19, movie20));
        }
    }
}
