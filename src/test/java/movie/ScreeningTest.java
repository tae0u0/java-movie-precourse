package movie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class ScreeningTest {
    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie("아바타", 180, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
    }

    @Test
    @DisplayName("상영을 정상적으로 생성할 수 있다")
    void createScreening() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 10, 14, 30);

        Screening screening = new Screening(movie, startTime, 1);

        assertThat(screening.getMovie()).isEqualTo(movie);
        assertThat(screening.getStartTime()).isEqualTo(startTime);
        assertThat(screening.getTheaterNumber()).isEqualTo(1);
    }

    @Test
    @DisplayName("영화가 null이면 예외가 발생한다")
    void createScreeningWithNullMovie() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 10, 14, 30);

        assertThatThrownBy(() -> new Screening(null, startTime, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("영화는 필수입니다.");
    }

    @Test
    @DisplayName("상영 시작 시간이 null이면 예외가 발생한다")
    void createScreeningWithNullStartTime() {
        assertThatThrownBy(() -> new Screening(movie, null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상영 시작 시간은 필수입니다.");
    }

    @Test
    @DisplayName("상영관 번호가 0 이하이면 예외가 발생한다")
    void createScreeningWithInvalidTheaterNumber() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 10, 14, 30);

        assertThatThrownBy(() -> new Screening(movie, startTime, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상영관 번호는 1 이상이어야 합니다.");
    }

    @Test
    @DisplayName("영화 상영 가능 기간이 아니면 예외가 발생한다")
    void createScreeningOutsideMoviePeriod() {
        LocalDateTime startTime = LocalDateTime.of(2023, 12, 31, 14, 30);

        assertThatThrownBy(() -> new Screening(movie, startTime, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("영화 상영 가능 기간이 아닙니다.");
    }

    @Test
    @DisplayName("상영 종료 시간을 계산할 수 있다")
    void getEndTime() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 10, 14, 30);
        Screening screening = new Screening(movie, startTime, 1);

        LocalDateTime endTime = screening.getEndTime();

        assertThat(endTime).isEqualTo(LocalDateTime.of(2024, 1, 10, 17, 30));
    }

    @Test
    @DisplayName("같은 상영관에서 시간이 겹치는 상영을 확인할 수 있다")
    void isOverlappingWithSameTheater() {
        Screening screening1 = new Screening(movie, LocalDateTime.of(2024, 1, 10, 14, 0), 1);
        Screening screening2 = new Screening(movie, LocalDateTime.of(2024, 1, 10, 16, 0), 1);

        assertThat(screening1.isOverlappingWith(screening2)).isTrue();
    }

    @Test
    @DisplayName("다른 상영관에서는 시간이 겹쳐도 겹치지 않는다고 판단한다")
    void isNotOverlappingWithDifferentTheater() {
        Screening screening1 = new Screening(movie, LocalDateTime.of(2024, 1, 10, 14, 0), 1);
        Screening screening2 = new Screening(movie, LocalDateTime.of(2024, 1, 10, 16, 0), 2);

        assertThat(screening1.isOverlappingWith(screening2)).isFalse();
    }

    @Test
    @DisplayName("무비데이인지 확인할 수 있다")
    void isMovieDay() {
        Screening movieDay10 = new Screening(movie, LocalDateTime.of(2024, 1, 10, 14, 0), 1);
        Screening movieDay20 = new Screening(movie, LocalDateTime.of(2024, 1, 20, 14, 0), 1);
        Screening movieDay30 = new Screening(movie, LocalDateTime.of(2024, 1, 30, 14, 0), 1);
        Screening normalDay = new Screening(movie, LocalDateTime.of(2024, 1, 15, 14, 0), 1);

        assertThat(movieDay10.isMovieDay()).isTrue();
        assertThat(movieDay20.isMovieDay()).isTrue();
        assertThat(movieDay30.isMovieDay()).isTrue();
        assertThat(normalDay.isMovieDay()).isFalse();
    }

    @Test
    @DisplayName("조조/심야 상영인지 확인할 수 있다")
    void isEarlyOrLateShow() {
        Screening earlyShow = new Screening(movie, LocalDateTime.of(2024, 1, 10, 10, 30), 1);
        Screening lateShow = new Screening(movie, LocalDateTime.of(2024, 1, 10, 20, 30), 1);
        Screening normalShow = new Screening(movie, LocalDateTime.of(2024, 1, 10, 14, 30), 1);

        assertThat(earlyShow.isEarlyOrLateShow()).isTrue();
        assertThat(lateShow.isEarlyOrLateShow()).isTrue();
        assertThat(normalShow.isEarlyOrLateShow()).isFalse();
    }
}