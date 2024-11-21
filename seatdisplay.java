import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class seatdisplay {

    // Database connection details
    private static final String URL = "jdbc:mariadb://localhost:3307/allocation";
    private static final String USER = "root";
    private static final String PASSWORD = "root123";

    private static int hallNumber = 101;
    private static int seatIndex = 1;
    private static int studentIndex = 0;
    private static int[] studentsPerCourse;
    private static JPanel seatPanel;
    private static JPanel mainPanel;
    private static JFrame frame;
    private static PreparedStatement preparedStatement;
    private static int[] baseNumbers;

    public static void main(String[] args) {
        // Create initial frame for user input
        JFrame inputFrame = new JFrame("Course and Student Input");
        inputFrame.setSize(400, 400);
        inputFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inputFrame.setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel coursesLabel = new JLabel("Number of Courses:");
        JTextField coursesField = new JTextField();
        inputPanel.add(coursesLabel);
        inputPanel.add(coursesField);

        JPanel coursesDetailPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        coursesDetailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addButton = new JButton("Add Courses");
        inputPanel.add(new JLabel());
        inputPanel.add(addButton);

        inputFrame.add(inputPanel, BorderLayout.NORTH);
        inputFrame.add(new JScrollPane(coursesDetailPanel), BorderLayout.CENTER);

        JButton allocateButton = new JButton("Allocate");
        inputFrame.add(allocateButton, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            coursesDetailPanel.removeAll();
            int numberOfCourses = Integer.parseInt(coursesField.getText());
            for (int i = 0; i < numberOfCourses; i++) {
                coursesDetailPanel.add(new JLabel("Course " + (i + 1) + " Students:"));
                coursesDetailPanel.add(new JTextField());
            }
            inputFrame.revalidate();
            inputFrame.repaint();
        });

        allocateButton.addActionListener(e -> {
            int numberOfCourses = Integer.parseInt(coursesField.getText());
            studentsPerCourse = new int[numberOfCourses];
            baseNumbers = new int[numberOfCourses];
            for (int i = 0; i < numberOfCourses; i++) {
                JTextField studentField = (JTextField) coursesDetailPanel.getComponent(i * 2 + 1);
                studentsPerCourse[i] = Integer.parseInt(studentField.getText());
                baseNumbers[i] = 71761000 + (i * 1000);  // Initial base number for each course
            }
            inputFrame.dispose();
            try {
                allocateSeats();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        inputFrame.setVisible(true);
    }

    private static void allocateSeats() throws SQLException {
        frame = new JFrame("Seat Matrix");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        prepareNewHall();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO sallocation (hallno, seatno, sroll_no) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            allocateNextBatchOfStudents();

            JButton nextButton = new JButton("Next");
            nextButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (seatIndex <= 60) {
                        return;
                    }
                    if (studentIndex < studentsPerCourse.length) {
                        updateBaseNumbers();
                        hallNumber++;
                        seatIndex = 1;
                        prepareNewHall();
                        try {
                            allocateNextBatchOfStudents();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }
            });

            mainPanel.add(nextButton, BorderLayout.SOUTH);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void prepareNewHall() {
        JPanel headingPanel = new JPanel(new GridLayout(5, 1));
        headingPanel.setBackground(new Color(173, 216, 230));
        JLabel instituteLabel = new JLabel("COVAI INSTITUTE OF TECHNOLOGY ", JLabel.CENTER);
        instituteLabel.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel hallLabel = new JLabel("Hall-" + hallNumber, JLabel.CENTER);
        hallLabel.setFont(new Font("Serif", Font.BOLD, 18));
        JLabel examLabel = new JLabel("Theory Examination - May 2024", JLabel.CENTER);
        examLabel.setFont(new Font("Serif", Font.BOLD, 18));
        JLabel seatArrangementLabel = new JLabel("Seat Arrangement", JLabel.CENTER);
        seatArrangementLabel.setFont(new Font("Serif", Font.BOLD, 16));

        headingPanel.add(instituteLabel);
        headingPanel.add(hallLabel);
        headingPanel.add(examLabel);
        headingPanel.add(seatArrangementLabel);

        seatPanel = new JPanel(new GridLayout(0, 10, 5, 5));
        seatPanel.setBackground(new Color(240, 255, 255));

        addInvigilatorInfo(headingPanel, hallNumber);

        mainPanel.removeAll();
        mainPanel.add(headingPanel, BorderLayout.NORTH);
        mainPanel.add(seatPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }


private static void allocateNextBatchOfStudents() throws SQLException {
    int hallCapacity = 60; // Assuming each hall has a capacity of 60 seats
    int remainingSeatsInHall = hallCapacity - seatIndex + 1;
    int remainingStudentsForCourse = studentsPerCourse[studentIndex] - (seatIndex - 1) % hallCapacity;

    // Check if there are enough remaining seats in the current hall for the remaining students
    if (remainingSeatsInHall < remainingStudentsForCourse) {
        // Move to the next hall
        hallNumber++;
        seatIndex = 1;
        prepareNewHall();
    }

    // Allocate seats in the current hall
    for (; studentIndex < studentsPerCourse.length; studentIndex++) {
        for (int i = 1; i <= studentsPerCourse[studentIndex]; i++) {
            if (seatIndex > hallCapacity) {
                return; // Exit if hall capacity is exceeded
            }
            JPanel buttonPanel = new JPanel(new BorderLayout());
            buttonPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            buttonPanel.setBackground(new Color(255, 228, 225));
            int setIndex = (seatIndex - 1) % baseNumbers.length;
            int increment = ((seatIndex - 1) / baseNumbers.length) + 1;
            int seatNumber = baseNumbers[setIndex] + increment;
            JLabel centerLabel = new JLabel(String.valueOf(seatNumber), JLabel.CENTER);
            centerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            centerLabel.setForeground(Color.RED);
            JLabel bottomRightLabel = new JLabel(String.valueOf(seatIndex), JLabel.RIGHT);
            bottomRightLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            bottomRightLabel.setForeground(Color.RED);
            buttonPanel.add(centerLabel, BorderLayout.CENTER);
            buttonPanel.add(bottomRightLabel, BorderLayout.SOUTH);
            seatPanel.add(buttonPanel);

            preparedStatement.setInt(1, hallNumber);
            preparedStatement.setInt(2, seatIndex);
            preparedStatement.setDouble(3, seatNumber);
            preparedStatement.addBatch();

            seatIndex++;
        }
    }

    preparedStatement.executeBatch();

    // Check if there are more students remaining for allocation
    if (studentIndex < studentsPerCourse.length) {
        allocateNextBatchOfStudents(); // Recursively allocate seats for the remaining students
    }
}

    private static void addInvigilatorInfo(JPanel mainPanel, int hallNumber) {
        JPanel invigilatorsPanel = new JPanel(new GridLayout(2, 1));
        JLabel invigilatorsLabel = new JLabel("Invigilators for Hall-" + hallNumber + ":", JLabel.CENTER);
        invigilatorsLabel.setFont(new Font("Serif", Font.BOLD, 16));

        StringBuilder invigilatorsNames = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String teacherQuery = "SELECT sname FROM teacher_allocation WHERE hallno = ?";
            PreparedStatement teacherStmt = conn.prepareStatement(teacherQuery);
            teacherStmt.setInt(1, hallNumber);
            ResultSet rs = teacherStmt.executeQuery();
            while (rs.next()) {
                if (invigilatorsNames.length() > 0) {
                    invigilatorsNames.append(", ");
                }
                invigilatorsNames.append(rs.getString("sname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JLabel invigilatorsNamesLabel = new JLabel(invigilatorsNames.toString(), JLabel.CENTER);
        invigilatorsNamesLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        invigilatorsPanel.add(invigilatorsLabel);
        invigilatorsPanel.add(invigilatorsNamesLabel);

        mainPanel.add(invigilatorsPanel, BorderLayout.SOUTH);
    }

    private static void updateBaseNumbers() {
        int lastFourSeatNumbers[] = new int[baseNumbers.length];
        int lastSeatIndex = seatIndex - 1;
        for (int i = 0; i < baseNumbers.length; i++) {
            int setIndex = (lastSeatIndex - baseNumbers.length + i) % baseNumbers.length;
            int increment = ((lastSeatIndex - baseNumbers.length + i) / baseNumbers.length) + 1;
            lastFourSeatNumbers[i] = baseNumbers[setIndex] + increment;
        }

        for (int i = 0; i < baseNumbers.length; i++) {
            baseNumbers[i] = lastFourSeatNumbers[i] + 1;
        }
    }
}

