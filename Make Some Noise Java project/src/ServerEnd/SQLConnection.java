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

    public static void insert(String tableName, String[] values) throws DatabaseConnectionException {
        // Build the SQL query with correct syntax
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + "(username, password, email, projectList) VALUES (");
        for (int i = 0; i < values.length; i++) {
            sql.append("?");
            if (i < values.length - 1) sql.append(", ");
        }
        sql.append(")");  // Close the VALUES()

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setString(i + 1, values[i]);  // Correct way to set parameters
            }

            pstmt.executeUpdate();
            System.out.println("User inserted successfully.");
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database connection error: " + e.getMessage());
        }
    }

    public static void delete(String tableName, String condition) throws DatabaseConnectionException {
        String sql = "DELETE FROM " + tableName + " WHERE " + condition;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database connection error: " + e.getMessage());
        }
    }

    public static List<Map<String, Object>> select(String tableName, String columns, String condition)
            throws DatabaseConnectionException {
        String sql = "SELECT " + columns + " FROM " + tableName;
        if (condition != null && !condition.isEmpty()) {
            sql += " WHERE " + condition;
        }

        try {
            return queryExecute(sql);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database connection error: " + e.getMessage());
        }
    }

    private static synchronized List<Map<String, Object>> queryExecute(String sql) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    row.put(columnName, columnValue);
                }
                results.add(row);
            }
        }  // Auto-closes resources
            return results;
    }

    private static synchronized void queryUpdate(String sql) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();
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