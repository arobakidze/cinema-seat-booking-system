USE cinema;

INSERT INTO cinema_halls (name, total_rows, seats_per_row) VALUES
                                                               ('Hall 1', 10, 12),
                                                               ('Hall 2', 8, 10),
                                                               ('Hall 3', 12, 15);

INSERT INTO seats (hall_id, row_num, seat_number, is_booked) VALUES
                                                                    (1, 1, 1, false), (1, 1, 2, false), (1, 1, 3, false),
                                                                    (1, 2, 1, false), (1, 2, 2, true), (1, 2, 3, false),
                                                                    (2, 1, 1, false), (2, 1, 2, false), (2, 1, 3, false),
                                                                    (3, 1, 1, false), (3, 1, 2, false), (3, 1, 3, false);

INSERT INTO movies (title, duration) VALUES
                                         ('Inception', 148),
                                         ('The Dark Knight', 152),
                                         ('Interstellar', 169);

INSERT INTO movie_sessions (movie_id, hall_id, date_time) VALUES
                                                              (1, 1, '2026-06-10 14:00:00'),
                                                              (1, 2, '2026-06-10 18:00:00'),
                                                              (2, 1, '2026-06-11 15:00:00'),
                                                              (3, 3, '2026-06-12 20:00:00');
