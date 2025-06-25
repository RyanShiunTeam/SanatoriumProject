package caregiver.utils;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCputil {

    // 唯一的資料來源實體（Singleton）
    private static HikariDataSource hikariDataSource;

    static {
        // 在類別載入時初始化
        HikariConfig config = new HikariConfig();
        //config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        config.setJdbcUrl("jdbc:sqlserver://localhost:1433;DatabaseName=caregiver;encrypt=false");
        config.setUsername("Layfon");
        config.setPassword("123456");

        // 其他可選設定（連線池參數）
        config.setMaximumPoolSize(10);       // 同時最多10個連線
        config.setMinimumIdle(2);            // 最少保留2個空閒連線
        config.setIdleTimeout(600000);       // 空閒超過10分鐘就釋放連線
        config.setMaxLifetime(1800000);      // 最長存活30分鐘

        hikariDataSource = new HikariDataSource(config);
    }

    // 對外提供資料來源
    public static DataSource getDataSource() {
        return hikariDataSource;
    }

    // 關閉連線池（程式結束前可呼叫）
    public static void closeDataSource() {
        if (hikariDataSource != null && !hikariDataSource.isClosed()) {
            hikariDataSource.close();
        }
    }
}