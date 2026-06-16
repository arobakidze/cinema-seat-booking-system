package com.cinema.service.impl;

import com.cinema.MyBatisUtil;
import com.cinema.domain.Reservation;
import com.cinema.domain.ReservationSeat;
import com.cinema.domain.Seat;
import com.cinema.persistence.mybatis.dao.ReservationDao;
import com.cinema.persistence.mybatis.dao.ReservationSeatDao;
import com.cinema.persistence.mybatis.dao.SeatDao;
import com.cinema.service.ReservationService;
import org.apache.ibatis.session.SqlSession;

import java.util.*;
import java.util.stream.Collectors;

public class ReservationServiceImpl implements ReservationService {

    @Override
    public Reservation bookSeats(Long sessionId, String customerName, List<Long> seatIds) {
        validateBookRequest(sessionId, customerName, seatIds);

        List<Long> distinctSeatIds = new ArrayList<>(new LinkedHashSet<>(seatIds));
        if (distinctSeatIds.size() != seatIds.size()) {
            throw new IllegalArgumentException("Duplicate seat IDs are not allowed.");
        }

        try (SqlSession session = MyBatisUtil.getSession()) {
            try {
                SeatDao seatDao = session.getMapper(SeatDao.class);
                ReservationDao reservationDao = session.getMapper(ReservationDao.class);
                ReservationSeatDao reservationSeatDao = session.getMapper(ReservationSeatDao.class);

                List<Seat> availableSeats = seatDao.findAvailableBySessionId(sessionId);
                Set<Long> availableSeatIds = availableSeats.stream()
                        .map(Seat::getId)
                        .collect(Collectors.toSet());

                if (!availableSeatIds.containsAll(distinctSeatIds)) {
                    throw new IllegalStateException("One or more requested seats are not available for this session.");
                }

                Reservation reservation = new Reservation();
                reservation.setSessionId(sessionId);
                reservation.setCustomerName(customerName.trim());
                reservationDao.insert(reservation);

                List<ReservationSeat> items = distinctSeatIds.stream()
                        .map(seatId -> new ReservationSeat(null, reservation.getId(), seatId))
                        .collect(Collectors.toList());

                reservationSeatDao.insertBatch(items);
                reservation.setSeats(reservationSeatDao.findSeatsByReservationId(reservation.getId()));

                session.commit();
                return reservation;
            } catch (RuntimeException e) {
                session.rollback();
                throw e;
            }
        }
    }

    @Override
    public void cancelReservation(Long reservationId) {
        if (reservationId == null || reservationId <= 0) {
            throw new IllegalArgumentException("Reservation ID must be positive.");
        }

        try (SqlSession session = MyBatisUtil.getSession()) {
            try {
                ReservationDao reservationDao = session.getMapper(ReservationDao.class);
                ReservationSeatDao reservationSeatDao = session.getMapper(ReservationSeatDao.class);

                Reservation reservation = reservationDao.findById(reservationId);
                if (reservation == null) {
                    throw new IllegalArgumentException("Reservation not found: " + reservationId);
                }

                reservationSeatDao.deleteByReservationId(reservationId);
                reservationDao.delete(reservationId);

                session.commit();
            } catch (RuntimeException e) {
                session.rollback();
                throw e;
            }
        }
    }

    @Override
    public Optional<Reservation> findById(Long reservationId) {
        if (reservationId == null || reservationId <= 0) {
            throw new IllegalArgumentException("Reservation ID must be positive.");
        }

        try (SqlSession session = MyBatisUtil.getSession()) {
            ReservationDao reservationDao = session.getMapper(ReservationDao.class);
            ReservationSeatDao reservationSeatDao = session.getMapper(ReservationSeatDao.class);

            Reservation reservation = reservationDao.findById(reservationId);
            if (reservation == null) {
                return Optional.empty();
            }

            reservation.setSeats(reservationSeatDao.findSeatsByReservationId(reservationId));
            return Optional.of(reservation);
        }
    }

    @Override
    public List<Reservation> listBySession(Long sessionId) {
        if (sessionId == null || sessionId <= 0) {
            throw new IllegalArgumentException("Session ID must be positive.");
        }

        try (SqlSession session = MyBatisUtil.getSession()) {
            ReservationDao reservationDao = session.getMapper(ReservationDao.class);
            ReservationSeatDao reservationSeatDao = session.getMapper(ReservationSeatDao.class);

            List<Reservation> reservations = reservationDao.findBySessionId(sessionId);
            reservations.forEach(reservation ->
                    reservation.setSeats(reservationSeatDao.findSeatsByReservationId(reservation.getId()))
            );
            return reservations;
        }
    }

    private void validateBookRequest(Long sessionId, String customerName, List<Long> seatIds) {
        if (sessionId == null || sessionId <= 0) {
            throw new IllegalArgumentException("Session ID must be positive.");
        }
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name must not be blank.");
        }
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("At least one seat ID must be provided.");
        }
        if (seatIds.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Seat ID list must not contain null values.");
        }
    }
}
