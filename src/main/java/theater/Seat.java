package theater;

public class Seat {
    private final SeatGrade seatGrade;
    private final String seatNumber;

    public Seat(SeatGrade seatGrade, String seatNumber) {
        this.seatGrade = seatGrade;
        this.seatNumber = seatNumber;
    }

    public int getSeatPrice(){
        return seatGrade.getPrice();
    }

}
