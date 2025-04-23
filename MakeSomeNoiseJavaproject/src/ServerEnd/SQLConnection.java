package ServerEnd;

import BackEnd.Editor.NoiseLayer;
import BackEnd.Editor.PerlinNoiseLayer;
import Exceptions.DatabaseConnectionException;

import java.sql.*;
import java.util.*;

/** SQLConnection handles the basic MySQL connection and actions. This is the only script in the system that connects
 * directly to the server at any given point. Handles opening and closing connections, validating arguments, safe SQL
 * queries, insertions, deletions, etc, and stores the connection information. */
public class SQLConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/makesomenoise";
    private static final String USER = "appUser";
    private static final String PASSWORD = "make some noise";


    /** Insert creates a new row in the designated table.
     * @param tableName String name of the table to perform actions on
     * @param columns A string array of the column names to perform actions on.
     * @param values A string array of values to assign to each column, the same column order of progression.
     * @return an int ID value of the index at which the new row was created in the table.
     * @throws DatabaseConnectionException if there is an error when performing insert.
     * @throws IllegalArgumentException if there is an error in the format of the input -- for example, if there are more columns than values. */
    public static int insert(String tableName, String[] columns, Object[] values) throws DatabaseConnectionException {
        if (columns.length != values.length) {
            throw new IllegalArgumentException("Columns and values must have the same length.");
        }

        String columnNames = String.join(", ", columns);
        String placeholders = String.join(", ", Collections.nCopies(values.length, "?"));
        String sql = "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + placeholders + ")";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof String)
                    pstmt.setString(i + 1, (String)values[i]);
                else if (values[i] instanceof Integer)
                    pstmt.setInt(i + 1, (Integer) values[i]);
                else
                    pstmt.setObject(i + 1, values[i]); // fallback for other types
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

    /** Update modifies an existing row in the designated table.
     * @param tableName String name of the table to perform actions on
     * @param id The int ID of the index of the row to modify.
     * @param columnName The String name of the column to modify in that row.
     * @param value The String new value to be assigned to the specific entry in that table.
     * @throws DatabaseConnectionException if there is an error when performing update. */
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

    /** Update modifies an existing row in the designated table.
     * @param tableName String name of the table to perform actions on
     * @param id The int ID of the index of the row to modify.
     * @param columnName The String name of the column to modify in that row.
     * @param value The int new value to be assigned to the specific entry in that table.
     * @throws DatabaseConnectionException if there is an error when performing update. */
    public static void update(String tableName, int id, String columnName, int value) throws DatabaseConnectionException {
        String sql = "UPDATE " + tableName + " SET " + columnName + " = ? WHERE ID = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, value);
            pstmt.setInt(2, id);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database connection error in update: " + e.getMessage());
        }
    }

    /** Delete deletes an existing row in the designated table.
     * @param tableName String name of the table to perform actions on
     * @param conditionColumn The String column name that will be searched according to an equals condition
     * @param conditionValue The String value to try to match when searching in the conditionColumn.
     * @throws DatabaseConnectionException if there is an error when performing update. */
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

    /** Select searches the table and returns a List of rows that exactly match the given conditions.
     * @param tableName String name of the table to perform actions on
     * @param column The String column whose data we wish to return.
     * @param conditionColumn The String array of column names that will be searched according to an equals condition
     * @param conditionValue The String array of values to try to match when searching in the conditionColumn.
     * @param groupBy The String array of column names to group the results by and sort.
     * @return A List of Maps with a key/value pair of <String, Object>, where the String is the column name and the Object is the value of the entry for that row, column
     * @throws DatabaseConnectionException if there is an error when performing update.
     * @throws IllegalArgumentException if there is an error in the format of the input -- for example, if there are more columns than values.*/
    public static List<Map<String, Object>> select(String tableName, String column, String[] conditionColumn, Object[] conditionValue, String[] groupBy) throws DatabaseConnectionException {
        if (conditionColumn.length != conditionValue.length) {
            throw new IllegalArgumentException("Condition columns and values must have the same length.");
        }

        List<Map<String, Object>> resultList = new ArrayList<>();

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

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < conditionValue.length; i++) {
                if (conditionValue[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer)conditionValue[i]);
                } else if (conditionValue[i] instanceof String) {
                    pstmt.setString(i + 1, (String)conditionValue[i]);
                }else {
                    pstmt.setObject(i+1, conditionValue[i]);
                }
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(meta.getColumnLabel(i), rs.getObject(i));
                    }
                    resultList.add(row);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database connection error in select: " + e.getMessage());
        }

        return resultList;
    }

    /** Select searches the table and returns a List of rows that roughly match the given conditions ("LIKE" instead of "=")
     * @param tableName String name of the table to perform actions on
     * @param column The String column whose data we wish to return.
     * @param conditionColumn The String array of column names that will be searched according to an equals condition
     * @param conditionValue The String array of values to try to match when searching in the conditionColumn.
     * @param groupBy The String array of column names to group the results by and sort.
     * @return A List of Maps with a key/value pair of <String, Object>, where the String is the column name and the Object is the value of the entry for that row, column
     * @throws DatabaseConnectionException if there is an error when performing update.
     * @throws IllegalArgumentException if there is an error in the format of the input -- for example, if there are more columns than values.*/
    public static List<Map<String, Object>> selectLike(String tableName, String column, String[] conditionColumn, Object[] conditionValue, String[] groupBy) throws DatabaseConnectionException {
        if (conditionColumn.length != conditionValue.length) {
            throw new IllegalArgumentException("Condition columns and values must have the same length.");
        }

        List<Map<String, Object>> resultList = new ArrayList<>();

        // Start building SQL query
        StringBuilder sqlBuilder = new StringBuilder("SELECT " + column + " FROM " + tableName);

        // WHERE clause using LIKE for substring matching
        if (conditionColumn.length > 0) {
            sqlBuilder.append(" WHERE ");
            for (int i = 0; i < conditionColumn.length; i++) {
                sqlBuilder.append(conditionColumn[i]).append(" LIKE ?");
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

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < conditionValue.length; i++) {
                if (conditionValue[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer)conditionValue[i]);
                } else if (conditionValue[i] instanceof String) {
                    pstmt.setString(i + 1, "%"+(String)conditionValue[i]+"%");
                }else {
                    pstmt.setObject(i+1, conditionValue[i]);
                }
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(meta.getColumnLabel(i), rs.getObject(i));
                    }
                    resultList.add(row);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database connection error in select: " + e.getMessage());
        }
        return resultList;
    }


    /** Helper function to check if a string is an integer and filter out exception throws.
     * @param str String to check.
     * @return true if str can be parsed as an integer */
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

}