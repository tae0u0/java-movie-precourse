package movie.reservation;

import movie.discount.PaymentMethod;
import movie.Screening;
import movie.discount.DiscountCalculator;
import movie.seat.Seat;

import java.util.List;

public class PriceCalculator {
    private final DiscountCalculator discountCalculator;

    public PriceCalculator(DiscountCalculator discountCalculator) {
        this.discountCalculator = discountCalculator;
    }

    public int calculateFinalPrice(List<Seat> seats, Screening screening, int points, PaymentMethod paymentMethod) {
        int originalPrice = calculateOriginalPrice(seats);
        int basicDiscount = discountCalculator.calculateTotalDiscount(originalPrice, screening);
        int priceAfterBasicDiscount = originalPrice - basicDiscount;
        int priceAfterPoints = Math.max(0, priceAfterBasicDiscount - points);
        int paymentDiscount = paymentMethod.calculateDiscount(priceAfterPoints);

        return priceAfterPoints - paymentDiscount;
    }

    private int calculateOriginalPrice(List<Seat> seats) {
        return seats.stream()
                .mapToInt(Seat::getPrice)
                .sum();
    }
}