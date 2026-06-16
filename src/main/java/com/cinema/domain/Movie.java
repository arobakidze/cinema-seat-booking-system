package com.cinema.domain;

public class Movie {

    private Long id;
    private String title;
    private int durationMinutes;
    private String genres;

    @Override
    public String toString() {
        return getId() + ". "
                + title
                + " (" + durationMinutes + " min)"
                + " Genres: "
                + genres;
    }

    public Long getId() {
        return id;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genre) {
        this.genres = genre;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int duration) {
        this.durationMinutes = duration;
    }
}