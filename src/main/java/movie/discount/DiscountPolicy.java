package movie.discount;

import movie.Screening;

public interface DiscountPolicy {
    int calculateDiscount(int originalPrice, Screening screening);
}