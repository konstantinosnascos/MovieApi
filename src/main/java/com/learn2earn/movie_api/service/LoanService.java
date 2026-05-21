package com.learn2earn.movie_api.service;

import com.learn2earn.movie_api.dto.LoanResponseDTO;
import com.learn2earn.movie_api.exception.LoanNotFoundException;
import com.learn2earn.movie_api.exception.MovieAlreadyLoanedException;
import com.learn2earn.movie_api.exception.MovieAlreadyReturnedException;
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
    @CacheEvict(value = "movies", allEntries = true)
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

    @Transactional
    @CacheEvict(value = "movies", allEntries = true)
    public LoanResponseDTO returnMovie(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(()-> new LoanNotFoundException(loanId));

        if (loan.getReturnDate()!=null) {
            throw new MovieAlreadyReturnedException(loanId);
        }

        loan.setReturnDate(LocalDate.now());

        Movie movie = loan.getMovie();
        movie.setLoaned(false);
        movieRepository.save(movie);
        loanRepository.save(loan);

        return new LoanResponseDTO(
                loan.getId(),
                loan.getBorrowerName(),
                movie.getTitle()
        );
    }
}
