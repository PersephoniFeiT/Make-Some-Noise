package ServerEnd;

import Exceptions.Accounts.DatabaseConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/makesomenoise";
    private static final String USER = "appUser";
    private static final String PASSWORD = "make some noise";

    public static int insert(String tableName, String[] values) throws DatabaseConnectionException {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName +  " VALUES (");
        for (String v : values){
            sql.append(v);
        }
        sql.append(")");  // Close the VALUES()

        Integer ID = SQLConnection.queryUpdate(sql.toString());
        if (ID == null)
            throw new DatabaseConnectionException("Database connection error: Could not insert.");
        else return ID.intValue();
    }

    public static void update(String tableName, int ID, String columnName, String value) throws DatabaseConnectionException {
        String sql = "UPDATE " +tableName+ " SET " + columnName + " = " + value + " WHERE ID = " + ID;
        SQLConnection.queryUpdate(sql);
    }

    public static void delete(String tableName, String condition) throws DatabaseConnectionException{
        String sql = "DELETE FROM "+ tableName + " WHERE " + condition;
        SQLConnection.queryUpdate(sql);
    }

    public static ResultSet select(String tableName, String column, String where) throws DatabaseConnectionException {
        String sql = "SELECT " + column + " FROM "+ tableName + " WHERE " + where;
         return SQLConnection.queryExecute(sql);
    }

    private static synchronized ResultSet queryExecute(String sql) throws DatabaseConnectionException{
        try {
            // Establish connection
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL successfully!");
            PreparedStatement pstmt = conn.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery(sql);

            // Close connection
            pstmt.close();
            conn.close();
            return rs;
        } catch (SQLException e){
            throw new DatabaseConnectionException("Database connection error: " + e.getMessage());
        }
    }

    private static synchronized Integer queryUpdate(String sql) throws DatabaseConnectionException {
        try {
            // Establish connection
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL successfully!");
            PreparedStatement pstmt = conn.prepareStatement(sql);

            int rowsAffected = pstmt.executeUpdate();

            // Close connection
            pstmt.close();
            conn.close();

            if (rowsAffected > 0) {
                // Retrieve the generated ID
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
            return null;
        } catch (SQLException e){
            throw new DatabaseConnectionException("Database connection error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to MySQL successfully!");

            String sql = "INSERT INTO accounts (username, password, email, projectList) VALUES (?, 'test', ?, '{}')";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "myName");
                pstmt.setString(2, "myEmail5");
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Rows inserted: " + rowsAffected);
            }
            System.out.println("INSERTED");

            sql = "SELECT * FROM accounts";
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getString("username"));
                }
            }
            System.out.println("SELECTED");

            sql = "DELETE FROM accounts WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "myName");
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Rows deleted: " + rowsAffected);
            }
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}