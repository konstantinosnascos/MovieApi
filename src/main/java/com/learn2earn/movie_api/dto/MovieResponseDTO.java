package com.learn2earn.movie_api.dto;

public record MovieResponseDTO(Long id, String title, String director, String status, boolean isLoaned) {
}
//Just for lab of v1 and v2 we leave out rating and add it in v2