package com.learn2earn.movie_api.dto;

public record MovieResponseV2DTO(String id, String title, com.learn2earn.movie_api.model.Director director, String status, double rating) {
}
