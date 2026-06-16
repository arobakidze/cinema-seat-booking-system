package com.cinema.service.impl;

import com.cinema.MyBatisUtil;
import com.cinema.domain.CinemaHall;
import com.cinema.domain.Reservation;
import com.cinema.domain.Seat;
import com.cinema.persistence.mybatis.dao.HallDao;
import com.cinema.persistence.mybatis.dao.SeatDao;
import com.cinema.service.ReservationService;
import com.cinema.service.SeatAllocationService;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SeatAllocationServiceImpl implements SeatAllocationService {

    private final ReservationService reservationService;

    public SeatAllocationServiceImpl(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public List<Seat> listAvailableSeats(Long sessionId) {
        validateSessionId(sessionId);

        try (SqlSession session = MyBatisUtil.getSession()) {
            SeatDao seatDao = session.getMapper(SeatDao.class);
            return seatDao.findAvailableBySessionId(sessionId);
        }
    }

    @Override
    public List<Seat> suggestBestSeats(Long sessionId, int groupSize) {
        validateSessionId(sessionId);
        validateGroupSize(groupSize);

        try (SqlSession session = MyBatisUtil.getSession()) {
            SeatDao seatDao = session.getMapper(SeatDao.class);
            HallDao hallDao = session.getMapper(HallDao.class);

            List<Seat> availableSeats = seatDao.findAvailableBySessionId(sessionId);
            if (availableSeats.size() < groupSize) {
                return List.of();
            }

            Long hallId = availableSeats.get(0).getHallId();
            CinemaHall hall = hallDao.findById(hallId);
            if (hall == null) {
                throw new IllegalStateException("Hall not found for session " + sessionId);
            }

            return chooseBestSeatGroup(availableSeats, hall, groupSize);
        }
    }

    @Override
    public Reservation bookBestAvailableSeats(Long sessionId, String customerName, int groupSize) {
        List<Seat> bestSeats = suggestBestSeats(sessionId, groupSize);
        if (bestSeats.isEmpty()) {
            throw new IllegalStateException("No consecutive seats available for group size " + groupSize);
        }

        List<Long> seatIds = bestSeats.stream()
                .map(Seat::getId)
                .collect(Collectors.toList());

        return reservationService.bookSeats(sessionId, customerName, seatIds);
    }

    private List<Seat> chooseBestSeatGroup(List<Seat> availableSeats, CinemaHall hall, int groupSize) {
        Map<Integer, List<Seat>> seatsByRow = availableSeats.stream()
                .collect(Collectors.groupingBy(Seat::getRowNum));

        SeatGroupCandidate bestCandidate = null;

        List<Integer> sortedRows = seatsByRow.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        for (Integer rowNum : sortedRows) {
            List<Seat> rowSeats = seatsByRow.get(rowNum).stream()
                    .sorted(Comparator.comparingInt(Seat::getSeatNumber))
                    .collect(Collectors.toList());

            for (int start = 0; start <= rowSeats.size() - groupSize; start++) {
                List<Seat> candidateSeats = new ArrayList<>(rowSeats.subList(start, start + groupSize));
                if (!isConsecutive(candidateSeats)) {
                    continue;
                }

                double score = calculateScore(candidateSeats, hall);
                double rowDistance = Math.abs(candidateSeats.get(0).getRowNum() - calculateRowCentre(hall));
                double seatDistance = Math.abs(calculateSeatGroupCentre(candidateSeats) - calculateSeatCentre(hall));

                SeatGroupCandidate candidate = new SeatGroupCandidate(
                        candidateSeats,
                        score,
                        rowDistance,
                        seatDistance
                );

                if (isBetter(candidate, bestCandidate)) {
                    bestCandidate = candidate;
                }
            }
        }

        return bestCandidate == null ? List.of() : bestCandidate.seats;
    }

    private boolean isConsecutive(List<Seat> seats) {
        for (int i = 1; i < seats.size(); i++) {
            if (seats.get(i).getSeatNumber() != seats.get(i - 1).getSeatNumber() + 1) {
                return false;
            }
        }
        return true;
    }

    private double calculateScore(List<Seat> seats, CinemaHall hall) {
        double rowDistance = Math.abs(seats.get(0).getRowNum() - calculateRowCentre(hall));
        double seatDistance = Math.abs(calculateSeatGroupCentre(seats) - calculateSeatCentre(hall));
        return (rowDistance * 10.0) + seatDistance;
    }

    private double calculateRowCentre(CinemaHall hall) {
        return (hall.getTotalRows() + 1) / 2.0;
    }

    private double calculateSeatCentre(CinemaHall hall) {
        return (hall.getSeatsPerRow() + 1) / 2.0;
    }

    private double calculateSeatGroupCentre(List<Seat> seats) {
        return seats.stream()
                .mapToInt(Seat::getSeatNumber)
                .average()
                .orElseThrow(() -> new IllegalStateException("Seat group cannot be empty."));
    }

    private boolean isBetter(SeatGroupCandidate candidate, SeatGroupCandidate currentBest) {
        if (currentBest == null) {
            return true;
        }
        if (candidate.score < currentBest.score) {
            return true;
        }
        if (Double.compare(candidate.score, currentBest.score) == 0
                && candidate.rowDistance < currentBest.rowDistance) {
            return true;
        }
        if (Double.compare(candidate.score, currentBest.score) == 0
                && Double.compare(candidate.rowDistance, currentBest.rowDistance) == 0
                && candidate.seatDistance < currentBest.seatDistance) {
            return true;
        }
        return Double.compare(candidate.score, currentBest.score) == 0
                && Double.compare(candidate.rowDistance, currentBest.rowDistance) == 0
                && Double.compare(candidate.seatDistance, currentBest.seatDistance) == 0
                && candidate.seats.get(0).getSeatNumber() < currentBest.seats.get(0).getSeatNumber();
    }

    private void validateSessionId(Long sessionId) {
        if (sessionId == null || sessionId <= 0) {
            throw new IllegalArgumentException("Session ID must be positive.");
        }
    }

    private void validateGroupSize(int groupSize) {
        if (groupSize <= 0) {
            throw new IllegalArgumentException("Group size must be positive.");
        }
    }

    private static class SeatGroupCandidate {
        private final List<Seat> seats;
        private final double score;
        private final double rowDistance;
        private final double seatDistance;

        private SeatGroupCandidate(List<Seat> seats, double score, double rowDistance, double seatDistance) {
            this.seats = seats;
            this.score = score;
            this.rowDistance = rowDistance;
            this.seatDistance = seatDistance;
        }
    }
}
