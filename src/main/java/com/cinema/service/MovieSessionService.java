package com.cinema.service;

import com.cinema.domain.MovieSession;
import com.cinema.persistence.mybatis.dao.MovieSessionDao;

import java.util.List;

public class MovieSessionService {
    private final MovieSessionDao sessionDao;

    public MovieSessionService(MovieSessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    public List<MovieSession> getSessionsByMovie(Long movieId) {
        return sessionDao.findByMovieId(movieId);
    }

    public MovieSession getSession(Long id) {
        return sessionDao.findById(id);
    }

    public List<MovieSession> getAllSessions() { return sessionDao.findAll();}
}
