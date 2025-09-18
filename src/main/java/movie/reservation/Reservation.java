package movie.reservation;

import movie.discount.PaymentMethod;
import movie.Screening;
import movie.seat.Seat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Reservation {
    private final Screening screening;
    private final List<Seat> seats;
    private final int finalPrice;
    private final PaymentMethod paymentMethod;
    private final LocalDateTime reservationTime;

    public Reservation(Screening screening, List<Seat> seats, int finalPrice, PaymentMethod paymentMethod) {
        validateScreening(screening);
        validateSeats(seats);
        validateFinalPrice(finalPrice);
        validatePaymentMethod(paymentMethod);

        this.screening = screening;
        this.seats = List.copyOf(seats);
        this.finalPrice = finalPrice;
        this.paymentMethod = paymentMethod;
        this.reservationTime = LocalDateTime.now();

        reserveSeats();
    }

    private void validateScreening(Screening screening) {
        if (screening == null) {
            throw new IllegalArgumentException("상영 정보는 필수입니다.");
        }
    }

    private void validateSeats(List<Seat> seats) {
        if (seats == null || seats.isEmpty()) {
            throw new IllegalArgumentException("좌석은 최소 1개 이상 선택해야 합니다.");
        }
        for (Seat seat : seats) {
            if (seat.isReserved()) {
                throw new IllegalArgumentException("이미 예약된 좌석이 포함되어 있습니다: " + seat.getSeatNumber());
            }
        }
    }

    private void validateFinalPrice(int finalPrice) {
        if (finalPrice < 0) {
            throw new IllegalArgumentException("최종 가격은 0 이상이어야 합니다.");
        }
    }

    private void validatePaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new IllegalArgumentException("결제 방법은 필수입니다.");
        }
    }

    private void reserveSeats() {
        for (Seat seat : seats) {
            seat.reserve();
        }
    }

    public void cancel() {
        for (Seat seat : seats) {
            seat.cancel();
        }
    }

    public boolean isOverlappingWith(Reservation other) {
        return this.screening.isOverlappingWith(other.screening);
    }

    public Screening getScreening() {
        return screening;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return finalPrice == that.finalPrice &&
                Objects.equals(screening, that.screening) &&
                Objects.equals(seats, that.seats) &&
                paymentMethod == that.paymentMethod &&
                Objects.equals(reservationTime, that.reservationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(screening, seats, finalPrice, paymentMethod, reservationTime);
    }

    @Override
    public String toString() {
        return String.format("Reservation{movie='%s', startTime=%s, seats=%d개, price=%d원, payment=%s}",
                screening.getMovie().getTitle(),
                screening.getStartTime(),
                seats.size(),
                finalPrice,
                paymentMethod);
    }
}