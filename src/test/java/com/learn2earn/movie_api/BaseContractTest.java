package com.learn2earn.movie_api;

import com.learn2earn.movie_api.controller.MovieController;
import com.learn2earn.movie_api.dto.MovieResponseDTO;
import com.learn2earn.movie_api.service.MovieService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.List;

@SpringBootTest
public abstract class BaseContractTest {

    @Autowired
    private MovieController movieController;

    @MockBean
    private MovieService movieService;

    @BeforeEach
    public void setup(String username){
        //we provide mock data so we can test our contract and get verifications
        Mockito.when(movieService.getAllMovies(username)).thenReturn(List.of(new MovieResponseDTO(
                1L,
                "Test Movie",
                "Test Director",
                "WATCHED",
                false)));

        RestAssuredMockMvc.standaloneSetup(movieController);
    }

}
