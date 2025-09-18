package movie.discount;

public enum PaymentMethod {
    CREDIT_CARD(0.05),
    CASH(0.02);

    private final double discountRate;

    PaymentMethod(double discountRate) {
        this.discountRate = discountRate;
    }

    public int calculateDiscount(int amount) {
        return (int) (amount * discountRate);
    }
}