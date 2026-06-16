package com.cinema.service;

import com.cinema.domain.Movie;
import com.cinema.persistence.mybatis.dao.MovieDao;

import java.util.List;

public class MovieService {

    private final MovieDao movieDao;

    public MovieService(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    public List<Movie> findAll() {
        return movieDao.findAll();
    }

    public Movie findById(Long id) {
        return movieDao.findById(id);
    }

    // Method name used by CinemaApp
    public List<Movie> getAllMovies() {
        return findAll();
    }

    // Method name used by CinemaApp
    public Movie getMovie(Long id) {
        return findById(id);
    }
}