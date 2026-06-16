USE cinema;

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
