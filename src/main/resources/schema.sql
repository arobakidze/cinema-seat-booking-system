CREATE
DATABASE IF NOT EXISTS cinema;
USE
cinema;

DROP TABLE IF EXISTS reservation_seats;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS movie_sessions;
DROP TABLE IF EXISTS seats;
DROP TABLE IF EXISTS movies;
DROP TABLE IF EXISTS cinema_halls;

CREATE TABLE cinema_halls
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    total_rows    INT          NOT NULL,
    seats_per_row INT          NOT NULL
);

CREATE TABLE seats
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    hall_id     BIGINT NOT NULL,
    row_num     INT    NOT NULL,
    seat_number INT    NOT NULL,
    is_booked   BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (hall_id) REFERENCES cinema_halls (id)
);

CREATE TABLE movies
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    duration_minutes INT          NOT NULL,
    genres           VARCHAR(255) NOT NULL
);

CREATE TABLE movie_sessions
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id   BIGINT   NOT NULL,
    hall_id    BIGINT   NOT NULL,
    start_time DATETIME NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies (id),
    FOREIGN KEY (hall_id) REFERENCES cinema_halls (id)
);

CREATE TABLE reservations
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id    BIGINT       NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    FOREIGN KEY (session_id) REFERENCES movie_sessions (id)
);

CREATE TABLE reservation_seats
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_id BIGINT NOT NULL,
    seat_id        BIGINT NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservations (id),
    FOREIGN KEY (seat_id) REFERENCES seats (id)
);