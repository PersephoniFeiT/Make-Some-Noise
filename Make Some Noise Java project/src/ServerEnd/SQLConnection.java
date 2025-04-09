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
            return -1;

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database connection error in insert: " + e.getMessage());
        }
    }

    public static void update(String tableName, int id, String columnName, String value) throws DatabaseConnectionException {
        String sql = "UPDATE " + tableName + " SET " + columnName + " = ? WHERE ID = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, value);
            pstmt.setInt(2, id);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database connection error in update: " + e.getMessage());
        }
    }

    public static void delete(String tableName, String conditionColumn, String conditionValue) throws DatabaseConnectionException {
        String sql = "DELETE FROM " + tableName + " WHERE " + conditionColumn + " = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, conditionValue);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database connection error in delete: " + e.getMessage());
        }
    }

    public static ResultSet select(String tableName, String column, String[] conditionColumn, String[] conditionValue, String[] groupBy) throws DatabaseConnectionException {
        if (conditionColumn.length != conditionValue.length) {
            throw new IllegalArgumentException("Condition columns and values must have the same length.");
        }

        // Start building SQL query
        StringBuilder sqlBuilder = new StringBuilder("SELECT " + column + " FROM " + tableName);

        // WHERE clause
        if (conditionColumn.length > 0) {
            sqlBuilder.append(" WHERE ");
            for (int i = 0; i < conditionColumn.length; i++) {
                sqlBuilder.append(conditionColumn[i]).append(" = ?");
                if (i < conditionColumn.length - 1) {
                    sqlBuilder.append(" AND ");
                }
            }
        }

        // GROUP BY clause
        if (groupBy != null && groupBy.length > 0) {
            sqlBuilder.append(" GROUP BY ");
            for (int i = 0; i < groupBy.length; i++) {
                sqlBuilder.append(groupBy[i]);
                if (i < groupBy.length - 1) {
                    sqlBuilder.append(", ");
                }
            }
        }

        String sql = sqlBuilder.toString();

        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Set condition values
            for (int i = 0; i < conditionValue.length; i++) {
                if (isInteger(conditionValue[i])) pstmt.setInt(i + 1, Integer.parseInt(conditionValue[i]));
                else pstmt.setString(i + 1, conditionValue[i]);
            }

            return pstmt.executeQuery(); // Caller must close ResultSet & Connection

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database connection error in select: " + e.getMessage());
        }
    }

    private static boolean isInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        NoiseLayer n = new PerlinNoiseLayer();
        System.out.println(n.getClass());
    }
}