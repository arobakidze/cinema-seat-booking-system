package com.cinema.persistance.mybatis.dao;

import com.cinema.domain.MovieSession;

import java.util.List;

public interface MovieSessionDao {

    List<MovieSession> findByMovieId(Long movieId);
    MovieSession findById(Long id);
}
