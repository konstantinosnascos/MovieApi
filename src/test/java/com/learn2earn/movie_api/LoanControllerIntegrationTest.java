package com.learn2earn.movie_api;
import com.learn2earn.movie_api.exception.ErrorResponse;
import com.learn2earn.movie_api.exception.*;
import com.learn2earn.movie_api.model.Director;
import com.learn2earn.movie_api.model.Loan;
import com.learn2earn.movie_api.model.Movie;
import com.learn2earn.movie_api.model.User;
import com.learn2earn.movie_api.repository.DirectorRepository;
import com.learn2earn.movie_api.repository.LoanRepository;
import com.learn2earn.movie_api.repository.MovieRepository;
import com.learn2earn.movie_api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoanControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private DirectorRepository directorRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepo;

    @BeforeEach
    public void setup(){
        loanRepository.deleteAll();
        movieRepository.deleteAll();
        directorRepository.deleteAll();
    }

    @Test
    void borrowMovie_ShouldReturnCreatedAndSetLoanStatus() throws Exception {
        User alice = userRepo.save(
                new User(
                        "alice",
                        passwordEncoder.encode("password")
                )
        );

        Director director = new Director("Ridley Scott");
        Movie movie = movieRepository.save(new Movie("Alien", director, "Available", alice));
        mockMvc.perform(post("/api/v1/loans/" + movie.getId())
                .param("borrowerName", "John Doe"))
                .andExpect(status().isCreated());

        Movie updatedMovie = movieRepository.findById(movie.getId()).get();
        assertTrue(updatedMovie.isLoaned());
    }

    @Test
    void borrowMovie_ShouldReturnConflict_WhenAlreadyLoaned() throws Exception {
        User alice = userRepo.save(
                new User(
                        "alice",
                        passwordEncoder.encode("password")
                )
        );

        Director director = new Director("Ridley Scott");
        Movie movie = movieRepository.save(new Movie("Blade Runner", director, "Available", alice));
        movie.setLoaned(true);
        movie = movieRepository.save(movie);

        mockMvc.perform(post("/api/v1/loans/" + movie.getId())
                        .param("borrowerName", "John Doe"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Already loaned"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/api/v1/loans/" + movie.getId()))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void borrowMovie_ShouldReturnNotFound_WhenMovieDoesNotExist() throws Exception {

        mockMvc.perform(post("/api/v1/loans/1111")
                        .param("borrowerName", "Jane Doe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/api/v1/loans/1111"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void returnedMovie_ShouldBePossibleToLoanAgain() throws Exception {
        User alice = userRepo.save(
                new User(
                        "alice",
                        passwordEncoder.encode("password")
                )
        );

        Director director = new Director("James Cameron");

        Movie movie = movieRepository.save(
                new Movie("Avatar", director, "Available", alice)
        );

        mockMvc.perform(post("/api/v1/loans/" + movie.getId())
                        .param("borrowerName", "John"))
                .andExpect(status().isCreated());

        Loan firstLoan = loanRepository.findAll().get(0);

        mockMvc.perform(put("/api/v1/loans/" + firstLoan.getId() + "/return"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/loans/" + movie.getId())
                        .param("borrowerName", "Jane"))
                .andExpect(status().isCreated());

        List<Loan> loans = loanRepository.findAll();

        assertEquals(2, loans.size());

        Loan secondLoan = loans.get(1);

        assertNotEquals(firstLoan.getId(), secondLoan.getId());

        assertNull(secondLoan.getReturnDate());

        Movie updatedMovie = movieRepository.findById(movie.getId()).get();

        assertTrue(updatedMovie.isLoaned());
    }

    @Test
    void returnMovie_ShouldReturn200AndSetReturnDate() throws Exception {
        User alice = userRepo.save(
                new User(
                        "alice",
                        passwordEncoder.encode("password")
                )
        );

        Director director = new Director("Ridley Scott");

        Movie movie = movieRepository.save(
                new Movie("Alien", director, "Available", alice)
        );

        mockMvc.perform(post("/api/v1/loans/" + movie.getId())
                        .param("borrowerName", "John"))
                .andExpect(status().isCreated());

        Loan loan = loanRepository.findAll().get(0);

        mockMvc.perform(put("/api/v1/loans/" + loan.getId() + "/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loan.getId()))
                .andExpect(jsonPath("$.borrowerName").value("John"))
                .andExpect(jsonPath("$.movieTitle").value("Alien"));

        Loan updatedLoan = loanRepository.findById(loan.getId()).get();

        assertNotNull(updatedLoan.getReturnDate());

        Movie updatedMovie = movieRepository.findById(movie.getId()).get();

        assertFalse(updatedMovie.isLoaned());
    }

    @Test
    void loanMovie_ShouldReturn409_whenMovieIsAlreadyLoaned() throws Exception {
        User alice = userRepo.save(
                new User(
                        "alice",
                        passwordEncoder.encode("password")
                )
        );

        Director director = new Director("Christopher Nolan");
        Movie movie = movieRepository.save(
                new Movie("Inception", director, "Available", alice));

        mockMvc.perform(post("/api/v1/loans/" + movie.getId())
                .param("borrowerName", "John Doe"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/loans/" + movie.getId())
                .param("borrowerName", "Kostas"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(movie.getId() + " is already loaned"));
    }
}