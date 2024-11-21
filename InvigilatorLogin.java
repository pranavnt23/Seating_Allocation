import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class InvigilatorLogin extends JFrame {

    public InvigilatorLogin() {
        // Set up the frame
        setTitle("Covai Institute of Technology - Invigilator Login");
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
        JLabel subHeadingLabel = new JLabel("Invigilator Login");
        subHeadingLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subHeadingLabel.setForeground(Color.RED);
        subHeadingPanel.add(subHeadingLabel);
        mainPanel.add(subHeadingPanel);

        // Empty space
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

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

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

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
                    openInvigilatorDetails(username); // Open InvigilatorDetails window
                } else {
                    JOptionPane.showMessageDialog(InvigilatorLogin.this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        mainPanel.add(loginButton);

        add(mainPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null); 
        setVisible(true);
    }

    private boolean authenticate(String username, String password) {
        String url = "jdbc:mariadb://localhost:3307/teacher";
        String user = "root";
        String dbPassword = "root123";
        String query = "SELECT * FROM teacher_details WHERE tusername=? AND tpassword=?";

        try {
            Connection connection = DriverManager.getConnection(url, user, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true; // Authentication successful
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false; // Authentication failed
    }
    
    // Method to open InvigilatorDetails
    private void openInvigilatorDetails(String username) {
        InvigilatorDetails invigilatorDetails = new InvigilatorDetails(username);
        invigilatorDetails.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InvigilatorLogin::new);
    }
}
