import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
//import java.time.LocalTime;

public class studentlogin extends JFrame {

    public studentlogin() {
        // Set up the frame
        setTitle("Covai Institute of Technology - login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(new Color(204, 229, 255)); // Light Blue

        // Heading
        JPanel headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headingLabel = new JLabel("Covai Institute of Technology");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(Color.blue);
        headingPanel.add(headingLabel);
        mainPanel.add(headingPanel);

        // Sub-heading
        JPanel subHeadingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel subHeadingLabel = new JLabel("Login");
        subHeadingLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subHeadingLabel.setForeground(Color.RED);
        subHeadingPanel.add(subHeadingLabel);
        mainPanel.add(subHeadingPanel);

        // Empty space
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Side heading
        JLabel sideHeadingLabel = new JLabel("Student Login");
        sideHeadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sideHeadingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        sideHeadingLabel.setForeground(Color.BLUE);
        mainPanel.add(sideHeadingLabel);

        // Username field
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setPreferredSize(new Dimension(100, 30));
        usernamePanel.add(usernameLabel);
        JTextField usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(200, 30));
        usernamePanel.add(usernameField);
        mainPanel.add(usernamePanel);

        // Password field
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setPreferredSize(new Dimension(100, 30));
        passwordPanel.add(passwordLabel);
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(150, 30));
        passwordPanel.add(passwordField);

        // Password show/hide checkbox
        JCheckBox showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
        showPasswordCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                passwordField.setEchoChar(cb.isSelected() ? '\u0000' : '*'); // Show/hide password
            }
        });
        passwordPanel.add(showPasswordCheckbox);
        mainPanel.add(passwordPanel);

        // Empty space
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setBackground(Color.BLUE);
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(100, 40));
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (authenticate(username, password)) {
                    // Directly open the next window without showing the success message
                    dispose();
                    openStudentDetails(username); // Pass username to openStudentDetails method
                } else {
                    JOptionPane.showMessageDialog(studentlogin.this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        mainPanel.add(loginButton);

        // Check the time conditions
        /*LocalTime time = LocalTime.now();
        if (time.isAfter(LocalTime.of(18, 0)) || time.isBefore(LocalTime.of(12, 30))) {
            // Condition 1: Time between 6:00 PM and next day 12:30 PM
            // Your code for condition 1 goes here
            System.out.println("Condition 1: Time between 6:00 PM and next day 12:30 PM");
        } else if (time.isAfter(LocalTime.of(12, 30)) && time.isBefore(LocalTime.of(14, 0))) {
            // Condition 2: Time between 12:30 PM and 2:00 PM
            // Your code for condition 2 goes here
            System.out.println("Condition 2: Time between 12:30 PM and 2:00 PM");
        } else {
            // Condition 3: All other times
            // Your code for condition 3 goes here
            System.out.println("Condition 3: All other times");
        }*/

        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);

        // Display the window
        pack();
        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);
    }

    // Database authentication method for student
    private boolean authenticate(String username, String password) {
        String url = "jdbc:mariadb://localhost:3307/student";
        String user = "root";
        String dbPassword = "root123";
        String query = "SELECT * FROM student_details WHERE susername=? AND spassword=?";

        try (Connection connection = DriverManager.getConnection(url, user, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Returns true if a row is present, i.e., authentication successful
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false; // Authentication failed
    }

    // Method to open StudentDetails
    private void openStudentDetails(String username) {
        StudentDetails studentDetails = new StudentDetails(username); // Pass username to constructor
        studentDetails.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(studentlogin::new);
    }
}
