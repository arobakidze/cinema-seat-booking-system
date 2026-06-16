package com.cinema;

import com.cinema.controller.CinemaApp;
import com.cinema.persistence.mybatis.dao.MovieDao;
import com.cinema.persistence.mybatis.dao.MovieSessionDao;
import com.cinema.service.MovieService;
import com.cinema.service.MovieSessionService;
import com.cinema.service.ReservationService;
import com.cinema.service.SeatAllocationService;
import com.cinema.service.impl.ReservationServiceImpl;
import com.cinema.service.impl.SeatAllocationServiceImpl;
import org.apache.ibatis.session.SqlSession;

public class Main {

    public static void main(String[] args) {
        try (SqlSession session = MyBatisUtil.getSession()) {
            MovieDao movieDao = session.getMapper(MovieDao.class);
            MovieSessionDao movieSessionDao = session.getMapper(MovieSessionDao.class);

            MovieService movieService = new MovieService(movieDao);
            MovieSessionService movieSessionService = new MovieSessionService(movieSessionDao);

            ReservationService reservationService = new ReservationServiceImpl();
            SeatAllocationService seatAllocationService = new SeatAllocationServiceImpl(reservationService);

            CinemaApp app = new CinemaApp(
                    movieService,
                    movieSessionService,
                    reservationService,
                    seatAllocationService
            );

            app.start();
        }
    }
}