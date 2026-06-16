package com.cinema;

import com.cinema.controller.CinemaApp;
import com.cinema.persistance.mybatis.dao.MovieDao;
import com.cinema.persistance.mybatis.dao.MovieSessionDao;
import com.cinema.service.MovieService;
import com.cinema.service.MovieSessionService;
import com.cinema.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

public class Main {

    public static void main(String[] args) {

        try (SqlSession session = MyBatisUtil.getSession()) {

            MovieDao movieDao =
                    session.getMapper(MovieDao.class);

            MovieSessionDao sessionDao =
                    session.getMapper(MovieSessionDao.class);

            MovieService movieService =
                    new MovieService(movieDao);

            MovieSessionService movieSessionService =
                    new MovieSessionService(sessionDao);

            CinemaApp app =
                    new CinemaApp(movieService, movieSessionService);

            app.start();
        }
    }
}