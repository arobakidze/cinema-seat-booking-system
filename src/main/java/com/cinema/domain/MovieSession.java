package com.cinema.domain;

import java.time.LocalDateTime;

public class MovieSession {

    private Long id;
    private Long movieId;
    private Long hallId;
    private LocalDateTime startTime;

    @Override
    public String toString() {
        return  getId() + ". "
                + movieId
                + " (" + hallId + " min)"
                + " Genres: "
                + startTime;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getHallId() {
        return hallId;
    }

    public void setHallId(Long hallId) {
        this.hallId = hallId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
