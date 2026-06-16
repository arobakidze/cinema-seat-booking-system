package com.cinema;

import com.cinema.controller.CinemaApp;
import com.cinema.persistence.mybatis.dao.*;
import com.cinema.service.*;
import com.cinema.service.impl.HallServiceImpl;
import com.cinema.service.impl.ReservationServiceImpl;
import com.cinema.service.impl.SeatAllocationServiceImpl;
import com.cinema.service.impl.SeatServiceImpl;
import com.cinema.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

public class Main {

    public static void main(String[] args) {

        try (SqlSession session = MyBatisUtil.getSession()) {

            MovieDao movieDao =
                    session.getMapper(MovieDao.class);

            MovieSessionDao sessionDao =
                    session.getMapper(MovieSessionDao.class);

            HallDao hallDao = session.getMapper(HallDao.class);
            SeatDao seatDao = session.getMapper(SeatDao.class);
            ReservationDao reservationDao =  session.getMapper(ReservationDao.class);
            ReservationSeatDao reservationSeatDao = session.getMapper(ReservationSeatDao.class);

            MovieService movieService =
                    new MovieService(movieDao);

            MovieSessionService movieSessionService =
                    new MovieSessionService(sessionDao);

            HallServiceImpl hallServiceImpl = new HallServiceImpl(hallDao);
            SeatServiceImpl seatServiceImpl = new SeatServiceImpl(seatDao);
            ReservationServiceImpl reservationService = new ReservationServiceImpl(reservationDao);
            SeatAllocationServiceImpl seatAllocationService = new SeatAllocationServiceImpl(reservationSeatDao);
            CinemaApp app =
                    new CinemaApp(movieService, movieSessionService, hallServiceImpl, seatServiceImpl,reservationService,seatAllocationService);

            app.start();
        }
    }
}
