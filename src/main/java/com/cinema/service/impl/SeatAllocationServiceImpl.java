package com.cinema.service.impl;

import com.cinema.persistence.mybatis.dao.MovieDao;
import com.cinema.persistence.mybatis.dao.ReservationSeatDao;
import com.cinema.persistence.mybatis.dao.SeatDao;

public class SeatAllocationServiceImpl {
    private ReservationSeatDao seatDao;
    public SeatAllocationServiceImpl(ReservationSeatDao seatDao) {
        this.seatDao = seatDao;
    }

}
