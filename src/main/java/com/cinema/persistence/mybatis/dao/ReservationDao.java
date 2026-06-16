package com.cinema.persistence.mybatis.dao;

import com.cinema.domain.Reservation;

import java.util.List;

public interface ReservationDao {

    void insert(Reservation reservation);

    Reservation findById(Long id);

    List<Reservation> findBySessionId(Long sessionId);

    void delete(Long id);
}
