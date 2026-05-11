package com.learn2earn.movie_api.repository;

import com.learn2earn.movie_api.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>{

}



