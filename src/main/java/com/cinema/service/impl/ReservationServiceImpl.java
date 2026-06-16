package com.cinema.service.impl;

import com.cinema.persistence.mybatis.dao.MovieDao;
import com.cinema.persistence.mybatis.dao.ReservationDao;

public class ReservationServiceImpl {

    private ReservationDao reservationDao;

    public ReservationServiceImpl(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

}
