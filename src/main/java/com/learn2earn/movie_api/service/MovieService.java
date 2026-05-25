package com.learn2earn.movie_api.service;

import com.learn2earn.movie_api.dto.MovieRequestDTO;
import com.learn2earn.movie_api.dto.MovieResponseDTO;
import com.learn2earn.movie_api.dto.MovieResponseV2DTO;
import com.learn2earn.movie_api.model.Director;
import com.learn2earn.movie_api.model.User;
import com.learn2earn.movie_api.repository.DirectorRepository;
import com.learn2earn.movie_api.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.learn2earn.movie_api.model.Movie;
import com.learn2earn.movie_api.exception.*;
import com.learn2earn.movie_api.repository.MovieRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository repository;
    private final DirectorRepository directorRepository;
    private final UserRepository userRepository;

    public MovieService(MovieRepository repository, DirectorRepository directorRepository, UserRepository userRepository) {
        this.repository = repository;
        this.directorRepository = directorRepository;
        this.userRepository = userRepository;
    }


    //V1 methods
    public List<MovieResponseDTO> getAllMovies(String username){

        return repository.findByOwnerUsername(username)
                .stream()
                .map(m -> new MovieResponseDTO(
                        m.getId(),
                        m.getTitle(),
                        m.getDirector().getName(),
                        m.getStatus(),
                        m.isLoaned()))
                .collect(Collectors.toList());
    }

    //find by id
    @Cacheable(value = "movie", key = "#id")
    public MovieResponseDTO getMovieById(Long id, String username){
        Movie movie = repository.findByIdAndOwnerUsername(id, username).orElseThrow(()->
                new MovieNotFoundException(id));
        return new MovieResponseDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector().getName(),
                movie.getStatus(),
                movie.isLoaned());
    }

    //save movie
    public MovieResponseDTO createMovie(MovieRequestDTO request, String username){
        Director director = directorRepository.findByName(request.director())
                .orElseGet(() -> directorRepository.save(new Director(request.director())));
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Movie movie = new Movie(request.title(), director, request.status(), owner);
        Movie savedMovie = repository.save(movie);
        return new MovieResponseDTO(
                savedMovie.getId(),
                savedMovie.getTitle(),
                savedMovie.getDirector().getName(),
                savedMovie.getStatus(),
                savedMovie.isLoaned());
    }

    //delete
    @CacheEvict(value = "movies", key = "#id")
    public void deleteMovie(Long id, String username){
        repository.findByIdAndOwnerUsername(id, username).orElseThrow(()->
                new MovieNotFoundException(id));
        repository.deleteById(id);
    }

    //---------------------V2-------------------------V2--------------------------V2--------------
    //V2 methods -> with rating

    public List<MovieResponseV2DTO> getAllMoviesV2(String username){
        return repository.findByOwnerUsername(username)
                .stream()
                .map(m -> new MovieResponseV2DTO(
                        m.getId(),
                        m.getTitle(),
                        m.getDirector().getName(),
                        m.getStatus(),
                        m.getRating(),
                        m.isLoaned()))
                .collect(Collectors.toList());
    }

    public MovieResponseV2DTO getMovieByIdV2(Long id, String username){
        Movie movie = repository.findByIdAndOwnerUsername(id, username).orElseThrow(()->
                new MovieNotFoundException(id));
        return new MovieResponseV2DTO(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector().getName(),
                movie.getStatus(),
                movie.getRating(),
                movie.isLoaned());
    }
}
