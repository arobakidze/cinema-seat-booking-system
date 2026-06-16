package com.cinema.service;

import com.cinema.domain.Movie;
import com.cinema.persistence.mybatis.dao.MovieDao;

import java.util.List;

public class MovieService {
    private final MovieDao movieDao;

    public MovieService(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    public List<Movie> getAllMovies() {
        return movieDao.findAll();
    }

    public Movie getMovie(Long id) {
        return movieDao.findById(id);
    }
}
