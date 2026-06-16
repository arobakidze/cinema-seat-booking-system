package com.cinema.service;

import com.cinema.domain.Reservation;
import com.cinema.domain.Seat;

import java.util.List;

public interface SeatAllocationService {

    List<Seat> listAvailableSeats(Long sessionId);

    List<Seat> suggestBestSeats(Long sessionId, int groupSize);

    Reservation bookBestAvailableSeats(Long sessionId, String customerName, int groupSize);
}
