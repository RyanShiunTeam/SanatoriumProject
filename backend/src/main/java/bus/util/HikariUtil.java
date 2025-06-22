package bus.util;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;



public class HikariUtil {
	
private static HikariDataSource hikariDataSource;
	
	public static DataSource getDataSource() {
		HikariConfig config = new HikariConfig();
		// 設定連線池的基本參數 (記得改成自己的資料庫連線資訊)
		config.setJdbcUrl("jdbc:sqlserver://localhost:1433;DatabaseName=rehabus;encrypt=false");
		config.setUsername("Elena");
		config.setPassword("860308");
		hikariDataSource = new HikariDataSource(config);
			
		return hikariDataSource;
	}
	
	private HikariUtil() {
		
	}
	public static void closeDataSource() {
		if (hikariDataSource != null && !hikariDataSource.isClosed()) {
			hikariDataSource.close();
		}
	}
}
