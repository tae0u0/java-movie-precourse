package movie;

import movie.discount.*;
import movie.reservation.PriceCalculator;
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

class PriceCalculatorTest {
    private PriceCalculator priceCalculator;
    private Movie movie;
    private List<Seat> seats;

    @BeforeEach
    void setUp() {
        List<DiscountPolicy> policies = Arrays.asList(
                new MovieDayDiscountPolicy(),
                new TimeDiscountPolicy()
        );
        DiscountCalculator discountCalculator = new DiscountCalculator(policies);
        priceCalculator = new PriceCalculator(discountCalculator);

        movie = new Movie("아바타", 180, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        seats = Arrays.asList(
                new Seat("S1", SeatType.S),  // 18,000원
                new Seat("A1", SeatType.A)   // 15,000원
        );
    }

    @Test
    @DisplayName("무비데이 + 조조 할인 + 포인트 + 신용카드 결제 시 최종 가격을 계산할 수 있다")
    void calculateFinalPriceWithAllDiscounts() {
        Screening movieDayEarlyScreening = new Screening(movie, LocalDateTime.of(2024, 1, 10, 10, 30), 1);

        int finalPrice = priceCalculator.calculateFinalPrice(seats, movieDayEarlyScreening, 5000, PaymentMethod.CREDIT_CARD);

        // 원가: 33,000원
        // 무비데이 10% 할인: -3,300원 → 29,700원
        // 시간 할인: -2,000원 → 27,700원
        // 포인트 사용: -5,000원 → 22,700원
        // 신용카드 5% 할인: -1,135원 → 21,565원
        assertThat(finalPrice).isEqualTo(21565);
    }

    @Test
    @DisplayName("일반 상영 + 현금 결제 시 최종 가격을 계산할 수 있다")
    void calculateFinalPriceWithCashPayment() {
        Screening normalScreening = new Screening(movie, LocalDateTime.of(2024, 1, 15, 14, 0), 1);

        int finalPrice = priceCalculator.calculateFinalPrice(seats, normalScreening, 0, PaymentMethod.CASH);

        // 원가: 33,000원
        // 할인 없음
        // 현금 2% 할인: -660원 → 32,340원
        assertThat(finalPrice).isEqualTo(32340);
    }

    @Test
    @DisplayName("포인트가 결제 금액보다 큰 경우 0원이 된다")
    void calculateFinalPriceWithExcessivePoints() {
        Screening normalScreening = new Screening(movie, LocalDateTime.of(2024, 1, 15, 14, 0), 1);

        int finalPrice = priceCalculator.calculateFinalPrice(seats, normalScreening, 50000, PaymentMethod.CASH);

        assertThat(finalPrice).isEqualTo(0);
    }

    @Test
    @DisplayName("시간 할인만 적용되는 경우")
    void calculateFinalPriceWithTimeDiscountOnly() {
        Screening lateScreening = new Screening(movie, LocalDateTime.of(2024, 1, 15, 20, 30), 1);

        int finalPrice = priceCalculator.calculateFinalPrice(seats, lateScreening, 0, PaymentMethod.CREDIT_CARD);

        // 원가: 33,000원
        // 시간 할인: -2,000원 → 31,000원
        // 신용카드 5% 할인: -1,550원 → 29,450원
        assertThat(finalPrice).isEqualTo(29450);
    }
}