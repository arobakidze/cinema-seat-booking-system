package com.cinema.persistence.mybatis.dao;

import com.cinema.domain.CinemaHall;

import java.util.List;

public interface HallDao {

    List<CinemaHall> findAll();

    CinemaHall findById(Long id);

    void insert(CinemaHall hall);

    void update(CinemaHall hall);

    void delete(Long id);
}
