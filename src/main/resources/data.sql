USE
cinema;

INSERT INTO cinema_halls (name, total_rows, seats_per_row)
VALUES ('Hall 1', 10, 12),
       ('Hall 2', 8, 10),
       ('Hall 3', 12, 15);

INSERT INTO seats (hall_id, row_num, seat_number, is_booked)
SELECT h.id,
       r.row_num,
       s.seat_number,
       FALSE
FROM cinema_halls h
         JOIN (SELECT 1 AS row_num
               UNION ALL
               SELECT 2
               UNION ALL
               SELECT 3
               UNION ALL
               SELECT 4
               UNION ALL
               SELECT 5
               UNION ALL
               SELECT 6
               UNION ALL
               SELECT 7
               UNION ALL
               SELECT 8
               UNION ALL
               SELECT 9
               UNION ALL
               SELECT 10
               UNION ALL
               SELECT 11
               UNION ALL
               SELECT 12) r
         JOIN (SELECT 1 AS seat_number
               UNION ALL
               SELECT 2
               UNION ALL
               SELECT 3
               UNION ALL
               SELECT 4
               UNION ALL
               SELECT 5
               UNION ALL
               SELECT 6
               UNION ALL
               SELECT 7
               UNION ALL
               SELECT 8
               UNION ALL
               SELECT 9
               UNION ALL
               SELECT 10
               UNION ALL
               SELECT 11
               UNION ALL
               SELECT 12
               UNION ALL
               SELECT 13
               UNION ALL
               SELECT 14
               UNION ALL
               SELECT 15) s
WHERE r.row_num <= h.total_rows
  AND s.seat_number <= h.seats_per_row;

INSERT INTO movies (title, duration_minutes, genres)
VALUES ('Deadpool & Wolverine', 128, 'Action, Comedy, Superhero'),
       ('Inside Out 2', 96, 'Animation, Family, Comedy'),
       ('Oppenheimer', 180, 'Biography, Drama, History'),
       ('Past Lives', 106, 'Romance, Drama'),
       ('Backrooms', 110, 'Horror, Sci-Fi'),
       ('Michael', 130, 'Biography, Drama, History, Music'),
       ('Star Wars: The Mandalorian and Grogu', 140, 'Action, Adventure, Family, Fantasy, Sci-Fi'),
       ('Toy Story 5', 120, 'Adventure, Animation, Comedy, Drama, Family, Fantasy');

INSERT INTO movie_sessions (movie_id, hall_id, start_time)
VALUES (1, 1, '2026-06-15 11:00:00'),
       (1, 2, '2026-06-15 11:00:00'),
       (2, 3, '2026-06-15 11:00:00'),
       (2, 1, '2026-06-15 14:00:00'),
       (3, 2, '2026-06-15 14:00:00'),
       (4, 3, '2026-06-16 14:00:00'),
       (5, 1, '2026-06-16 17:00:00'),
       (6, 2, '2026-06-15 17:00:00'),
       (6, 3, '2026-06-15 17:00:00'),
       (7, 1, '2026-06-15 20:00:00'),
       (7, 2, '2026-06-15 20:30:00'),
       (8, 3, '2026-06-16 20:00:00'),
       (8, 1, '2026-06-16 03:00:00');