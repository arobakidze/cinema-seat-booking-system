package com.cinema.controller;

import com.cinema.domain.Movie;
import com.cinema.domain.MovieSession;
import com.cinema.domain.Seat;
import com.cinema.service.MovieService;
import com.cinema.service.MovieSessionService;
import com.cinema.service.ReservationService;
import com.cinema.service.SeatAllocationService;
import com.cinema.ui.ConsoleMenu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CinemaApp {

    private static final Logger LOGGER = LogManager.getLogger(CinemaApp.class);

    private final MovieService movieService;
    private final MovieSessionService sessionService;
    private final ReservationService reservationService;
    private final SeatAllocationService seatAllocationService;
    private final Scanner scanner = new Scanner(System.in);

    public CinemaApp(MovieService movieService,
                     MovieSessionService sessionService,
                     ReservationService reservationService,
                     SeatAllocationService seatAllocationService) {
        this.movieService = movieService;
        this.sessionService = sessionService;
        this.reservationService = reservationService;
        this.seatAllocationService = seatAllocationService;
    }

    public void start() {
        while (true) {
            printMainMenu();

            int choice = readInt();

            switch (choice) {
                case 1:
                    moviesMenu();
                    break;

                case 2:
                    openBookingMenuWithoutMovieSelection();
                    break;

                case 3:
                    allAvailableSessionsMenu();
                    break;

                case 0:
                    LOGGER.info("Project closed");
                    System.out.println("Goodbye! ");
                    return;

                default:
                    LOGGER.error("Invalid choice on cinema menu");
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }

    private void printMainMenu() {
        LOGGER.info("Cinema menu shown");
        System.out.println();
        System.out.println("=== CINEMA SYSTEM ===");
        System.out.println("1. View movies and choose session \uD83C\uDF7F ");
        System.out.println("2. Open booking menu by session ID \uD83C\uDF9F\uFE0F");
        System.out.println("3. View available sessions and seats \uD83C\uDF7F ");
        System.out.println("0. Exit \uD83E\uDD7A");
        System.out.print("Choose: ");
    }

    private void allAvailableSessionsMenu() {
        while (true) {
            List<MovieSession> sessions = sessionService.findAll();

            LOGGER.info("available sessions menu shown");
            System.out.println("\n🎥 === AVAILABLE SESSIONS ===");
            List<Long> sessionIds = new ArrayList<>();

            for (MovieSession s : sessions) {
                List<Seat> availableSeats = seatAllocationService.listAvailableSeats(s.getId());
                for (Seat seat : availableSeats) {
                    if (!seat.getIsBooked()) {
                        sessionIds.add(s.getId());
                        LOGGER.info("{}. movie: {}, hall: {}, start time: {}", s.getId(), movieService.getMovie(s.getMovieId()).getTitle(), s.getHallId(), s.getStartTime());

                        System.out.println(s.getId() + ". movie: {"
                                + movieService.getMovie(s.getMovieId()).getTitle() +
                                "}, hall: {" + s.getHallId()
                                + "}, there's {" + availableSeats.size() + "} available seats"
                                + ",start time: {" + s.getStartTime() + "}");
                        break;
                    }
                }

            }
            System.out.println("0. Back");
            System.out.print("Choose sessions: ");

            Long choice = readLong();

            if (choice == 0) {
                return;
            }

            if (!sessionIds.contains(choice)) {
                LOGGER.error("Invalid session choice");
                System.out.println("Invalid session ID.");
                continue;
            }

            availableSeatsMenu(choice);
        }
    }

    public void availableSeatsMenu(Long sessionId) {

        MovieSession session = sessionService.findById(sessionId);

        System.out.println("Available seats for " + movieService.getMovie(session.getMovieId()).getTitle());
        System.out.println("Available seats: ");
        List<Seat> availableSeats = seatAllocationService.listAvailableSeats(sessionId);
        for (Seat seat : availableSeats) {
            if (seat.getIsBooked()) continue;
            System.out.println(seat.getId() + ". hall-{" + seat.getHallId() + "}, seat number-{" + seat.getSeatNumber() + "}, row number-{" + seat.getRowNum() + "}");
        }

        System.out.println("type 0 to go back");
        Long choice = readLong();

        if (choice == 0) {
        }
    }

    private void moviesMenu() {
        while (true) {
            List<Movie> movies = movieService.getAllMovies();

            LOGGER.info("Movies menu shown");
            System.out.println();
            System.out.println("=== \uD83C\uDF7F MOVIES \uD83C\uDF7F ===");

            if (movies == null || movies.isEmpty()) {
                System.out.println("No movies found.");
                return;
            }

            for (int i = 0; i < movies.size(); i++) {
                Movie movie = movies.get(i);
                System.out.println((i + 1) + ". " + movie.getTitle()
                        + " (" + movie.getDurationMinutes() + " min)"
                        + " Genres: " + movie.getGenres());
            }

            System.out.println("0. Back");
            System.out.print("Choose movie: ");

            int choice = readInt();

            if (choice == 0) {
                return;
            }

            if (choice < 1 || choice > movies.size()) {
                LOGGER.error("Invalid choice on movies menu");
                System.out.println("Invalid movie choice.");
                continue;
            }

            Movie selectedMovie = movies.get(choice - 1);
            sessionsMenu(selectedMovie);
        }
    }

    private void sessionsMenu(Movie selectedMovie) {
        while (true) {
            List<MovieSession> sessions = sessionService.getSessionsByMovie(selectedMovie.getId());

            if (sessions == null || sessions.isEmpty()) {
                LOGGER.info("No sessions found for movie {}", selectedMovie.getTitle());
                System.out.println("No sessions available for this movie.");
                return;
            }

            LOGGER.info("Sessions menu shown");
            System.out.println();
            System.out.println("=== SESSIONS FOR: " + selectedMovie.getTitle() + " \uD83C\uDFA5 ===");

            List<Long> sessionIds = new ArrayList<>();

            for (MovieSession session : sessions) {
                sessionIds.add(session.getId());

                System.out.println(
                        "Session ID " + session.getId()
                                + " | Hall: " + session.getHallId()
                                + " | Start time: " + session.getStartTime()
                );
            }

            System.out.println("0. Back");
            System.out.print("Enter one of the shown session IDs: ");

            long sessionId = readLong();

            if (sessionId == 0) {
                return;
            }

            if (!sessionIds.contains(sessionId)) {
                LOGGER.error("Invalid session choice");
                System.out.println("Invalid session ID.");
                continue;
            }

            bookingModule(sessionId);
        }
    }

    private void bookingModule(Long sessionId) {
        LOGGER.info("Booking module opened for session {}", sessionId);

        ConsoleMenu consoleMenu = new ConsoleMenu(
                reservationService,
                seatAllocationService,
                scanner
        );

        consoleMenu.runForSession(sessionId);
    }

    private void openBookingMenuWithoutMovieSelection() {
        ConsoleMenu consoleMenu = new ConsoleMenu(
                reservationService,
                seatAllocationService,
                scanner
        );

        consoleMenu.run();
    }

    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    private long readLong() {
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}