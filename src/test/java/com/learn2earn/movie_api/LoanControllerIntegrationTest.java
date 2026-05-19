package com.learn2earn.movie_api;
import com.learn2earn.movie_api.exception.ErrorResponse;
import com.learn2earn.movie_api.exception.*;
import com.learn2earn.movie_api.model.Director;
import com.learn2earn.movie_api.model.Movie;
import com.learn2earn.movie_api.repository.DirectorRepository;
import com.learn2earn.movie_api.repository.LoanRepository;
import com.learn2earn.movie_api.repository.MovieRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @BeforeEach
    public void setup(){
        loanRepository.deleteAll();
        movieRepository.deleteAll();
        directorRepository.deleteAll();
    }

    @Test
    void borrowMovie_ShouldReturnCreatedAndSetLoanStatus() throws Exception {

        Director director = new Director("Ridley Scott");
        Movie movie = movieRepository.save(new Movie("Alien", director, "Available"));
        mockMvc.perform(post("/api/v1/loans/" + movie.getId())
                .param("borrowerName", "John Doe"))
                .andExpect(status().isCreated());

        Movie updatedMovie = movieRepository.findById(movie.getId()).get();
        assertTrue(updatedMovie.isLoaned());
    }

    @Test
    void borrowMovie_ShouldReturnConflict_WhenAlreadyLoaned() throws Exception {

        Director director = new Director("Ridley Scott");
        Movie movie = movieRepository.save(new Movie("Blade Runner", director, "Available"));
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
}