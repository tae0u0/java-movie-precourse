package movie.discount;

import movie.Screening;

public class MovieDayDiscountPolicy implements DiscountPolicy {
    private static final double DISCOUNT_RATE = 0.1;

    @Override
    public int calculateDiscount(int originalPrice, Screening screening) {
        if (screening.isMovieDay()) {
            return (int) (originalPrice * DISCOUNT_RATE);
        }
        return 0;
    }
}