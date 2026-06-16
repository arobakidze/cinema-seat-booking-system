package com.cinema.persistance.mybatis.dao;

import com.cinema.domain.Movie;
import java.util.List;

public interface MovieDao {

    List<Movie> findAll();
    Movie findById(Long id);
}
