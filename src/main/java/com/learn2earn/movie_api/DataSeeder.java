package com.learn2earn.movie_api;

import com.learn2earn.movie_api.dto.MovieResponseDTO;
import com.learn2earn.movie_api.model.Director;
import com.learn2earn.movie_api.model.Movie;
import com.learn2earn.movie_api.model.User;
import com.learn2earn.movie_api.repository.DirectorRepository;
import com.learn2earn.movie_api.repository.MovieRepository;
import com.learn2earn.movie_api.repository.UserRepository;
import com.learn2earn.movie_api.security.SecurityConfig;
import com.learn2earn.movie_api.service.MovieService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {
    private final MovieRepository movieRepo;
    private final DirectorRepository directorRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(MovieRepository movieRepo, DirectorRepository directorRepo, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.movieRepo = movieRepo;
        this.directorRepo = directorRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {

        User alice = userRepo.save(
                new User(
                        "alice",
                        passwordEncoder.encode("password")
                )
        );

        Director peter = directorRepo.save(new Director("Peter Jackson"));

        Movie LotR = movieRepo.save(new Movie("Lord of the Rings", peter, "WATCHED", alice));
        LotR.setRating(10);
    }
}
