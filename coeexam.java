import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class coeexam extends JFrame implements ActionListener {
    private JFrame resultFrame;
    private JPanel mainPanel, contentPanel;
    private JLabel titleLabel, studentLabel, hallLabel, invigilatorLabel;
    private JComboBox<String> courseComboBox, hallComboBox;
    private JButton showButton, showDetailsButton, showHallButton, goBackButton;
    private Connection studentConnection, teacherConnection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public coeexam() {
        setTitle("Controller of Examination");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());

        contentPanel = new JPanel(new GridLayout(7, 2));
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(50, 50, 50, 50), // Add padding
                BorderFactory.createLineBorder(Color.BLACK, 2))); // Double line border

        // Background color
        contentPanel.setBackground(new Color(204, 229, 255)); // Light Blue

        titleLabel = new JLabel("<html><b>Controller of Examination</b></html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 20)); // Change font size and style
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true); 
        titleLabel.setBackground(new Color(0, 0, 102));

        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        contentPanel.add(titleLabel);
        contentPanel.add(new JLabel());

        studentLabel = new JLabel("<html><b>Show Student Details</b></html>");
        studentLabel.setFont(new Font("Consolas", Font.BOLD, 20)); 
        contentPanel.add(studentLabel);
        contentPanel.add(new JLabel());
        courseComboBox = new JComboBox<>(new String[]{"Software Systems", "Computer Science Engineering",
                "Data Science", "Artificial Intelligence and Machine Learning"});
        contentPanel.add(courseComboBox);

        showButton = new JButton("Show");
        showButton.addActionListener(this);
        showButton.setPreferredSize(new Dimension(100, 30));
        contentPanel.add(showButton);

        hallLabel = new JLabel("<html><b>Show Hall Details</b></html>");
        contentPanel.add(hallLabel);
        contentPanel.add(new JLabel());
        hallComboBox = new JComboBox<>(new String[]{"Hall allocation"});
        contentPanel.add(hallComboBox);
        showHallButton = new JButton("Allocate halls");
        showHallButton.addActionListener(this);
        contentPanel.add(showHallButton);

        invigilatorLabel = new JLabel("<html><b>Show Invigilators Detail</b></html>");
        contentPanel.add(invigilatorLabel);
        contentPanel.add(new JLabel());
        showDetailsButton = new JButton("Show Details");
        showDetailsButton.addActionListener(this);
        contentPanel.add(showDetailsButton);
        

        goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(this);
        goBackButton.setPreferredSize(new Dimension(100, 30));
        contentPanel.add(goBackButton);

        // Add contentPanel to mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add mainPanel to JFrame
        add(mainPanel);

        try {
            String studentUrl = "jdbc:mariadb://localhost:3307/student";
            String teacherUrl = "jdbc:mariadb://localhost:3307/teacher";
            String user = "root";
            String dbPassword = "root123";
            studentConnection = DriverManager.getConnection(studentUrl, user, dbPassword);
            teacherConnection = DriverManager.getConnection(teacherUrl, user, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == showButton) {
            try {
                String course = (String) courseComboBox.getSelectedItem();

                String studentQuery = "SELECT sroll_no, sname, deptname FROM student_details WHERE deptname = ?";
                preparedStatement = studentConnection.prepareStatement(studentQuery);
                preparedStatement.setString(1, course);
                resultSet = preparedStatement.executeQuery();

                showStudentResult(resultSet);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == showDetailsButton) {
            try {
                String teacherQuery = "SELECT * FROM teacher_details";
                preparedStatement = teacherConnection.prepareStatement(teacherQuery);
                resultSet = preparedStatement.executeQuery();

                showTeacherResult(resultSet);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == showHallButton) {
            String hall = (String) hallComboBox.getSelectedItem();
            if (hall.equals("Hall allocation")) {
                SwingUtilities.invokeLater(() -> seatdisplay.main(new String[]{}));
            } else if (hall.equals("Hall2")) {
                SwingUtilities.invokeLater(() -> seatdsplayy.main(new String[]{}));
            } else {
                try {
                    String hallQuery = "SELECT * FROM hall_details WHERE hallname = ?";
                    preparedStatement = teacherConnection.prepareStatement(hallQuery);
                    preparedStatement.setString(1, hall);
                    resultSet = preparedStatement.executeQuery();

                    showHallResult(resultSet);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == goBackButton) {
            newFramz newFramz = new newFramz();
            newFramz.setVisible(true);
            dispose(); // Close the current frame
        }
    }

    private void showStudentResult(ResultSet resultSet) throws SQLException {
        resultFrame = new JFrame("Student Details");
        resultFrame.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        model.addColumn("Roll No");
        model.addColumn("Name");
        model.addColumn("Department");

        while (resultSet.next()) {
            model.addRow(new Object[]{
                    resultSet.getInt("sroll_no"),
                    resultSet.getString("sname"),
                    resultSet.getString("deptname")
            });
        }

        resultFrame.add(scrollPane, BorderLayout.CENTER);
        resultFrame.setSize(800, 400);
        resultFrame.setVisible(true);
    }

    private void showTeacherResult(ResultSet resultSet) throws SQLException {
        resultFrame = new JFrame("Teacher Details");
        resultFrame.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Department");

        while (resultSet.next()) {
            model.addRow(new Object[]{
                    resultSet.getInt("tid"),
                    resultSet.getString("tname"),
                    resultSet.getString("dept")
            });
        }

        


        resultFrame.add(scrollPane, BorderLayout.CENTER);
        resultFrame.setSize(800, 400);
        resultFrame.setVisible(true);
    }

    public static void main(String[] args) {
        coeexam coeexam = new coeexam();
        coeexam.setVisible(true);
    }

    private void showHallResult(ResultSet resultSet) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}