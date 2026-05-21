package com.learn2earn.movie_api.exception;

public class MovieAlreadyReturnedException extends RuntimeException {
    public MovieAlreadyReturnedException(Long loanId) {
        super("Loan with id " + loanId + " has already been returned");
    }
}
