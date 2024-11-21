public class Course
{
    private int courseID;
    private String courseName;
    private int totalStudents;
    Course(){}
    Course(int ID, String name)
    {
        this.courseID = ID;
        this.courseName = name;
    }
    public int getCourseID(){return courseID;}
    public String getCourseName() {return courseName;}
        
};