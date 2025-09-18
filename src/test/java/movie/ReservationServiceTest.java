package movie;

import movie.discount.*;
import movie.reservation.PriceCalculator;
import movie.reservation.Reservation;
import movie.reservation.ReservationService;
import movie.seat.Seat;
import movie.seat.SeatType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ReservationServiceTest {
    private ReservationService reservationService;
    private Movie movie;
    private Screening screening;
    private List<Seat> seats;

    @BeforeEach
    void setUp() {
        List<DiscountPolicy> policies = Arrays.asList(
                new MovieDayDiscountPolicy(),
                new TimeDiscountPolicy()
        );
        DiscountCalculator discountCalculator = new DiscountCalculator(policies);
        PriceCalculator priceCalculator = new PriceCalculator(discountCalculator);
        reservationService = new ReservationService(priceCalculator);

        movie = new Movie("아바타", 180, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        screening = new Screening(movie, LocalDateTime.of(2024, 1, 10, 10, 30), 1);
        seats = Arrays.asList(
                new Seat("A1", SeatType.A),
                new Seat("A2", SeatType.A)
        );
    }

    @Test
    @DisplayName("영화를 예매할 수 있다")
    void makeReservation() {
        Reservation reservation = reservationService.makeReservation(screening, seats, 0, PaymentMethod.CREDIT_CARD);

        assertThat(reservation.getScreening()).isEqualTo(screening);
        assertThat(reservation.getSeats()).hasSize(2);
        assertThat(reservation.getPaymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
        assertThat(seats.get(0).isReserved()).isTrue();
        assertThat(seats.get(1).isReserved()).isTrue();
    }

    @Test
    @DisplayName("이미 예약된 좌석으로 예매하면 예외가 발생한다")
    void makeReservationWithReservedSeat() {
        seats.get(0).reserve();

        assertThatThrownBy(() -> reservationService.makeReservation(screening, seats, 0, PaymentMethod.CREDIT_CARD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약된 좌석입니다: A1");
    }

    @Test
    @DisplayName("예매를 취소할 수 있다")
    void cancelReservation() {
        Reservation reservation = reservationService.makeReservation(screening, seats, 0, PaymentMethod.CREDIT_CARD);

        reservationService.cancelReservation(reservation);

        assertThat(seats.get(0).isReserved()).isFalse();
        assertThat(seats.get(1).isReserved()).isFalse();
        assertThat(reservationService.getAllReservations()).doesNotContain(reservation);
    }

    @Test
    @DisplayName("시간이 겹치지 않는 여러 영화를 예매할 수 있다")
    void makeMultipleReservations() {
        Screening screening2 = new Screening(movie, LocalDateTime.of(2024, 1, 10, 18, 0), 2);
        List<Seat> seats2 = Arrays.asList(new Seat("B1", SeatType.B));

        List<Reservation> reservations = reservationService.makeMultipleReservations(
                Arrays.asList(screening, screening2),
                Arrays.asList(seats, seats2),
                0,
                PaymentMethod.CASH
        );

        assertThat(reservations).hasSize(2);
        assertThat(reservations.get(0).getScreening()).isEqualTo(screening);
        assertThat(reservations.get(1).getScreening()).isEqualTo(screening2);
    }

    @Test
    @DisplayName("시간이 겹치는 여러 영화를 예매하면 예외가 발생한다")
    void makeMultipleReservationsWithOverlap() {
        Screening overlappingScreening = new Screening(movie, LocalDateTime.of(2024, 1, 10, 12, 0), 1);
        List<Seat> seats2 = Arrays.asList(new Seat("B1", SeatType.B));

        assertThatThrownBy(() -> reservationService.makeMultipleReservations(
                Arrays.asList(screening, overlappingScreening),
                Arrays.asList(seats, seats2),
                0,
                PaymentMethod.CASH
        ))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("시간이 겹치는 상영은 함께 예매할 수 없습니다.");
    }
}