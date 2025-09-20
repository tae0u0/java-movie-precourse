package theater;

public enum SeatGrade {
    S(18_000),
    A(15_000),
    B(12_000);

    private int price;

    SeatGrade(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
