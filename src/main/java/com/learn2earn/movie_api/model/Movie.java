package com.learn2earn.movie_api.model;

import java.util.UUID;

public class Movie {
    //Movie attributes
    //ADD imdb rating
    //add where to watch?
    private String id;
    private String title;
    private String director;
    private String status; //WATCHED/PLAN-TO-WATCH
    private double rating;

    public Movie(String title, String director, String status) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.director = director;
        this.status = status;
        //Default rating we can consider 0;
        this.rating = 0;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
