package movie;

import movie.seat.Seat;
import movie.seat.SeatType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SeatTest {

    @Test
    @DisplayName("좌석을 정상적으로 생성할 수 있다")
    void createSeat() {
        Seat seat = new Seat("A1", SeatType.A);

        assertThat(seat.getSeatNumber()).isEqualTo("A1");
        assertThat(seat.getSeatType()).isEqualTo(SeatType.A);
        assertThat(seat.getPrice()).isEqualTo(15000);
        assertThat(seat.isReserved()).isFalse();
    }

    @Test
    @DisplayName("좌석 번호가 null이면 예외가 발생한다")
    void createSeatWithNullSeatNumber() {
        assertThatThrownBy(() -> new Seat(null, SeatType.A))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("좌석 번호는 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("좌석 번호가 비어있으면 예외가 발생한다")
    void createSeatWithEmptySeatNumber() {
        assertThatThrownBy(() -> new Seat("", SeatType.A))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("좌석 번호는 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("좌석 타입이 null이면 예외가 발생한다")
    void createSeatWithNullSeatType() {
        assertThatThrownBy(() -> new Seat("A1", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("좌석 타입은 필수입니다.");
    }

    @Test
    @DisplayName("좌석을 예약할 수 있다")
    void reserveSeat() {
        Seat seat = new Seat("A1", SeatType.A);

        seat.reserve();

        assertThat(seat.isReserved()).isTrue();
    }

    @Test
    @DisplayName("이미 예약된 좌석을 다시 예약하면 예외가 발생한다")
    void reserveAlreadyReservedSeat() {
        Seat seat = new Seat("A1", SeatType.A);
        seat.reserve();

        assertThatThrownBy(seat::reserve)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 예약된 좌석입니다: A1");
    }

    @Test
    @DisplayName("예약된 좌석을 취소할 수 있다")
    void cancelReservation() {
        Seat seat = new Seat("A1", SeatType.A);
        seat.reserve();

        seat.cancel();

        assertThat(seat.isReserved()).isFalse();
    }

    @Test
    @DisplayName("예약되지 않은 좌석을 취소하면 예외가 발생한다")
    void cancelNotReservedSeat() {
        Seat seat = new Seat("A1", SeatType.A);

        assertThatThrownBy(seat::cancel)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("예약되지 않은 좌석입니다: A1");
    }

    @Test
    @DisplayName("좌석 타입별 가격을 확인할 수 있다")
    void getSeatTypePrice() {
        Seat seatS = new Seat("S1", SeatType.S);
        Seat seatA = new Seat("A1", SeatType.A);
        Seat seatB = new Seat("B1", SeatType.B);

        assertThat(seatS.getPrice()).isEqualTo(18000);
        assertThat(seatA.getPrice()).isEqualTo(15000);
        assertThat(seatB.getPrice()).isEqualTo(12000);
    }
}