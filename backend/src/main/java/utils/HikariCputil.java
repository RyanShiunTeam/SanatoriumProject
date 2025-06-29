package utils;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCputil {
	
	private static HikariDataSource hikariDataSource;
	
	public static DataSource getDataSource() {
		HikariConfig config = new HikariConfig();
		// 設定連線池的基本參數 (記得改成自己的資料庫連線資訊)
		config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		config.setJdbcUrl("jdbc:sqlserver://localhost:1433;DatabaseName=Sanatorium;encrypt=false");
		config.setUsername("ryan");
		config.setPassword("9426");
		hikariDataSource = new HikariDataSource(config);
			
		return hikariDataSource;
	}
	
	public static void closeDataSource() {
		if (hikariDataSource != null && !hikariDataSource.isClosed()) {
			hikariDataSource.close();
		}
	}
}
