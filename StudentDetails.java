import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentDetails extends JFrame {

    private JLabel rollNoLabel, nameLabel;
    private JLabel titleLabel;
    private String rollNo;  // Add this to store the roll number

    public StudentDetails(String username) {
        setTitle("Student Details");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(new Color(204, 229, 255));

        setLayout(new GridBagLayout());

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(new Color(204, 229, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        titleLabel = new JLabel("STUDENT DETAILS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 153));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        detailsPanel.add(titleLabel, gbc);

        String url = "jdbc:mariadb://localhost:3307/student";
        String user = "root";
        String dbPassword = "root123";
        String query = "SELECT sroll_no, sname, deptname FROM student_details WHERE susername=?";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    rollNo = resultSet.getString("sroll_no");  // Store the roll number
                    String name = resultSet.getString("sname");
                    String dept = resultSet.getString("deptname");

                    titleLabel.setText("WELCOME ! " + name);

                    rollNoLabel = new JLabel("Roll No: " + rollNo);
                    nameLabel = new JLabel("Name: " + name);

                    rollNoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                    nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

                    rollNoLabel.setHorizontalAlignment(JLabel.CENTER);
                    nameLabel.setHorizontalAlignment(JLabel.CENTER);

                    JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
                    infoPanel.setBackground(new Color(204, 229, 255));
                    infoPanel.add(rollNoLabel);
                    infoPanel.add(nameLabel);

                    gbc.gridy = 1;
                    gbc.gridwidth = 2;
                    gbc.anchor = GridBagConstraints.CENTER;
                    detailsPanel.add(infoPanel, gbc);

                    JButton viewTimeTableButton = new JButton("View Time Table");
                    viewTimeTableButton.setFont(new Font("Arial", Font.PLAIN, 14));
                    viewTimeTableButton.setBackground(new Color(255, 203, 203));
                    viewTimeTableButton.setForeground(Color.BLACK);
                    viewTimeTableButton.setFocusPainted(false);
                    viewTimeTableButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            displayTimeTable(dept);
                        }
                    });

                    gbc.gridy = 2;
                    gbc.gridwidth = 1;
                    gbc.anchor = GridBagConstraints.LINE_START;
                    detailsPanel.add(viewTimeTableButton, gbc);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JButton showHallDetailsButton = new JButton("Show Hall Details");
        showHallDetailsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        showHallDetailsButton.setBackground(new Color(255, 203, 203));
        showHallDetailsButton.setForeground(Color.BLACK);
        showHallDetailsButton.setFocusPainted(false);
        showHallDetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showHallDetails();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        detailsPanel.add(showHallDetailsButton, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        JButton goBackButton = new JButton("Go Back");
        goBackButton.setFont(new Font("Arial", Font.PLAIN, 14));
        goBackButton.setBackground(new Color(255, 203, 203));
        goBackButton.setForeground(Color.BLACK);
        goBackButton.setFocusPainted(false);
        goBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newFramz newFrame = new newFramz();
                newFrame.setVisible(true);
                dispose();
            }
        });
        buttonPanel.add(goBackButton);

        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.insets = new Insets(20, 20, 20, 20);
        mainGbc.anchor = GridBagConstraints.CENTER;
        add(detailsPanel, mainGbc);

        mainGbc.gridy = 1;
        add(buttonPanel, mainGbc);

        setVisible(true);
    }

    private void displayTimeTable(String dept) {
        String url = "jdbc:mariadb://localhost:3307/coe";
        String user = "root";
        String dbPassword = "root123";
        String query = "SELECT * FROM TimeTable WHERE dept=?";

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        tableModel.addColumn("Date");
        tableModel.addColumn("Session");
        tableModel.addColumn("Course ID");
        tableModel.addColumn("Course Name");

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, dept);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String date = resultSet.getString("date");
                    String session = resultSet.getString("session");
                    String courseID = resultSet.getString("course_id");
                    String courseName = resultSet.getString("course_name");

                    tableModel.addRow(new Object[]{date, session, courseID, courseName});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JFrame timetableFrame = new JFrame("Time Table");
        timetableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        timetableFrame.setLayout(new BorderLayout());
        timetableFrame.add(scrollPane, BorderLayout.CENTER);
        timetableFrame.pack();
        timetableFrame.setLocationRelativeTo(null);
        timetableFrame.setVisible(true);
    }

    private void showHallDetails() {
        String url = "jdbc:mariadb://localhost:3307/allocation";
        String user = "root";
        String dbPassword = "root123";
        String query = "SELECT hallno, seatno FROM sallocation WHERE sroll_no=?";

        String hallNumber = "";
        String seatNumber = "";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, rollNo);  // Use the stored roll number
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    hallNumber = resultSet.getString("hallno");
                    seatNumber = resultSet.getString("seatno");

                    // Now we have the hall number and seat number, we can proceed to display them
                    JFrame hallDetailsFrame = new JFrame("Hall Details");
                    hallDetailsFrame.setSize(300, 200);
                    hallDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    hallDetailsFrame.setLocationRelativeTo(null);

                    JPanel hallDetailsPanel = new JPanel();
                    hallDetailsPanel.setLayout(new BoxLayout(hallDetailsPanel, BoxLayout.Y_AXIS));
                    hallDetailsPanel.setBackground(new Color(204, 229, 255));

                    JLabel hallDetailsLabel = new JLabel("Your Hall Details");
                    hallDetailsLabel.setFont(new Font("Arial", Font.BOLD, 18));
                    hallDetailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    JLabel hallNumberLabel = new JLabel("Hall Number: " + hallNumber);
                    hallNumberLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                    hallNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    JLabel seatNumberLabel = new JLabel("Seat Number: " + seatNumber);
                    seatNumberLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                    seatNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    hallDetailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
                    hallDetailsPanel.add(hallDetailsLabel);
                    hallDetailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    hallDetailsPanel.add(hallNumberLabel);
                    hallDetailsPanel.add(seatNumberLabel);

                    hallDetailsFrame.add(hallDetailsPanel);
                    hallDetailsFrame.setVisible(true);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDetails("testuser"));
    }
    }