package com.learn2earn.movie_api.dto;

public record ReturnLoanDTO(
        Long loanId,
        Long movieId,
        String movieTitle,
        String borrowerName
) {}
