import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;



public class COE {
    private Hall[] examHalls;
    private Course[] courses;
    private HashMap<String, TimeTable> schedule;
    private int totalHallsAvailable;
    private int totalInvigilators; 
    private int totalStudentsAppearing;
    private int totalQPs;

    COE(HashMap<String, TimeTable> tt) {
        this.schedule = tt;
    }

    COE(Hall[] halls, Course[] courses, int totalStudents, int totalInvigilators) {
        this.examHalls = halls;
        this.courses = courses;
        this.totalStudentsAppearing = totalStudents;
        this.totalInvigilators = totalInvigilators; 
        this.totalHallsAvailable = halls.length;
    }

    public void displayHall(int hallNo) {
        examHalls[hallNo].displaySeats();
    }

    public void displayTodaysTimeTables() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = today.format(formatter);
        
        if (schedule.containsKey(formattedDate)) {
            TimeTable todayTimeTable = schedule.get(formattedDate);
            todayTimeTable.displayTimeTable();
        } else {
            System.out.println("No timetable available for today.");
        }
    }
}
