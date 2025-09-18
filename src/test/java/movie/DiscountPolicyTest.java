package movie;

import movie.discount.MovieDayDiscountPolicy;
import movie.discount.TimeDiscountPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class DiscountPolicyTest {
    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie("아바타", 180, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
    }

    @Test
    @DisplayName("무비데이에 10% 할인이 적용된다")
    void movieDayDiscountPolicy() {
        MovieDayDiscountPolicy policy = new MovieDayDiscountPolicy();
        Screening movieDayScreening = new Screening(movie, LocalDateTime.of(2024, 1, 10, 14, 0), 1);

        int discount = policy.calculateDiscount(10000, movieDayScreening);

        assertThat(discount).isEqualTo(1000);
    }

    @Test
    @DisplayName("무비데이가 아닌 날에는 할인이 적용되지 않는다")
    void noMovieDayDiscount() {
        MovieDayDiscountPolicy policy = new MovieDayDiscountPolicy();
        Screening normalScreening = new Screening(movie, LocalDateTime.of(2024, 1, 15, 14, 0), 1);

        int discount = policy.calculateDiscount(10000, normalScreening);

        assertThat(discount).isEqualTo(0);
    }

    @Test
    @DisplayName("조조/심야 상영에 2000원 할인이 적용된다")
    void timeDiscountPolicy() {
        TimeDiscountPolicy policy = new TimeDiscountPolicy();
        Screening earlyScreening = new Screening(movie, LocalDateTime.of(2024, 1, 15, 10, 30), 1);
        Screening lateScreening = new Screening(movie, LocalDateTime.of(2024, 1, 15, 20, 30), 1);

        int earlyDiscount = policy.calculateDiscount(10000, earlyScreening);
        int lateDiscount = policy.calculateDiscount(10000, lateScreening);

        assertThat(earlyDiscount).isEqualTo(2000);
        assertThat(lateDiscount).isEqualTo(2000);
    }

    @Test
    @DisplayName("일반 시간대에는 시간 할인이 적용되지 않는다")
    void noTimeDiscount() {
        TimeDiscountPolicy policy = new TimeDiscountPolicy();
        Screening normalScreening = new Screening(movie, LocalDateTime.of(2024, 1, 15, 14, 0), 1);

        int discount = policy.calculateDiscount(10000, normalScreening);

        assertThat(discount).isEqualTo(0);
    }
}