import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class Teacher {
    private String examHallDetails;  // Details of the exam hall
    private int hallNo;              // Hall number
    private int numberOfStudents;    // Total number of students
    private String[] students;       // Array to store student names
    private String[] absentees;      // Array to store absentees
    private String username;         // Teacher's username
    private String password;         // Teacher's password
    private Connection connection;   // Database connection

    // Constructor
    public Teacher(String examHallDetails, int hallNo, int numberOfStudents, String username, String password) {
        this.examHallDetails = examHallDetails;
        this.hallNo = hallNo;
        this.numberOfStudents = numberOfStudents;
        this.username = username;
        this.password = password;
        this.students = new String[numberOfStudents];    // Initialize the array to store student names
        this.absentees = new String[numberOfStudents];  // Initialize the array to store absentees
        this.connection = connectToDatabase();          // Connect to the database
        createTablesIfNotExist();                        // Create tables if they don't exist
    }

    // Method to connect to the database
    private Connection connectToDatabase() {
        String url = "jdbc:mariadb://localhost:3307/teacher";
        String userName = "root";
        String dbPassword = "root123";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, userName, dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to the database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        return conn;
    }

    // Method to create tables if they don't exist
    private void createTablesIfNotExist() {
        try {
            Statement statement = connection.createStatement();

            // Create attendance table if it doesn't exist
            String createAttendanceTableSQL = "CREATE TABLE IF NOT EXISTS attendance (" +
                    "sroll_no INT AUTO_INCREMENT PRIMARY KEY," +
                    "sname VARCHAR(255) NOT NULL," +
                    "is_present INT)";
            statement.executeUpdate(createAttendanceTableSQL);

            // Create teacher_details table if it doesn't exist
            String createTeacherDetailsTableSQL = "CREATE TABLE IF NOT EXISTS teacher_details (" +
                    "hall_no INT PRIMARY KEY," +
                    "exam_hall_details VARCHAR(255) NOT NULL," +
                    "no_of_students INT NOT NULL)";
            statement.executeUpdate(createTeacherDetailsTableSQL);

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error creating tables: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    // Method for teacher login
    public boolean teacherLogin(String enteredUsername, String enteredPassword) {
        return enteredUsername.equals(username) && enteredPassword.equals(password);
    }

    // Method to mark attendance for students
    public void markAttendance(String studentName, boolean isPresent) {
        try {
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO attendance (sname, is_present) VALUES ('" + studentName + "', " + (isPresent ? 1 : 0) + ")";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int studentId = rs.getInt(1);
                for (int i = 0; i < numberOfStudents; i++) {
                    if (students[i] == null) {
                        students[i] = studentName;       // If student name is not already in the array, add it
                        if (!isPresent) {
                            absentees[i] = studentName;  // Add student to absentees if not present
                        }
                        break;
                    }
                    if (students[i].equals(studentName)) {
                        if (!isPresent) {
                            absentees[i] = studentName;  // Add student to absentees if not present
                        }
                        break;
                    }
                }
            }
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error marking attendance: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to get exam hall details
    public String getExamHallDetails() {
        return examHallDetails;
    }

    // Method to get hall number
    public int getHallNo() {
        return hallNo;
    }

    // Method to get the number of students
    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    // Method to get the list of absentees
    public String getAbsentees() {
        StringBuilder absenteesList = new StringBuilder();
        for (int i = 0; i < numberOfStudents; i++) {
            if (absentees[i] != null) {
                absenteesList.append(absentees[i]).append("<br/>");
            }
        }
        return absenteesList.toString();
    }

    public static void main(String[] args) {
        String examHallDetails = "Exam Hall 1";
        int hallNo = 101;
        int numberOfStudents = 5;
        String username = "teacher1";
        String password = "password";

        // Creating a teacher object
        Teacher teacher = new Teacher(examHallDetails, hallNo, numberOfStudents, username, password);

        // Teacher login
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JPanel loginPanel = new JPanel();
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(Box.createHorizontalStrut(15)); // a spacer
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, loginPanel, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String enteredUsername = usernameField.getText();
            String enteredPassword = new String(passwordField.getPassword());

            if (teacher.teacherLogin(enteredUsername, enteredPassword)) {
                // Marking attendance
                teacher.markAttendance("John", true);
                teacher.markAttendance("Alice", false);
                teacher.markAttendance("Bob", true);

                // Displaying exam hall details
                StringBuilder message = new StringBuilder();
                message.append("Exam Hall Details: ").append(teacher.getExamHallDetails()).append("\n");
                message.append("Hall No: ").append(teacher.getHallNo()).append("\n");
                message.append("Number of Students: ").append(teacher.getNumberOfStudents()).append("\n");

                // Displaying absentees
                message.append("Absentees:\n").append(teacher.getAbsentees()).append("\n");

                JOptionPane.showMessageDialog(null, message.toString(), "Teacher Dashboard", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.exit(0);
        }
    }
}
