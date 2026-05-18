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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public LocalDate getLoanedDate() {
        return loanedDate;
    }

    public void setLoanedDate(LocalDate loanedDate) {
        this.loanedDate = loanedDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
