package com.learn2earn.movie_api.repository;

import com.learn2earn.movie_api.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {
    Optional<Director> findByName(String director);
}
