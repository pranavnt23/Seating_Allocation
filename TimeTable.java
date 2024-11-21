import java.util.*;

public class TimeTable 
{
    private LinkedHashMap<String, Course> schedule;

    TimeTable() 
    {
        this.schedule = new LinkedHashMap<>();
    }

    TimeTable(LinkedHashMap<String, Course> table) 
    {
        this.schedule = table;
    }

    void displayTimeTable() 
    {
        System.out.println("Date\tCourse ID\tCourse Name");
        for (Map.Entry<String, Course> entry : schedule.entrySet()) 
        {
            String date = entry.getKey();
            Course course = entry.getValue();
            System.out.printf("%s\t%d\t\t%s\n", date, course.getCourseID(), course.getCourseName());
        }
    }
}