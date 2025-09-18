package movie.seat;

public enum SeatType {
    S(18000),
    A(15000),
    B(12000);

    private final int price;

    SeatType(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}