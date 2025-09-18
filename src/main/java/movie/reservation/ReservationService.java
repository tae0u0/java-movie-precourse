package movie.reservation;

import movie.discount.PaymentMethod;
import movie.Screening;
import movie.seat.Seat;

import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private final List<Reservation> reservations;
    private final PriceCalculator priceCalculator;

    public ReservationService(PriceCalculator priceCalculator) {
        this.reservations = new ArrayList<>();
        this.priceCalculator = priceCalculator;
    }

    public Reservation makeReservation(Screening screening, List<Seat> seats, int points, PaymentMethod paymentMethod) {
        validateSeatsAvailability(seats);

        int finalPrice = priceCalculator.calculateFinalPrice(seats, screening, points, paymentMethod);
        Reservation reservation = new Reservation(screening, seats, finalPrice, paymentMethod);

        reservations.add(reservation);
        return reservation;
    }

    public List<Reservation> makeMultipleReservations(List<Screening> screenings, List<List<Seat>> allSeats, int points, PaymentMethod paymentMethod) {
        if (screenings.size() != allSeats.size()) {
            throw new IllegalArgumentException("상영 수와 좌석 목록 수가 일치해야 합니다.");
        }

        validateNoTimeOverlap(screenings);

        List<Reservation> newReservations = new ArrayList<>();
        for (int i = 0; i < screenings.size(); i++) {
            Reservation reservation = makeReservation(screenings.get(i), allSeats.get(i), 0, paymentMethod);
            newReservations.add(reservation);
        }

        return newReservations;
    }

    private void validateSeatsAvailability(List<Seat> seats) {
        for (Seat seat : seats) {
            if (seat.isReserved()) {
                throw new IllegalArgumentException("이미 예약된 좌석입니다: " + seat.getSeatNumber());
            }
        }
    }

    private void validateNoTimeOverlap(List<Screening> screenings) {
        for (int i = 0; i < screenings.size(); i++) {
            for (int j = i + 1; j < screenings.size(); j++) {
                if (screenings.get(i).isOverlappingWith(screenings.get(j))) {
                    throw new IllegalArgumentException("시간이 겹치는 상영은 함께 예매할 수 없습니다.");
                }
            }
        }
    }

    public void cancelReservation(Reservation reservation) {
        if (reservations.remove(reservation)) {
            reservation.cancel();
        }
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }
}