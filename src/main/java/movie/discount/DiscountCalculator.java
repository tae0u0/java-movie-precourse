package movie.discount;

import movie.Screening;

import java.util.List;

public class DiscountCalculator {
    private final List<DiscountPolicy> discountPolicies;

    public DiscountCalculator(List<DiscountPolicy> discountPolicies) {
        this.discountPolicies = discountPolicies;
    }

    public int calculateTotalDiscount(int originalPrice, Screening screening) {
        int movieDayDiscount = 0;
        int timeDiscount = 0;

        for (DiscountPolicy policy : discountPolicies) {
            if (policy instanceof MovieDayDiscountPolicy) {
                movieDayDiscount = policy.calculateDiscount(originalPrice, screening);
            } else if (policy instanceof TimeDiscountPolicy) {
                timeDiscount = policy.calculateDiscount(originalPrice, screening);
            }
        }

        return movieDayDiscount + timeDiscount;
    }
}