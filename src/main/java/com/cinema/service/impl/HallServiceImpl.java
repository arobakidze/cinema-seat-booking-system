package com.cinema.service.impl;

import com.cinema.domain.CinemaHall;
import com.cinema.persistence.mybatis.dao.HallDao;
import com.cinema.service.HallService;
import java.util.List;

public class HallServiceImpl implements HallService {

    private HallDao hallDao;

    public void setHallDao(HallDao hallDao) {
        this.hallDao = hallDao;
    }

    @Override
    public List<CinemaHall> findAll() {
        return hallDao.findAll();
    }

    @Override
    public CinemaHall findById(Long id) {
        return hallDao.findById(id);
    }

    @Override
    public void insert(CinemaHall hall) {
        hallDao.insert(hall);
    }

    @Override
    public void update(CinemaHall hall) {
        hallDao.update(hall);
    }

    @Override
    public void delete(Long id) {
        hallDao.delete(id);
    }
}
