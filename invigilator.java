import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class invigilator {
    public static void main(String[] args) {
        String url = "jdbc:mariadb://localhost:3307/";
        String databaseName = "db1";
        String userName = "root";
        String password = "root123";

        try (Connection connection = DriverManager.getConnection(url, userName, password)) {
            createDatabase(connection, databaseName);

            connection.setCatalog(databaseName);

            createTables(connection);

            JOptionPane.showMessageDialog(null, "Tables created and queries executed successfully",
                    "System Message", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void createDatabase(Connection connection, String databaseName) throws Exception {
        Statement statement = connection.createStatement();
        String sql = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        statement.executeUpdate(sql);
        statement.close();
    }

    private static void createTables(Connection connection) throws Exception {
        
        String createTableSQL = "CREATE TABLE IF NOT EXISTS numbers1 (nos INT)";
        executeUpdate(connection, createTableSQL);

        
        String userInput = JOptionPane.showInputDialog(null, "Enter a number:");
        int number;
        try {
            
            number = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
       
        String insertDataSQL = "INSERT INTO numbers1 (nos) VALUES (" + number + ")";
        executeUpdate(connection, insertDataSQL);
 
    }

    private static void executeUpdate(Connection connection, String sql) throws Exception {
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }
}
