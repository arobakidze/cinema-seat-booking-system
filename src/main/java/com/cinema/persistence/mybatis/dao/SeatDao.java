package com.cinema.persistence.mybatis.dao;

import com.cinema.domain.Seat;
import java.util.List;

public interface SeatDao {

    List<Seat> findAll();

    Seat findById(Long id);

    List<Seat> findByHallId(Long hallId);

    List<Seat> findAvailableBySessionId(Long sessionId);

    void insert(Seat seat);

    void update(Seat seat);

    void delete(Long id);
}
