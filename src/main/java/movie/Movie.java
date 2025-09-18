package movie;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Movie {
    private final String title;
    private final int durationMinutes;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Movie(String title, int durationMinutes, LocalDate startDate, LocalDate endDate) {
        validateTitle(title);
        validateDuration(durationMinutes);
        validatePeriod(startDate, endDate);

        this.title = title;
        this.durationMinutes = durationMinutes;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("영화 제목은 비어있을 수 없습니다.");
        }
    }

    private void validateDuration(int durationMinutes) {
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("상영 시간은 0보다 커야 합니다.");
        }
    }

    private void validatePeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("상영 시작일과 종료일은 필수입니다.");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("상영 시작일은 종료일보다 이전이어야 합니다.");
        }
    }

    public boolean isScreenableOn(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public LocalDateTime calculateEndTime(LocalDateTime startTime) {
        return startTime.plusMinutes(durationMinutes);
    }

    public String getTitle() {
        return title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return durationMinutes == movie.durationMinutes &&
                Objects.equals(title, movie.title) &&
                Objects.equals(startDate, movie.startDate) &&
                Objects.equals(endDate, movie.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, durationMinutes, startDate, endDate);
    }

    @Override
    public String toString() {
        return String.format("Movie{title='%s', duration=%d분, period=%s~%s}",
                title, durationMinutes, startDate, endDate);
    }
}