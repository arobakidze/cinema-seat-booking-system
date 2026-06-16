package com.cinema.service;

import com.cinema.domain.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    Reservation bookSeats(Long sessionId, String customerName, List<Long> seatIds);

    void cancelReservation(Long reservationId);

    Optional<Reservation> findById(Long reservationId);

    List<Reservation> listBySession(Long sessionId);
}
