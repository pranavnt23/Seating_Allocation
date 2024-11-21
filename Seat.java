public class Seat
{
    private int seatNo;
    private int rollNo;
    
    Seat(int seatNo, int rollNo)
    {
        this.seatNo = seatNo;
        this.rollNo = rollNo;
    }
    public void displaySeatDetails()
    {
        System.out.println(seatNo + " "+ rollNo);
    }
};