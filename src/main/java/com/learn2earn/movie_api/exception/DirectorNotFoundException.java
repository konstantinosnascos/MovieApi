package com.learn2earn.movie_api.exception;

public class DirectorNotFoundException extends RuntimeException {
    public DirectorNotFoundException(Long id) {
        super("Movie with id " + id + " not found");
    }
}
