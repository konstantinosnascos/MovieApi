package com.learn2earn.movie_api.repository;

import com.learn2earn.movie_api.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
}
