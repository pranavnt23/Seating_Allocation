

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class seatdsplayy {

    public static void main(String[] args) {
        // Database connection details
        String dbUrl = "jdbc:mariadb://localhost:3307/allocation";
        String user = "root";
        String dbPassword = "root123";

        // Create a new JFrame container
        JFrame frame = new JFrame("Seat Matrix of 60 Students");

        // Set the frame's size
        frame.setSize(800, 800);

        // Specify an action for the close button
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the heading panel
        JPanel headingPanel = new JPanel(new GridLayout(5, 1));
        headingPanel.setBackground(new Color(173, 216, 230)); // Light Blue color

        JLabel instituteLabel = new JLabel("COVAI INSTITUTE OF TECHNOLOGY ", JLabel.CENTER);
        instituteLabel.setFont(new Font("Serif", Font.BOLD, 20));

        JLabel hallLabel = new JLabel("Hall-II", JLabel.CENTER);
        hallLabel.setFont(new Font("Serif", Font.BOLD, 18));

        JLabel examLabel = new JLabel("Theory Examination - May 2024", JLabel.CENTER);
        examLabel.setFont(new Font("Serif", Font.BOLD, 18));

        JLabel seatArrangementLabel = new JLabel("Seat Arrangement", JLabel.CENTER);
        seatArrangementLabel.setFont(new Font("Serif", Font.BOLD, 16));

        // Fetch the invigilators for the specified hall
        JLabel invigilatorsLabel = new JLabel("Invigilators: ", JLabel.CENTER);
        invigilatorsLabel.setFont(new Font("Serif", Font.BOLD, 16));

        StringBuilder invigilatorsNames = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(dbUrl, user, dbPassword)) {
            String teacherQuery = "SELECT sname FROM teacher_allocation WHERE hallno = 102";
            PreparedStatement teacherStmt = conn.prepareStatement(teacherQuery);
            teacherStmt.setInt(1, 101); // Assuming Hall-II corresponds to hallno 101
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

        // Create a panel for invigilators information
        JPanel invigilatorsPanel = new JPanel(new GridLayout(2, 1));
        invigilatorsPanel.add(invigilatorsLabel);
        invigilatorsPanel.add(invigilatorsNamesLabel);

        headingPanel.add(instituteLabel);
        headingPanel.add(hallLabel);
        headingPanel.add(examLabel);
        headingPanel.add(seatArrangementLabel);
        headingPanel.add(invigilatorsPanel);

        // Create a JPanel with GridLayout for the seat matrix
        JPanel seatPanel = new JPanel(new GridLayout(6, 10, 5, 5)); // 6 rows and 10 columns
        seatPanel.setBackground(new Color(240, 255, 255)); // Light Cyan color

        // Define the base seat numbers
        int[] baseNumbers = {71761015, 71762015, 71763015, 71764015};

        try (Connection conn = DriverManager.getConnection(dbUrl, user, dbPassword)) {
            String sql = "INSERT INTO sallocation (hallno, seatno, sroll_no) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Add buttons with seat numbers to the seatPanel
            for (int i = 1; i <= 60; i++) {
                JPanel buttonPanel = new JPanel(new BorderLayout());
                buttonPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                buttonPanel.setBackground(new Color(255, 228, 225)); // Misty Rose color

                // Calculate seat number
                int setIndex = (i - 1) % 4;
                int increment = ((i - 1) / 4) + 1;
                int seatNumber = baseNumbers[setIndex] + increment;

                JLabel centerLabel = new JLabel(String.valueOf(seatNumber), JLabel.CENTER);
                centerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                centerLabel.setForeground(Color.RED); // Set the text color to red

                JLabel bottomRightLabel = new JLabel(String.valueOf(i), JLabel.RIGHT);
                bottomRightLabel.setFont(new Font("Arial", Font.PLAIN, 10));
                bottomRightLabel.setForeground(Color.RED); // Set the text color to red

                buttonPanel.add(centerLabel, BorderLayout.CENTER);
                buttonPanel.add(bottomRightLabel, BorderLayout.SOUTH);

                seatPanel.add(buttonPanel);

                // Insert the seat allocation into the database
                pstmt.setInt(1, 101); // Assuming Hall-II is represented by hallno 101
                pstmt.setInt(2, i);
                pstmt.setDouble(3, seatNumber);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add the heading and seat panels to the main panel
        mainPanel.add(headingPanel, BorderLayout.NORTH);
        mainPanel.add(seatPanel, BorderLayout.CENTER);

        // Add the main panel to the frame
        frame.add(mainPanel);

        // Display the frame
        frame.setVisible(true);
    }
}

