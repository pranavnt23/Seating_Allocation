import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class newFramz extends JFrame {
    JButton student, admin, invigilator;
    JLabel header;
    JPanel cont;
    
    newFramz() 
    {
        setTitle("AdminPage");
        setVisible(true);
        setSize(1300, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setBackground(new Color(199, 199, 22));

        cont = new JPanel();
        cont.setBackground(new Color(255, 255, 255));
        cont.setLayout(new GridLayout(1, 3, 20, 20)); 

        header = new JLabel(" EXAMGRID-SMART SEATING SOLUTIONS", SwingConstants.CENTER); // Set text alignment to center
        header.setFont(new Font("Consolas", Font.PLAIN, 45));
        header.setForeground(Color.WHITE);
        header.setOpaque(true); 
        header.setBackground(new Color(0, 0, 102)); 
        student = new JButton("COE");
        student.setFont(student.getFont().deriveFont(Font.PLAIN, 18));
        student.setIcon(new ImageIcon(new ImageIcon("C:\\Users\\Deepika\\Desktop\\JAVA\\teacher\\src\\admin.png").getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH))); // Resized image
        student.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              
                new AdminLogin();
            }
        });

        admin = new JButton("STUDENT");
        admin.setFont(admin.getFont().deriveFont(Font.PLAIN, 18)); 
        admin.setIcon(new ImageIcon(new ImageIcon("C:\\Users\\Deepika\\Desktop\\JAVA\\teacher\\src\\student.png").getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH))); // Resized image
        admin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the Student login window
                new studentlogin();
            }
        });
        
        
        invigilator = new JButton("INVIGILATOR");
        invigilator.setFont(invigilator.getFont().deriveFont(Font.PLAIN, 18)); 
        invigilator.setIcon(new ImageIcon(new ImageIcon("C:\\Users\\Deepika\\Desktop\\JAVA\\teacher\\src\\third.png").getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH))); // Resized image
        invigilator.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               
                new InvigilatorLogin();
            }
        });

        cont.add(student);
        cont.add(admin);
        cont.add(invigilator);

        add(cont);
        add(header, BorderLayout.NORTH); // Setting header to the top
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new newFramz());
    }
}


