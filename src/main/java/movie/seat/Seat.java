package movie.seat;

import java.util.Objects;

public class Seat {
    private final String seatNumber;
    private final SeatType seatType;
    private boolean isReserved;

    public Seat(String seatNumber, SeatType seatType) {
        validateSeatNumber(seatNumber);
        validateSeatType(seatType);

        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.isReserved = false;
    }

    private void validateSeatNumber(String seatNumber) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("좌석 번호는 비어있을 수 없습니다.");
        }
    }

    private void validateSeatType(SeatType seatType) {
        if (seatType == null) {
            throw new IllegalArgumentException("좌석 타입은 필수입니다.");
        }
    }

    public void reserve() {
        if (isReserved) {
            throw new IllegalStateException("이미 예약된 좌석입니다: " + seatNumber);
        }
        this.isReserved = true;
    }

    public void cancel() {
        if (!isReserved) {
            throw new IllegalStateException("예약되지 않은 좌석입니다: " + seatNumber);
        }
        this.isReserved = false;
    }

    public int getPrice() {
        return seatType.getPrice();
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public boolean isReserved() {
        return isReserved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return Objects.equals(seatNumber, seat.seatNumber) &&
                seatType == seat.seatType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatNumber, seatType);
    }

    @Override
    public String toString() {
        return String.format("Seat{number='%s', type=%s, price=%d, reserved=%s}",
                seatNumber, seatType, seatType.getPrice(), isReserved);
    }
}