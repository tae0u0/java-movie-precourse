package movie.discount;

import movie.Screening;

public class TimeDiscountPolicy implements DiscountPolicy {
    private static final int DISCOUNT_AMOUNT = 2000;

    @Override
    public int calculateDiscount(int originalPrice, Screening screening) {
        if (screening.isEarlyOrLateShow()) {
            return DISCOUNT_AMOUNT;
        }
        return 0;
    }
}