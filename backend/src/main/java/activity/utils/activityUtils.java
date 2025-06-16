package activity.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class activityUtils {
	

	
		private static final String URL = "jdbc:sqlserver://localhost:1433;DatabaseName=Sanatorium;encrypt=false";
	    private static final String USER = "gary";
	    private static final String PASSWORD = "123456";

	    public static Connection getConnection() {
	        try {
	            return DriverManager.getConnection(URL, USER, PASSWORD);
	        } catch (SQLException e) {
	            throw new RuntimeException("無法連線", e);
	        }
	    }

	    public static void closeResource(Connection conn, PreparedStatement stmt) {
	        try {
	            if (stmt != null) stmt.close();
	            if (conn != null) conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}


