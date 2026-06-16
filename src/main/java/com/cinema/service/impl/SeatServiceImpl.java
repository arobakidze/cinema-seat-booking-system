package com.cinema.service.impl;

import com.cinema.domain.Seat;
import com.cinema.persistence.mybatis.dao.SeatDao;
import com.cinema.service.SeatService;

import java.util.List;

public class SeatServiceImpl implements SeatService {

    private SeatDao seatDao;

    public void setSeatDao(SeatDao seatDao) {
        this.seatDao = seatDao;
    }

    @Override
    public List<Seat> findAll() {
        return seatDao.findAll();
    }

    @Override
    public Seat findById(Long id) {
        return seatDao.findById(id);
    }

    @Override
    public List<Seat> findByHallId(Long hallId) {
        return seatDao.findByHallId(hallId);
    }

    @Override
    public List<Seat> findAvailableBySessionId(Long sessionId) {
        return seatDao.findAvailableBySessionId(sessionId);
    }

    @Override
    public void insert(Seat seat) {
        seatDao.insert(seat);
    }

    @Override
    public void update(Seat seat) {
        seatDao.update(seat);
    }

    @Override
    public void delete(Long id) {
        seatDao.delete(id);
    }
}
