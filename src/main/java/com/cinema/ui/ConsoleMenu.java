package com.cinema.ui;

import com.cinema.domain.Reservation;
import com.cinema.domain.Seat;
import com.cinema.service.ReservationService;
import com.cinema.service.SeatAllocationService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleMenu {

    private final ReservationService reservationService;
    private final SeatAllocationService seatAllocationService;

    public ConsoleMenu(ReservationService reservationService,
                       SeatAllocationService seatAllocationService) {
        this.reservationService = reservationService;
        this.seatAllocationService = seatAllocationService;
    }

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
                printMenu();
                System.out.print("Choose an option: ");
                String choice = scanner.nextLine().trim();

                try {
                    switch (choice) {
                        case "1":
                            handleListAvailableSeats(scanner);
                            break;
                        case "2":
                            handleBookSpecificSeats(scanner);
                            break;
                        case "3":
                            handleSuggestBestSeats(scanner);
                            break;
                        case "4":
                            handleBookBestSeats(scanner);
                            break;
                        case "5":
                            handleCancelReservation(scanner);
                            break;
                        case "6":
                            handleFindReservation(scanner);
                            break;
                        case "0":
                            running = false;
                            System.out.println("Goodbye.");
                            break;
                        default:
                            System.out.println("Unknown option. Please try again.");
                    }
                } catch (RuntimeException e) {
                    System.out.println("Error: " + e.getMessage());
                }

                System.out.println();
            }
        }
    }

    private void printMenu() {
        System.out.println("=== Cinema Seat Booking ===");
        System.out.println("1. List available seats by session ID");
        System.out.println("2. Book specific seats");
        System.out.println("3. Suggest best seats for a group");
        System.out.println("4. Book best available seats for a group");
        System.out.println("5. Cancel reservation");
        System.out.println("6. Find reservation by ID");
        System.out.println("0. Exit");
    }

    private void handleListAvailableSeats(Scanner scanner) {
        Long sessionId = readLong(scanner, "Enter session ID: ");
        List<Seat> availableSeats = seatAllocationService.listAvailableSeats(sessionId);

        if (availableSeats.isEmpty()) {
            System.out.println("No available seats for this session.");
            return;
        }

        availableSeats.forEach(seat -> System.out.println(formatSeat(seat)));
    }

    private void handleBookSpecificSeats(Scanner scanner) {
        Long sessionId = readLong(scanner, "Enter session ID: ");
        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine().trim();

        System.out.print("Enter seat IDs separated by commas (example: 1,2,3): ");
        List<Long> seatIds = parseIdList(scanner.nextLine());

        Reservation reservation = reservationService.bookSeats(sessionId, customerName, seatIds);
        printReservation(reservation);
    }

    private void handleSuggestBestSeats(Scanner scanner) {
        Long sessionId = readLong(scanner, "Enter session ID: ");
        int groupSize = readInt(scanner, "Enter group size: ");

        List<Seat> suggestedSeats = seatAllocationService.suggestBestSeats(sessionId, groupSize);
        if (suggestedSeats.isEmpty()) {
            System.out.println("No consecutive seats found for this group size.");
            return;
        }

        System.out.println("Suggested seats:");
        suggestedSeats.forEach(seat -> System.out.println(formatSeat(seat)));
    }

    private void handleBookBestSeats(Scanner scanner) {
        Long sessionId = readLong(scanner, "Enter session ID: ");
        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine().trim();
        int groupSize = readInt(scanner, "Enter group size: ");

        Reservation reservation = seatAllocationService.bookBestAvailableSeats(sessionId, customerName, groupSize);
        printReservation(reservation);
    }

    private void handleCancelReservation(Scanner scanner) {
        Long reservationId = readLong(scanner, "Enter reservation ID to cancel: ");
        reservationService.cancelReservation(reservationId);
        System.out.println("Reservation cancelled successfully.");
    }

    private void handleFindReservation(Scanner scanner) {
        Long reservationId = readLong(scanner, "Enter reservation ID: ");
        Optional<Reservation> reservation = reservationService.findById(reservationId);

        if (reservation.isPresent()) {
            printReservation(reservation.get());
        } else {
            System.out.println("Reservation not found.");
        }
    }

    private Long readLong(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return Long.parseLong(scanner.nextLine().trim());
    }

    private int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private List<Long> parseIdList(String raw) {
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(token -> !token.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    private String formatSeat(Seat seat) {
        return "Seat ID=" + seat.getId()
                + ", row=" + seat.getRowNum()
                + ", number=" + seat.getSeatNumber();
    }

    private void printReservation(Reservation reservation) {
        System.out.println("Reservation created/found:");
        System.out.println("Reservation ID: " + reservation.getId());
        System.out.println("Session ID: " + reservation.getSessionId());
        System.out.println("Customer: " + reservation.getCustomerName());

        if (reservation.getSeats() != null && !reservation.getSeats().isEmpty()) {
            System.out.println("Seats:");
            reservation.getSeats().forEach(seat ->
                    System.out.println("  - " + formatSeat(seat))
            );
        }
    }
}
