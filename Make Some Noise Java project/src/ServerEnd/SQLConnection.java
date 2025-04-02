package ServerEnd;

import BackEnd.Editor.NoiseLayer;
import BackEnd.Editor.PerlinNoiseLayer;
import Exceptions.Accounts.DatabaseConnectionException;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.*;

public class SQLConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/makesomenoise";
    private static final String USER = "appUser";
    private static final String PASSWORD = "make some noise";

    public static int insert(String tableName, String[] columns, String[] values) throws DatabaseConnectionException {
        if (columns.length != values.length) {
            throw new IllegalArgumentException("Columns and values must have the same length.");
        }

        String columnNames = String.join(", ", columns);
        String placeholders = String.join(", ", Collections.nCopies(values.length, "?"));

        String sql = "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + placeholders + ")";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setString(i + 1, values[i]);
            }

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;  // No ID generated

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database connection error: " + e.getMessage());
        }
    }

    public static void update(String tableName, int ID, String columnName, String value) throws DatabaseConnectionException {
        String sql = "UPDATE " +tableName+ " SET " + columnName + " = '" + value + "' WHERE ID = " + ID;
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

    private static synchronized ResultSet queryExecute(String sql) throws DatabaseConnectionException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL successfully!");
            PreparedStatement pstmt = conn.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();

            pstmt.close(); // Close PreparedStatement
            conn.close();  // Close Connection
            return rs;

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database connection error: " + e.getMessage());
        }
    }

    private static synchronized Integer queryUpdate(String sql) throws DatabaseConnectionException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL successfully!");

            // Specify RETURN_GENERATED_KEYS
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            int rowsAffected = pstmt.executeUpdate();
            Integer generatedId = null;

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
                rs.close();  // Close ResultSet
            }

            pstmt.close(); // Close PreparedStatement
            conn.close();  // Close Connection

            return generatedId;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database connection error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        NoiseLayer n = new PerlinNoiseLayer();
        System.out.println(n.getClass());
    }
}