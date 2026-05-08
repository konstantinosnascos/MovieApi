package com.learn2earn.movie_api.repository;

import com.learn2earn.movie_api.model.Movie;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class MovieRepository {
    private final Map<String, Movie> database = new HashMap<>();

    public MovieRepository() {
        //Sample data
        Movie inception = new Movie("Inception", "Christpher Nolan", "WATCHED");
        inception.setRating(7.5);
        database.put(inception.getId(), inception);
    }

    //Define operations
    public List<Movie> findAll() {return new ArrayList<>(database.values());}
    public Optional<Movie> findById(String id) {return Optional.ofNullable(database.get(id));}
    public Movie save(Movie movie) {database.put(movie.getId(), movie); return movie;}
    public void delete(String id) {database.remove(id);}
}
