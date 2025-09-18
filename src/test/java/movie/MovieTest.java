package movie;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class MovieTest {

    @Test
    @DisplayName("영화를 정상적으로 생성할 수 있다")
    void createMovie() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        Movie movie = new Movie("아바타", 180, startDate, endDate);

        assertThat(movie.getTitle()).isEqualTo("아바타");
        assertThat(movie.getDurationMinutes()).isEqualTo(180);
        assertThat(movie.getStartDate()).isEqualTo(startDate);
        assertThat(movie.getEndDate()).isEqualTo(endDate);
    }

    @ParameterizedTest
    @DisplayName("영화 제목이 비어있으면 예외가 발생한다")
    @ValueSource(strings = {"", "  ", "\t", "\n"})
    void createMovieWithEmptyTitle(String title) {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        assertThatThrownBy(() -> new Movie(title, 180, startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("영화 제목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("영화 제목이 null이면 예외가 발생한다")
    void createMovieWithNullTitle() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        assertThatThrownBy(() -> new Movie(null, 180, startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("영화 제목은 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @DisplayName("상영 시간이 0 이하이면 예외가 발생한다")
    @ValueSource(ints = {0, -1, -100})
    void createMovieWithInvalidDuration(int duration) {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        assertThatThrownBy(() -> new Movie("아바타", duration, startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상영 시간은 0보다 커야 합니다.");
    }

    @Test
    @DisplayName("시작일이 종료일보다 늦으면 예외가 발생한다")
    void createMovieWithInvalidPeriod() {
        LocalDate startDate = LocalDate.of(2024, 12, 31);
        LocalDate endDate = LocalDate.of(2024, 1, 1);

        assertThatThrownBy(() -> new Movie("아바타", 180, startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상영 시작일은 종료일보다 이전이어야 합니다.");
    }

    @Test
    @DisplayName("특정 날짜에 상영 가능한지 확인할 수 있다")
    void isScreenableOn() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        Movie movie = new Movie("아바타", 180, startDate, endDate);

        assertThat(movie.isScreenableOn(LocalDate.of(2024, 6, 15))).isTrue();
        assertThat(movie.isScreenableOn(LocalDate.of(2024, 1, 1))).isTrue();
        assertThat(movie.isScreenableOn(LocalDate.of(2024, 12, 31))).isTrue();
        assertThat(movie.isScreenableOn(LocalDate.of(2023, 12, 31))).isFalse();
        assertThat(movie.isScreenableOn(LocalDate.of(2025, 1, 1))).isFalse();
    }

    @Test
    @DisplayName("상영 종료 시간을 계산할 수 있다")
    void calculateEndTime() {
        Movie movie = new Movie("아바타", 180, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 10, 14, 30);

        LocalDateTime endTime = movie.calculateEndTime(startTime);

        assertThat(endTime).isEqualTo(LocalDateTime.of(2024, 1, 10, 17, 30));
    }
}