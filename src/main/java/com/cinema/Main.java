package com.cinema;

import com.cinema.service.ReservationService;
import com.cinema.service.SeatAllocationService;
import com.cinema.service.impl.ReservationServiceImpl;
import com.cinema.service.impl.SeatAllocationServiceImpl;
import com.cinema.ui.ConsoleMenu;

public class Main {

    public static void main(String[] args) {
        ReservationService reservationService = new ReservationServiceImpl();
        SeatAllocationService seatAllocationService = new SeatAllocationServiceImpl(reservationService);

        ConsoleMenu consoleMenu = new ConsoleMenu(reservationService, seatAllocationService);
        consoleMenu.run();
    }
}
