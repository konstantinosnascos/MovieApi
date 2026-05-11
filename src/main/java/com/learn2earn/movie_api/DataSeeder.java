package com.learn2earn.movie_api;

import com.learn2earn.movie_api.dto.MovieResponseDTO;
import com.learn2earn.movie_api.model.Director;
import com.learn2earn.movie_api.model.Movie;
import com.learn2earn.movie_api.repository.DirectorRepository;
import com.learn2earn.movie_api.repository.MovieRepository;
import com.learn2earn.movie_api.service.MovieService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {
    private final MovieRepository movieRepo;
    private final DirectorRepository directorRepo;

    public DataSeeder(MovieRepository movieRepo, DirectorRepository directorRepo) {
        this.movieRepo = movieRepo;
        this.directorRepo = directorRepo;
    }

    @Override
    @Transactional
    public void run(String... args) {

        Director peter = directorRepo.save(new Director("Peter Jackson"));

        Movie LotR = movieRepo.save(new Movie("Lord of the Rings", peter, "WATCHED"));
        LotR.setRating(10);
    }
}
