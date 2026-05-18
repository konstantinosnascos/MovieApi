package com.learn2earn.movie_api.exception;

public class MovieAlreadyLoanedException extends RuntimeException {
    public MovieAlreadyLoanedException(Long id) {
        super(id + " is already loaned");
    }
}
