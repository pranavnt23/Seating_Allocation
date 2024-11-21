import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class InvigilatorDetails extends JFrame implements ActionListener {
    private JButton showHallDetailsButton, enterHallDetailsButton, goBackButton;
    private String username;
    private String teacherName;
    private String teacherDept;
    private int hallNumber;
    private String coInvigilatorName;
    private String coInvigilatorDept;

    public InvigilatorDetails(String username) {
        this.username = username;

        // Fetch teacher details from the database
        fetchTeacherDetails();

        setTitle("Invigilator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        getContentPane().setBackground(new Color(204, 229, 255));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(welcomeLabel, gbc);

        gbc.gridy = 1;
        JLabel teacherNameLabel = new JLabel("Teacher Name: " + teacherName);
        teacherNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(teacherNameLabel, gbc);

        gbc.gridy = 2;
        JLabel teacherDeptLabel = new JLabel("Teacher Department: " + teacherDept);
        teacherDeptLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(teacherDeptLabel, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        showHallDetailsButton = new JButton("Show Hall Details");
        showHallDetailsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        showHallDetailsButton.setBackground(new Color(255, 203, 203));
        showHallDetailsButton.setForeground(Color.BLACK);
        showHallDetailsButton.setPreferredSize(new Dimension(160, 40));
        showHallDetailsButton.addActionListener(this);
        add(showHallDetailsButton, gbc);

        gbc.gridy = 4;
        enterHallDetailsButton = new JButton("Enter Hall Details");
        enterHallDetailsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        enterHallDetailsButton.setBackground(new Color(255, 203, 203));
        enterHallDetailsButton.setForeground(Color.BLACK);
        enterHallDetailsButton.setPreferredSize(new Dimension(160, 40));
        enterHallDetailsButton.addActionListener(this);
        add(enterHallDetailsButton, gbc);

        gbc.gridy = 5;
        goBackButton = new JButton("Go Back");
        goBackButton.setFont(new Font("Arial", Font.PLAIN, 14));
        goBackButton.setBackground(new Color(255, 203, 203));
        goBackButton.setForeground(Color.BLACK);
        goBackButton.setPreferredSize(new Dimension(160, 40));
        goBackButton.addActionListener(this);
        add(goBackButton, gbc);
    }

    private void fetchTeacherDetails() {
        String url = "jdbc:mariadb://localhost:3307/teacher";
        String user = "root";
        String password = "root123";
        String query = "SELECT tname, dept FROM teacher_details WHERE tusername = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                teacherName = resultSet.getString("tname");
                teacherDept = resultSet.getString("dept");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchHallNumber() {
        String url = "jdbc:mariadb://localhost:3307/allocation";
        String user = "root";
        String password = "root123";
        String query = "SELECT hallno FROM teacher_allocation WHERE sname = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, teacherName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                hallNumber = resultSet.getInt("hallno");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchCoInvigilatorDetails() {
        String url = "jdbc:mariadb://localhost:3307/allocation";
        String user = "root";
        String password = "root123";
        String query = "SELECT sname, dept FROM teacher_allocation WHERE hallno = ? AND sname != ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, hallNumber);
            preparedStatement.setString(2, teacherName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                coInvigilatorName = resultSet.getString("sname");
                coInvigilatorDept = resultSet.getString("dept");
            } else {
                coInvigilatorName = "N/A";
                coInvigilatorDept = "N/A";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == showHallDetailsButton) {
            fetchHallNumber();
            fetchCoInvigilatorDetails();
            JFrame hallDetailsFrame = new JFrame("Hall Details");
            hallDetailsFrame.setSize(300, 300);
            hallDetailsFrame.getContentPane().setBackground(new Color(204, 229, 255));

            hallDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            hallDetailsFrame.setLocationRelativeTo(null);
            hallDetailsFrame.setLayout(new GridLayout(8, 1));

            JLabel headerLabel = new JLabel("Your Hall Details", SwingConstants.CENTER);
            headerLabel.setFont(new Font("Arial", Font.BOLD, 40));
            hallDetailsFrame.add(headerLabel);

            JLabel nameLabel = new JLabel("Name: " + teacherName, SwingConstants.CENTER);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            hallDetailsFrame.add(nameLabel);

            JLabel deptLabel = new JLabel("Department: " + teacherDept, SwingConstants.CENTER);
            deptLabel.setFont(new Font("Arial", Font.BOLD, 16));
            hallDetailsFrame.add(deptLabel);

            JLabel hallLabel = new JLabel("Hall Number: " + hallNumber, SwingConstants.CENTER);
            hallLabel.setFont(new Font("Arial", Font.BOLD, 16));
            hallDetailsFrame.add(hallLabel);

            JLabel coInvigilatorHeaderLabel = new JLabel("Co-Invigilator Details", SwingConstants.CENTER);
            coInvigilatorHeaderLabel.setFont(new Font("Arial", Font.BOLD, 16));
            hallDetailsFrame.add(coInvigilatorHeaderLabel);

            JLabel coNameLabel = new JLabel("Name: " + coInvigilatorName, SwingConstants.CENTER);
            coNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            hallDetailsFrame.add(coNameLabel);

            JLabel coDeptLabel = new JLabel("Department: " + coInvigilatorDept, SwingConstants.CENTER);
            coDeptLabel.setFont(new Font("Arial", Font.BOLD, 16));
            hallDetailsFrame.add(coDeptLabel);

            hallDetailsFrame.setVisible(true);
        } else if (e.getSource() == enterHallDetailsButton) {
            TeacherGUI teacherGUI = new TeacherGUI();
            teacherGUI.setVisible(true);
            this.setVisible(false); // Hide the current frame
        } else if (e.getSource() == goBackButton) {
            newFramz newFramz = new newFramz(); // Create an instance of NewFrame
            newFramz.setVisible(true); // Show NewFrame
            this.dispose(); // Close the current frame
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InvigilatorDetails invigilatorDetails = new InvigilatorDetails("SampleUsername");
            invigilatorDetails.setVisible(true);
        });
    }
}
