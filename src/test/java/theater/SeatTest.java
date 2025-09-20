package theater;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SeatTest {

    @Test
    @DisplayName("가격을 불러올 수 있다.")
    void getSeatPrice(){
        Seat seat = new Seat(SeatGrade.S, "A1");
        int seatPrice = seat.getSeatPrice();

        assertThat(seatPrice).isEqualTo(18_000);
    }
}