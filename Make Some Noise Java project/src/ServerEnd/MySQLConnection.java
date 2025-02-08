package ServerEnd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    // MySQL Connection URL
    private static final String URL = "jdbc:mysql://localhost:3306/makeSomeNoiseDatabase";
    private static final String USER = "remoteUser";
    private static final String PASSWORD = "remotePassword";

    public static void main(String[] args) {
        try {
            // Establish connection
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL successfully!");

            // Close connection
            conn.close();
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}