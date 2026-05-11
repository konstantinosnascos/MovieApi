package com.learn2earn.movie_api.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String borrowerName;
    private LocalDate loanedDate;
    private LocalDate returnDate;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    protected Loan() {
    }

    public Loan(String borrowerName, Movie movie) {
        this.borrowerName = borrowerName;
        this.movie = movie;
    }


}
