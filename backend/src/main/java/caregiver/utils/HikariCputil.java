package caregiver.utils;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCputil {

    // 唯一的資料來源實體（Singleton）
    private static HikariDataSource hikariDataSource;

    static {
        try {
            System.out.println("=== 開始初始化 HikariCP (SanatoriumProject) ===");
            
            // 在類別載入時初始化
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // 修改為 SanatoriumProject 的資料庫
            config.setJdbcUrl("jdbc:sqlserver://localhost:1433;DatabaseName=caregiver;encrypt=false;trustServerCertificate=true");
            config.setUsername("Layfon");
            config.setPassword("123456");

            // 其他可選設定（連線池參數）
            config.setMaximumPoolSize(10);       // 同時最多10個連線
            config.setMinimumIdle(2);            // 最少保留2個空閒連線
            config.setConnectionTimeout(30000);  // 連線超時 30 秒
            config.setIdleTimeout(600000);       // 空閒超過10分鐘就釋放連線
            config.setMaxLifetime(1800000);      // 最長存活30分鐘
            
            // 連線測試查詢
            config.setConnectionTestQuery("SELECT 1");

            hikariDataSource = new HikariDataSource(config);
            
            System.out.println("HikariCP 連線池初始化成功 - 資料庫: sanatorium");
            System.out.println("JDBC URL: " + config.getJdbcUrl());
            System.out.println("使用者: " + config.getUsername());
            
            // 測試連線
            testConnection();
            
        } catch (Exception e) {
            System.err.println("HikariCP 初始化失敗: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("資料庫連線池初始化失敗", e);
        }
    }

    // 對外提供資料來源
    public static DataSource getDataSource() {
        if (hikariDataSource == null || hikariDataSource.isClosed()) {
            throw new RuntimeException("資料庫連線池未初始化或已關閉");
        }
        return hikariDataSource;
    }

    // 關閉連線池（程式結束前可呼叫）
    public static void closeDataSource() {
        if (hikariDataSource != null && !hikariDataSource.isClosed()) {
            hikariDataSource.close();
            System.out.println("HikariCP 連線池已關閉");
        }
    }
    
    // 測試資料庫連線
    public static boolean testConnection() {
        System.out.println("=== 測試資料庫連線 (sanatorium) ===");
        try {
            var connection = getDataSource().getConnection();
            System.out.println("成功獲取資料庫連線: " + connection);
            
            var stmt = connection.createStatement();
            var rs = stmt.executeQuery("SELECT 1 as test");
            boolean hasResult = rs.next();
            
            if (hasResult) {
                System.out.println("連線測試成功，查詢結果: " + rs.getInt("test"));
            }
            
            rs.close();
            stmt.close();
            connection.close();
            
            System.out.println("資料庫連線測試: " + (hasResult ? "成功" : "失敗"));
            return hasResult;
        } catch (Exception e) {
            System.err.println("資料庫連線測試失敗: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // 測試照服員表格是否存在
    public static boolean testCaregiverTable() {
        System.out.println("=== 測試照服員表格 (sanatorium 資料庫) ===");
        try {
            var connection = getDataSource().getConnection();
            var stmt = connection.createStatement();
            var rs = stmt.executeQuery("SELECT COUNT(*) as count FROM caregivers");
            
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("照服員表格存在，目前資料筆數: " + count);
                rs.close();
                stmt.close();
                connection.close();
                return true;
            }
            
            rs.close();
            stmt.close();
            connection.close();
            return false;
        } catch (Exception e) {
            System.err.println("照服員表格測試失敗: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}