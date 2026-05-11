package com.learn2earn.movie_api.dto;

public record MovieResponseV2DTO(Long id, String title, String director, String status, double rating, boolean isLoaned) {
}
