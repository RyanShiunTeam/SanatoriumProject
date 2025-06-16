package nursingHome;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCutil {
    public static Connection getConnection() {
        Connection connection = null;
        InputStream inputStream = null;

        try {
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String url = "jdbc:sqlserver://localhost:1433;databaseName=NursingHomeDB;encrypt=false";
            String user = "grant";         // ← 這裡直接寫帳號
            String password = "123456";    // ← 這裡直接寫密碼

            connection = DriverManager.getConnection(url, user, password);
            boolean status = !connection.isClosed();
            System.out.println("連線狀態: " + status);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return connection;
    }

    public static void closeResource(Connection connection) {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResource(Connection connection, Statement statement) {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResource(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
