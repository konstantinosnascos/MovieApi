/*package com.learn2earn.movie_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.*;


@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllMoviesReturns200() throws Exception {
        mockMvc.perform(get("/api/v1/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getPostValidMoviesReturns201() throws Exception {
        String jsonpayload = "{\"title\":\"The Matrix\",\"director\":\"\",\"status\":\"Released\"}";
        mockMvc.perform(post("/api/v1/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonpayload))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetNonExistentMovieReturns404() throws Exception {
        mockMvc.perform(get("/api/v1/movies/1234567890"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }
}
*/


package com.learn2earn.movie_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn2earn.movie_api.dto.MovieRequestDTO;
import com.learn2earn.movie_api.model.Director;
import com.learn2earn.movie_api.model.Movie;
import com.learn2earn.movie_api.model.User;
import com.learn2earn.movie_api.repository.DirectorRepository;
import com.learn2earn.movie_api.repository.LoanRepository;
import com.learn2earn.movie_api.repository.MovieRepository;
import com.learn2earn.movie_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepo;

    @BeforeEach
    void setup(){
        loanRepository.deleteAll();
        movieRepository.deleteAll();
        directorRepository.deleteAll();
    }

    @Test
    void createMovie_ShouldReturnCreated() throws Exception {
        User alice = userRepo.save(
                new User(
                        "alice",
                        passwordEncoder.encode("password")
                )
        );

        MovieRequestDTO request = new MovieRequestDTO("Inception", "Christopher Nolan", "Available");

        mockMvc.perform(post("/api/v1/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.director").value("Christopher Nolan"))
                .andExpect(jsonPath("$.status").value("Available"));
    }

    @Test
    void getMovies_ShouldReturnList() throws Exception {
        User alice = userRepo.save(
                new User(
                        "alice",
                        passwordEncoder.encode("password")
                )
        );
        Director director = new Director("Steven Spielberg");

        movieRepository.save(new Movie("Jurassic Park", director, "Available", alice));
        mockMvc.perform(get("/api/v1/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title").value("Jurassic Park"));
    }

    @Test
    void getMoviesById_ShouldReturnMovie() throws Exception {
        User alice = userRepo.save(
                new User(
                        "alice",
                        passwordEncoder.encode("password")
                )
        );
        Director director = new Director("Wachovskis");
        Movie movie= movieRepository.save(new Movie("The Matrix", director, "Available", alice));

        mockMvc.perform(get("/api/v1/movies/" + movie.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Matrix"));
    }

    @Test
    void getMovieById_ShouldReturn404_WhenNotFound() throws Exception {

        mockMvc.perform(get("/api/v1/movies/11111"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/api/v1/movies/11111"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deleteMovie_ShouldReturnNoContent() throws Exception {
        User alice = userRepo.save(
                new User(
                        "alice",
                        passwordEncoder.encode("password")
                )
        );
        Director director = new Director("George Lucas");
        Movie movie = movieRepository.save(new Movie("Star Wars", director, "Available", alice));
        mockMvc.perform(delete("/api/v1/movies/" + movie.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMovieById_ShouldReturn404_WhenNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/movies/11111/"))
                .andExpect(status().isNotFound());
    }

    //V2 example
    @Test
    void getMoviesV2_ShouldReturnListWithRating() throws Exception {
        User alice = userRepo.save(
                new User(
                        "alice",
                        passwordEncoder.encode("password")
                )
        );

        Director director = new Director("James Cameron");
        Movie movie = new Movie("Avatar", director, "Available", alice);
        movie.setRating(5);
        movieRepository.save(movie);
        mockMvc.perform(get("/api/v2/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].rating").value(5));


    }

    @Test
    public void testGetAllMoviesReturns200() throws Exception {
        mockMvc.perform(get("/api/v1/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testPostValidMoviesReturn201() throws Exception {
        User alice = userRepo.save(
                new User(
                        "alice",
                        passwordEncoder.encode("password")
                )
        );
        String jsonPayload = "{\"title\":\"The Matrix\",\"director\":\"Wachowskis\",\"status\":\"WATCHED\",\"alice}";
        mockMvc.perform(post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetNonExistentMovieReturn404() throws Exception {

        mockMvc.perform(get("/api/v1/movies/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/api/v1/movies/99999"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

}
