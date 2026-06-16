package com.cinema.persistence.mybatis.dao;

import com.cinema.domain.ReservationSeat;
import com.cinema.domain.Seat;

import java.util.List;

public interface ReservationSeatDao {

    void insertBatch(List<ReservationSeat> items);

    List<Seat> findSeatsByReservationId(Long reservationId);

    void deleteByReservationId(Long reservationId);
}
