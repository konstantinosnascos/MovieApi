package com.learn2earn.movie_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.learn2earn.movie_api.service.MovieService;
import com.learn2earn.movie_api.dto.*;

@RestController
@RequestMapping("api/v1/movies")
public class MovieController {

    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @GetMapping
    public List<MovieResponseDTO> getAllMovies(){
        return service.getAllMovies();
    }

    @GetMapping(value = "/{id}")
    public MovieResponseDTO getMovieById(@PathVariable String id){
        return service.getMovieById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponseDTO createMovie(@RequestBody MovieResponseDTO request){
        return service.createMovie(request);
    }

    @DeleteMapping( "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable String id){
        service.deleteMovie(id);
    }
}
