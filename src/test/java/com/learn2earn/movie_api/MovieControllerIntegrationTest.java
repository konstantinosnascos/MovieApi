package com.learn2earn.movie_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
