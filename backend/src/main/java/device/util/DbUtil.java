package device.util;

import java.sql.Connection;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;

public class DbUtil {

  private static DataSource dataSource;
    static {
        try {
            Context context = new InitialContext();
            dataSource = (DataSource) context.lookup("java:comp/env/jdbc/serdb");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("資料來源初始化失敗", e);
        }
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("取得資料庫連線失敗", e);
        }
    }
}