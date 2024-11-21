import java.sql.Time;
import java.util.*;

public class Main 
{
    public static void main(String args[]) 
    {
        //subjects for MSC SS-II
        Course c1 = new Course(41, "RMT");
        Course c2 = new Course(42, "Microprocessors");
        Course c3 = new Course(43, "SE");
        Course c4 = new Course(44, "DBMS");
        Course c5 = new Course(45, "Java");

        LinkedHashMap<String, Course> schedule1 = new LinkedHashMap<>();
        schedule1.put("27-05-2024", c1);
        schedule1.put("28-05-2024", c2);
        schedule1.put("29-05-2024", c3);
        schedule1.put("30-05-2024", c4);
        schedule1.put("31-05-2024", c5);

        //TimeTable SS2TT = new TimeTable(schedule1);

        Course d1 = new Course(61, "STQA");
        Course d2 = new Course(62, "MC");
        Course d3 = new Course(63, "CC");
        Course d4 = new Course(64, "SA");
        Course d5 = new Course(65, "DM");

        LinkedHashMap<String, Course> schedule2 = new LinkedHashMap<>();
        schedule2.put("09-05-2024", d1);
        schedule2.put("10-05-2024", d2);
        schedule2.put("11-05-2024", d3);
        schedule2.put("12-05-2024", d4);
        schedule2.put("13-05-2024", d5);



        TimeTable SS2 = new TimeTable(schedule1);
        TimeTable SS3 = new TimeTable(schedule2);

        HashMap<String, TimeTable> schedule = new HashMap<>();
        schedule.put("MSC SS - II", SS2);
        schedule.put("MSC SS - III", SS3);

        COE trail = new COE(schedule);
        trail.displayTodaysTimeTables();
    }
};

