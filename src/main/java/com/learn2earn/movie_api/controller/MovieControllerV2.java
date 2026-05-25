package com.learn2earn.movie_api.controller;

import com.learn2earn.movie_api.service.MovieService;
import org.springframework.web.bind.annotation.*;
import com.learn2earn.movie_api.dto.MovieResponseV2DTO;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/v2/movies")
public class MovieControllerV2 {

    private final MovieService service;

    public MovieControllerV2(MovieService service) {
        this.service = service;
    }

    @GetMapping
    public List<MovieResponseV2DTO> getAllMovies(String username){
        return service.getAllMoviesV2(username);
    }

    @GetMapping("/{id}")
    public MovieResponseV2DTO getMovieById(@PathVariable Long id, Principal principal){
        return service.getMovieByIdV2(id, principal.getName());
    }
}
