package com.learn2earn.movie_api.dto;

public record MovieResponseDTO(String id, String title, com.learn2earn.movie_api.model.Director director, String status) {
}
//Just for lab of v1 and v2 we leave out rating and add it in v2