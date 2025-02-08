package ServerEnd;

import java.sql.*;

public class MySQLConnection {
    // MySQL Connection URL
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/makeSomeNoiseDatabase";
    private static final String USER = "appUser";
    private static final String PASSWORD = "make some noise";

    public static void main(String[] args) {
        try {
            // Establish connection
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL successfully!");

            String sql = "SELECT * FROM accounts";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "value");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("Column1: " + rs.getString("column1"));
            }

            // Close connection
            conn.close();
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}