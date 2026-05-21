package com.learn2earn.movie_api.service;

import com.learn2earn.movie_api.dto.LoanResponseDTO;
import com.learn2earn.movie_api.exception.MovieAlreadyLoanedException;
import com.learn2earn.movie_api.exception.MovieNotFoundException;
import com.learn2earn.movie_api.model.Loan;
import com.learn2earn.movie_api.model.Movie;
import com.learn2earn.movie_api.repository.LoanRepository;
import com.learn2earn.movie_api.repository.MovieRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final MovieRepository movieRepository;

    public LoanService(LoanRepository loanRepository, MovieRepository movieRepository) {
        this.loanRepository = loanRepository;
        this.movieRepository = movieRepository;
    }

    @Transactional
    @CacheEvict(value = "movies", key = "#movieId")
    public void loanMovie(Long movieId, String borrowerName) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(()-> new MovieNotFoundException(movieId));

        if(movie.isLoaned()){
            throw new MovieAlreadyLoanedException(movieId);
        }
        movie.setLoaned(true);
        movieRepository.save(movie);

        Loan loan = new Loan(borrowerName, movie);
        loan.setLoanedDate(LocalDate.now());

        loanRepository.save(loan);
    }
}
