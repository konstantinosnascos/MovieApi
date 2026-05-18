package com.learn2earn.movie_api.service;

import com.learn2earn.movie_api.exception.*;
import com.learn2earn.movie_api.model.*;
import com.learn2earn.movie_api.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private LoanService loanService;

    @Test
    void borrowMovie_ThrowException_IfAlreadyLoaned(){

        //Prepare arrange of test instance

        Movie movie = new Movie("Dune", new Director("Dennis"), "PLAN_TO_WATCH");
        //set this movie as borrowed

        movie.setLoaned(true);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        //Set our action assert it
        assertThrows(MovieAlreadyLoanedException.class, () -> loanService.loanMovie(1L, "Alice"));
        verify(loanRepository,never()).save(any()); //to ensure never save this testing transaction
    }


}
