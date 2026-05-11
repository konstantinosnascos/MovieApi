package com.learn2earn.movie_api.service;

import com.learn2earn.movie_api.dto.MovieResponseDTO;
import com.learn2earn.movie_api.dto.MovieResponseV2DTO;
import com.learn2earn.movie_api.model.Director;
import com.learn2earn.movie_api.repository.DirectorRepository;
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

    public MovieService(MovieRepository repository, DirectorRepository directorRepository) {
        this.repository = repository;
        this.directorRepository = directorRepository;
    }


    //V1 methods
    public List<MovieResponseDTO> getAllMovies(){

        return repository.findAll()
                .stream()
                .map(m -> new MovieResponseDTO(
                        m.getId(),
                        m.getTitle(),
                        m.getDirector().getName(),
                        m.getStatus()))
                .collect(Collectors.toList());
    }

    //find by id
    public MovieResponseDTO getMovieById(String id){
        Movie movie = repository.findById(id).orElseThrow(()->
                new MovieNotFoundException(id));
        return new MovieResponseDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector().getName(),
                movie.getStatus());
    }

    //save movie
    public MovieResponseDTO createMovie(MovieResponseDTO request){
        Director director = directorRepository.findById(request.director()).orElseThrow(()-> new RuntimeException("Director not found"));
        Movie movie = new Movie(request.title(), director, request.status());
        Movie savedMovie = repository.save(movie);
        return new MovieResponseDTO(
                savedMovie.getId(),
                savedMovie.getTitle(),
                savedMovie.getDirector().getName(),
                savedMovie.getStatus());
    }

    //delete
    public void deleteMovie(String id){
        repository.findById(id).orElseThrow(()->
                new MovieNotFoundException(id));
        repository.deleteById(id);
    }

    //---------------------V2-------------------------V2--------------------------V2--------------
    //V2 methods -> with rating

    public List<MovieResponseV2DTO> getAllMoviesV2(){
        return repository.findAll()
                .stream()
                .map(m -> new MovieResponseV2DTO(
                        m.getId(),
                        m.getTitle(),
                        m.getDirector().getName(),
                        m.getStatus(),
                        m.getRating()))
                .collect(Collectors.toList());
    }

    public MovieResponseV2DTO getMovieByIdV2(String id){
        Movie movie = repository.findById(id).orElseThrow(()->
                new MovieNotFoundException(id));
        return new MovieResponseV2DTO(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector().getName(),
                movie.getStatus(),
                movie.getRating());
    }
}
