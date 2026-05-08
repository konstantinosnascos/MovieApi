package com.learn2earn.movie_api.controller;

import com.learn2earn.movie_api.service.MovieService;
import org.springframework.web.bind.annotation.*;
import com.learn2earn.movie_api.dto.MovieResponseV2DTO;
import java.util.List;

@RestController
@RequestMapping("api/v2/movies")
public class MovieControllerV2 {

    private final MovieService service;

    public MovieControllerV2(MovieService service) {
        this.service = service;
    }

    @GetMapping
    public List<MovieResponseV2DTO> getAllMovies(){
        return service.getAllMoviesV2();
    }

    @GetMapping("/{id}")
    public MovieResponseV2DTO getMovieById(@PathVariable String id){
        return service.getMovieByIdV2(id);
    }
}
