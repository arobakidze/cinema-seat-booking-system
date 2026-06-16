package com.cinema.service;

import com.cinema.domain.MovieSession;
import com.cinema.persistence.mybatis.dao.MovieSessionDao;

import java.util.List;

public class MovieSessionService {

    private final MovieSessionDao movieSessionDao;

    public MovieSessionService(MovieSessionDao movieSessionDao) {
        this.movieSessionDao = movieSessionDao;
    }

    public List<MovieSession> findAll() {
        return movieSessionDao.findAll();
    }

    public MovieSession findById(Long id) {
        return movieSessionDao.findById(id);
    }

    public List<MovieSession> findByMovieId(Long movieId) {
        return movieSessionDao.findByMovieId(movieId);
    }

    // Method name used by CinemaApp
    public List<MovieSession> getSessionsByMovie(Long movieId) {
        return findByMovieId(movieId);
    }
}