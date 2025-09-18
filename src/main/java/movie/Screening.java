package movie;

import java.time.LocalDateTime;
import java.util.Objects;

public class Screening {
    private final Movie movie;
    private final LocalDateTime startTime;
    private final int theaterNumber;

    public Screening(Movie movie, LocalDateTime startTime, int theaterNumber) {
        validateMovie(movie);
        validateStartTime(startTime);
        validateTheaterNumber(theaterNumber);
        validateScreeningPeriod(movie, startTime);

        this.movie = movie;
        this.startTime = startTime;
        this.theaterNumber = theaterNumber;
    }

    private void validateMovie(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("영화는 필수입니다.");
        }
    }

    private void validateStartTime(LocalDateTime startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("상영 시작 시간은 필수입니다.");
        }
    }

    private void validateTheaterNumber(int theaterNumber) {
        if (theaterNumber <= 0) {
            throw new IllegalArgumentException("상영관 번호는 1 이상이어야 합니다.");
        }
    }

    private void validateScreeningPeriod(Movie movie, LocalDateTime startTime) {
        if (!movie.isScreenableOn(startTime.toLocalDate())) {
            throw new IllegalArgumentException("영화 상영 가능 기간이 아닙니다.");
        }
    }

    public LocalDateTime getEndTime() {
        return movie.calculateEndTime(startTime);
    }

    public boolean isOverlappingWith(Screening other) {
        if (this.theaterNumber != other.theaterNumber) {
            return false;
        }

        LocalDateTime thisEnd = this.getEndTime();
        LocalDateTime otherEnd = other.getEndTime();

        return !(thisEnd.isBefore(other.startTime) || this.startTime.isAfter(otherEnd));
    }

    public boolean isMovieDay() {
        int day = startTime.getDayOfMonth();
        return day == 10 || day == 20 || day == 30;
    }

    public boolean isEarlyOrLateShow() {
        int hour = startTime.getHour();
        return hour < 11 || hour >= 20;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getTheaterNumber() {
        return theaterNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Screening screening = (Screening) o;
        return theaterNumber == screening.theaterNumber &&
                Objects.equals(movie, screening.movie) &&
                Objects.equals(startTime, screening.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie, startTime, theaterNumber);
    }

    @Override
    public String toString() {
        return String.format("Screening{movie='%s', startTime=%s, theater=%d}",
                movie.getTitle(), startTime, theaterNumber);
    }
}