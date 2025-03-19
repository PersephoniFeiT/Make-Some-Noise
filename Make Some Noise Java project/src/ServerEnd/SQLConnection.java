package ServerEnd;

import Exceptions.Accounts.DatabaseConnectionException;

import java.sql.*;

public class SQLConnection {
    // MySQL Connection URL
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/makeSomeNoiseDatabase";
    private static final String USER = "appUser";
    private static final String PASSWORD = "make some noise";

    public static int insert(String tableName, String[] values) throws DatabaseConnectionException {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName +  " VALUES (");
        for (String v : values){
            sql.append(v);
        }
        sql.append(")");

        try {
            Integer ID = SQLConnection.queryUpdate(sql.toString());
            if (ID == null)
                throw new DatabaseConnectionException("Database connection error: Could not insert.");
            else return ID.intValue();
        } catch (SQLException e){
            throw new DatabaseConnectionException("Database connection error: " + e.getMessage());
        }
    }

    public static void update(String tableName, int ID, String columnName, String value) throws DatabaseConnectionException {
        String sql = "UPDATE " +tableName+ " SET " + columnName + " = " + value + " WHERE ID = " + ID;
        try {
            SQLConnection.queryUpdate(sql);
        } catch (SQLException e){
            throw new DatabaseConnectionException("Database connection error: " + e.getMessage());
        }
    }

    public static void delete(String tableName, String condition) throws DatabaseConnectionException{
        String sql = "DELETE FROM "+ tableName + " WHERE " + condition;

        try {
            SQLConnection.queryUpdate(sql);
        } catch (SQLException e){
            throw new DatabaseConnectionException("Database connection error: " + e.getMessage());
        }
    }

    public static ResultSet select(String tableName, String column, String where) throws DatabaseConnectionException {
        String sql = "SELECT " + column + " FROM "+ tableName + " WHERE " + where;

        try {
            return SQLConnection.queryExecute(sql);
        } catch (SQLException e){
            throw new DatabaseConnectionException("Database connection error: " + e.getMessage());
        }
    }

    private static synchronized ResultSet queryExecute(String sql) throws SQLException{
        // Establish connection
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Connected to MySQL successfully!");
        PreparedStatement pstmt = conn.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery(sql);

        // Close connection
        pstmt.close();
        conn.close();
        return rs;
    }

    private static synchronized Integer queryUpdate(String sql) throws SQLException{
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
    }

    public static void main(String[] args) {
        try {
            // Establish connection
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL successfully!");

            String sql = "INSERT INTO accounts (name, email) VALUES (?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "myName");
            pstmt.setString(2, "myEmail5");
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("number of rows affected" + rowsAffected);

            sql = "SELECT * FROM accounts";
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery("SELECT * FROM accounts");
            System.out.println("rs: " + rs);
            while (rs.next()) {
                System.out.println(rs.getString("name"));
            }

            sql = "DELETE FROM accounts WHERE name = 'myName'";
            pstmt = conn.prepareStatement(sql);
            rowsAffected = pstmt.executeUpdate();
            System.out.println("number of rows affected:" + rowsAffected);

            // Close connection
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}