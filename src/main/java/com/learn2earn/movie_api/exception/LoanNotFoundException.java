package com.learn2earn.movie_api.exception;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(Long id) {
        super("Loan with id " + id + " not found");
    }
}
