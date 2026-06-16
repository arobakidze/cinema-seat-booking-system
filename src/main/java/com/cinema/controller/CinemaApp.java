package com.cinema.controller;

import com.cinema.domain.Movie;
import com.cinema.domain.MovieSession;
import com.cinema.service.MovieService;
import com.cinema.service.MovieSessionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CinemaApp {
    private static final Logger LOGGER = LogManager.getLogger(CinemaApp.class);

    private final MovieService movieService;
    private final MovieSessionService sessionService;
    private final Scanner scanner = new Scanner(System.in);

    public CinemaApp(MovieService movieService,
                     MovieSessionService sessionService) {
        this.movieService = movieService;
        this.sessionService = sessionService;
    }

    public void start() {
        while (true) {
            printMainMenu();

            int choice = readInt();

            switch (choice) {
                case 1 -> moviesMenu();
                case 2 -> {
                    LOGGER.info("project got closed");
                    System.out.println("👋 Goodbye!");
                    return;
                }
                default -> {
                    LOGGER.error("Invalid choice on cinema menu");
                    System.out.println("❌ Invalid option");
                }
            }
        }
    }

    // ---------------- MAIN MENU ----------------
    private void printMainMenu() {
        LOGGER.info("cinema menu shown");
        System.out.println("\n🎬 === CINEMA SYSTEM ===");
        System.out.println("1. View Movies");
        System.out.println("2. Exit");
        System.out.print("Choose: ");
    }

    // ---------------- MOVIES ----------------
    private void moviesMenu() {

        while (true) {

            List<Movie> movies = movieService.getAllMovies();

            LOGGER.info("movies menu shown");
            System.out.println("\n🎥 === MOVIES ===");

            for (Movie movie : movies) {
                LOGGER.info("movie: {}", movie.getTitle());
                System.out.println(
                        movie.toString()
                );
            }

            System.out.println("0. Back");
            System.out.print("Choose movie: ");

            int choice = readInt();

            if (choice == 0) {
                return;
            }

            if (choice < 1 || choice > movies.size()) {
                LOGGER.error("Invalid choice on movies menu");
                System.out.println("❌ Invalid choice");
                continue;
            }

            Movie selectedMovie = movies.get(choice - 1);

            sessionsMenu(selectedMovie.getId());
        }
    }

    // ---------------- SESSIONS ----------------
    private void sessionsMenu(long movieId) {

        List<MovieSession> sessions = sessionService.getSessionsByMovie(movieId);

        if (sessions == null || sessions.isEmpty()) {
            LOGGER.info("No sessions found");
            System.out.println("⚠ No sessions available.");
            return;
        }

        while (true) {

            LOGGER.info("session menu shown");
            System.out.println("\n🎞 === SESSIONS ===");

            List<Long> ids = new ArrayList<>();

            for (MovieSession s : sessions) {
                ids.add(s.getId());
                LOGGER.info("{}. movie: {}, hall: {}, start time: {}", s.getId(), movieService.getMovie(movieId).getTitle(), s.getHallId(), s.getStartTime());
                System.out.println(s.getId() + ". movie: "
                        + movieService.getMovie(movieId).getTitle() +
                        ", hall: " + s.getHallId() +
                        ",start time: " + s.getStartTime());
            }

            System.out.println("0. Back");
            System.out.print("Select session: ");

            boolean isValideSessionId = false;
            long sessionId = readLong();
            for (Long id : ids) {
                if (id == sessionId) {
                    isValideSessionId = true;
                    break;
                }
            }

            if (!isValideSessionId && sessionId != 0) {
                LOGGER.error("Invalid choice on sessions menu");
                System.out.println("❌ Invalid choice");
                continue;
            }
            else if (sessionId == 0) {
                return;
            }

            System.out.println("\n✅ Selected session ID: " + sessionId);
            bookingModule(movieId, sessionId);
            return;
        }
    }

    public void bookingModule(long movieId, long sessionId) {
        System.out.println("➡ Handing over to booking module...");
        System.exit(0);
    }

    // ---------------- SAFE INPUT ----------------
    private int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("❌ Enter a number!");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private long readLong() {
        while (!scanner.hasNextLong()) {
            System.out.println("❌ Enter a valid number!");
            scanner.next();
        }
        return scanner.nextLong();
    }
}