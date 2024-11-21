import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminLogin extends JFrame {

    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://localhost:3307/coe";
    static final String USER = "root";
    static final String PASS = "root123";

    private JTextField usernameField;
    private JPasswordField passwordField;

    public AdminLogin() {
        // Set up the frame
        setTitle("Covai Institute of Technology - Admin Login");
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
        JLabel subHeadingLabel = new JLabel("Admin Login");
        subHeadingLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subHeadingLabel.setForeground(Color.RED);
        subHeadingPanel.add(subHeadingLabel);
        mainPanel.add(subHeadingPanel);

        // Empty space
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Username field
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel usernameLabel = new JLabel("Admin ID:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setPreferredSize(new Dimension(100, 30));
        usernamePanel.add(usernameLabel);
        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(200, 30));
        usernamePanel.add(usernameField);
        mainPanel.add(usernamePanel);

        // Password field
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setPreferredSize(new Dimension(100, 30));
        passwordPanel.add(passwordLabel);
        passwordField = new JPasswordField(20);
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

        // Sign in button
        JButton signInButton = new JButton("Log In");
        signInButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        signInButton.setBackground(Color.BLUE);
        signInButton.setForeground(Color.WHITE);
        signInButton.setPreferredSize(new Dimension(100, 40));
        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String adminID = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (checkAdminCredentials(adminID, password)) {
                    // Open ExamController GUI
                    new coeexam().setVisible(true);
                    // Close the AdminLogin window
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Admin ID or Password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        mainPanel.add(signInButton);

        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);

        // Display the window
        pack();
        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);
    }

    // Method to check admin credentials
    private boolean checkAdminCredentials(String adminID, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean isValid = false;
        
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT * FROM admin_details WHERE ausername = ? AND apassword = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, adminID);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                isValid = true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return isValid;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminLogin::new);
    }
}
