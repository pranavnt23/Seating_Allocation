import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TeacherGUI extends JFrame implements ActionListener {

    private JLabel presentsLabel, absenteesLabel, malpracticeLabel;
    private JTextField presentsField;
    private JButton submitButton; // Removed goBackButton
    private JTextArea absenteesTextArea, malpracticeTextArea;

    public TeacherGUI() {
        setTitle("Teacher Panel");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(204, 229, 255)); 
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Teacher Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.PAGE_START;
        mainPanel.add(titleLabel, gbc);

        presentsLabel = new JLabel("Number of Presents:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        mainPanel.add(presentsLabel, gbc);

        presentsField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(presentsField, gbc);

        absenteesLabel = new JLabel("Absentees List:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(absenteesLabel, gbc);

        absenteesTextArea = new JTextArea(5, 20);
        JScrollPane textAreaScrollPane1 = new JScrollPane(absenteesTextArea);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH; 
        mainPanel.add(textAreaScrollPane1, gbc);

        malpracticeLabel = new JLabel("Malpractice Report:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridheight = 1;
        mainPanel.add(malpracticeLabel, gbc);

        malpracticeTextArea = new JTextArea(5, 20);
        JScrollPane textAreaScrollPane2 = new JScrollPane(malpracticeTextArea);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridheight = 1;
        mainPanel.add(textAreaScrollPane2, gbc);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        mainPanel.add(submitButton, gbc);

        // Removed the Go Back Button
        // Styling
        stylizeComponents();

        add(mainPanel);
        setLocationRelativeTo(null); 
        setVisible(true);
    }

    private void stylizeComponents() {
        // Styling labels
        presentsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        absenteesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        malpracticeLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Styling text fields
        Font textFieldFont = new Font("Arial", Font.PLAIN, 14);
        presentsField.setFont(textFieldFont);

        // Styling buttons
        submitButton.setBackground(Color.BLUE);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Styling text areas
        Font textAreaFont = new Font("Arial", Font.PLAIN, 14);
        absenteesTextArea.setFont(textAreaFont);
        malpracticeTextArea.setFont(textAreaFont);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TeacherGUI();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String presents = presentsField.getText();
            String absenteesList = absenteesTextArea.getText();
            String malpracticeReport = malpracticeTextArea.getText();
            
            // Database connection
            String teacherUrl = "jdbc:mariadb://localhost:3307/teacher";
            String user = "root";
            String dbPassword = "root123";
            
            // Update attendance table
            String attendanceQuery = "INSERT INTO attendence (present, absentees_list) VALUES (?, ?)";
            try (Connection connection = DriverManager.getConnection(teacherUrl, user, dbPassword);
                 PreparedStatement preparedStatement = connection.prepareStatement(attendanceQuery)) {
                preparedStatement.setString(1, presents);
                preparedStatement.setString(2, absenteesList);
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            // Update malpractice table
            String malpracticeQuery = "INSERT INTO malpractice (report) VALUES (?)";
            try (Connection connection = DriverManager.getConnection(teacherUrl, user, dbPassword);
                 PreparedStatement preparedStatement = connection.prepareStatement(malpracticeQuery)) {
                preparedStatement.setString(1, malpracticeReport);
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            System.out.println("Number of Presents: " + presents);
            System.out.println("Absentees List: \n" + absenteesList);
            System.out.println("Malpractice Report: \n" + malpracticeReport);
            
            JOptionPane.showMessageDialog(this, "Details updated", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
