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
    private final Scanner scanner;

    public ConsoleMenu(ReservationService reservationService,
                       SeatAllocationService seatAllocationService) {
        this(reservationService, seatAllocationService, new Scanner(System.in));
    }

    public ConsoleMenu(ReservationService reservationService,
                       SeatAllocationService seatAllocationService,
                       Scanner scanner) {
        this.reservationService = reservationService;
        this.seatAllocationService = seatAllocationService;
        this.scanner = scanner;
    }

    public void run() {
        runMenu(null);
    }

    public void runForSession(Long sessionId) {
        runMenu(sessionId);
    }

    private void runMenu(Long selectedSessionId) {
        boolean running = true;

        while (running) {
            printMenu(selectedSessionId);
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        handleListAvailableSeats(selectedSessionId);
                        break;
                    case "2":
                        handleBookSpecificSeats(selectedSessionId);
                        break;
                    case "3":
                        handleSuggestBestSeats(selectedSessionId);
                        break;
                    case "4":
                        handleBookBestSeats(selectedSessionId);
                        break;
                    case "5":
                        handleCancelReservation();
                        break;
                    case "6":
                        handleFindReservation();
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Unknown option. Please try again.");
                        break;
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println();
        }
    }

    private void printMenu(Long selectedSessionId) {
        System.out.println();
        System.out.println("\uD83C\uDF9E\uFE0F Booking Menu \uD83C\uDF9E\uFE0F");

        if (selectedSessionId != null) {
            System.out.println("Selected session ID: " + selectedSessionId);
        }

        System.out.println("1. List available seats");
        System.out.println("2. Book specific seats");
        System.out.println("3. Suggest best seats for a group");
        System.out.println("4. Book best available seats for a group");
        System.out.println("5. Cancel reservation");
        System.out.println("6. Find reservation by ID");
        System.out.println("0. Back");
    }

    private void handleListAvailableSeats(Long selectedSessionId) {
        Long sessionId = resolveSessionId(selectedSessionId);
        List<Seat> availableSeats = seatAllocationService.listAvailableSeats(sessionId);

        if (availableSeats.isEmpty()) {
            System.out.println("No available seats for this session.");
            return;
        }

        availableSeats.forEach(seat -> System.out.println(formatSeat(seat)));
    }

    private void handleBookSpecificSeats(Long selectedSessionId) {
        Long sessionId = resolveSessionId(selectedSessionId);

        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine().trim();

        System.out.print("Enter seat IDs separated by commas, example 1,2,3: ");
        List<Long> seatIds = parseIdList(scanner.nextLine());

        Reservation reservation = reservationService.bookSeats(sessionId, customerName, seatIds);
        printReservation(reservation);
    }

    private void handleSuggestBestSeats(Long selectedSessionId) {
        Long sessionId = resolveSessionId(selectedSessionId);
        int groupSize = readInt("Enter group size: ");

        List<Seat> suggestedSeats = seatAllocationService.suggestBestSeats(sessionId, groupSize);

        if (suggestedSeats.isEmpty()) {
            System.out.println("No consecutive seats found for this group size.");
            return;
        }

        System.out.println("Suggested seats:");
        suggestedSeats.forEach(seat -> System.out.println(formatSeat(seat)));
    }

    private void handleBookBestSeats(Long selectedSessionId) {
        Long sessionId = resolveSessionId(selectedSessionId);

        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine().trim();

        int groupSize = readInt("Enter group size: ");

        Reservation reservation = seatAllocationService.bookBestAvailableSeats(
                sessionId,
                customerName,
                groupSize
        );

        printReservation(reservation);
    }

    private void handleCancelReservation() {
        Long reservationId = readLong("Enter reservation ID to cancel: ");
        reservationService.cancelReservation(reservationId);
        System.out.println("Reservation cancelled successfully.");
    }

    private void handleFindReservation() {
        Long reservationId = readLong("Enter reservation ID: ");
        Optional<Reservation> reservation = reservationService.findById(reservationId);

        if (reservation.isPresent()) {
            printReservation(reservation.get());
        } else {
            System.out.println("Reservation not found.");
        }
    }

    private Long resolveSessionId(Long selectedSessionId) {
        if (selectedSessionId != null) {
            return selectedSessionId;
        }

        return readLong("Enter session ID: ");
    }

    private Long readLong(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
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
        System.out.println("Reservation:");
        System.out.println("Reservation ID: " + reservation.getId());
        System.out.println("Use this Reservation ID to find or cancel the reservation.");
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