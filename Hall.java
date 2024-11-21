public class Hall
{
    private int hallNo;
    private String block;
    private int hallSize;
    private Seat[][] seats;

    Hall(int hallNo, String block, int size)
    {
        this.hallNo = hallNo;
        this.block = block;
        this.hallSize = size;
    }
    
    public void displaySeats()
    {
       for(int i=0; i<seats.length; i++)
       {
        for(int j=0; j<seats[0].length; j++)
        {
            seats[i][j].displaySeatDetails();
        }
       } 
    }
};