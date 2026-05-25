package com.learn2earn.movie_api.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "movies")
public class Movie {
    //Movie attributes
    //ADD imdb rating
    //add where to watch?

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false)
    private String title;

    @Column(unique = false)
    private String status; //WATCHED/PLAN-TO-WATCH

    @Column(unique = false)
    private double rating;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "director_id", nullable = false)
    private Director director;

    @Column(unique = false)
    private boolean isLoaned;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    protected Movie() {
    }

    public Movie(String title, Director director, String status, User owner) {
        this.title = title;
        this.director = director;
        this.status = status;
        //Default rating we can consider 0;
        this.rating = 0;
        isLoaned = false;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getOwner() {
        return owner;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isLoaned()
    { return isLoaned;
    }
    public void setLoaned(boolean loaned) {
        isLoaned = loaned;
    }
}
