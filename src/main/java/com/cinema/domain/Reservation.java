package com.cinema.domain;

import java.util.List;

public class Reservation {

    private Long id;
    private Long sessionId;
    private String customerName;
    private List<Seat> seats;

    public Reservation() {
    }

    public Reservation(Long id, Long sessionId, String customerName) {
        this.id = id;
        this.sessionId = sessionId;
        this.customerName = customerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
