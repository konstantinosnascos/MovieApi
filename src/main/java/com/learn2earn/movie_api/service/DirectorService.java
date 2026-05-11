package com.learn2earn.movie_api.service;

import com.learn2earn.movie_api.model.Director;
import com.learn2earn.movie_api.repository.DirectorRepository;
import org.springframework.stereotype.Service;

@Service
public class DirectorService {
    private final DirectorRepository directorRepository;

    public DirectorService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public void createDirector(String name){
        directorRepository.save(new Director(name));
    }
}
