package com.learn2earn.movie_api.exception;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(String id) {
        super("Movie with id " + id + " not found");
    }
}
