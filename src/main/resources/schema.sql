CREATE
DATABASE IF NOT EXISTS cinema;
USE
cinema;

CREATE TABLE movies
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    title            VARCHAR(255) NOT NULL,
    duration_minutes INT          NOT NULL,
    genres           VARCHAR(255) NOT NULL
);

CREATE TABLE movie_sessions
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    movie_id   BIGINT   NOT NULL,
    hall_id    BIGINT   NOT NULL,
    start_time DATETIME NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies (id)
--     FOREIGN KEY (hall_id) REFERENCES cinema_halls (id)
);
