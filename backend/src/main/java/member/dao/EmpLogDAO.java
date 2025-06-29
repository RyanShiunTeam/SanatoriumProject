package member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import member.bean.EmpLog;
import utils.HikariCputil;

public class EmpLogDAO {
	
	public void insert(EmpLog log) throws SQLException {
	    final String sql = "INSERT INTO emp_log (user_id, action, target_id) VALUES (?, ?, ?)";
	    try (Connection conn = HikariCputil.getDataSource().getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, log.getUserId());
	        ps.setString(2, log.getAction());
	        if (log.getTargetId() != null) {
	            ps.setInt(3, log.getTargetId());
	        } else {
	            ps.setNull(3, java.sql.Types.INTEGER);
	        }
	        ps.executeUpdate();
	        System.out.println("成功紀錄");
	    }
	}
	
	// 取得全部員工紀錄
	public List<EmpLog> getAllEmpLogs() throws SQLException {
	    final String sql = "SELECT * FROM emp_log";
	    try (Connection conn = HikariCputil.getDataSource().getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        List<EmpLog> empLogs = new ArrayList<>();
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                EmpLog log = new EmpLog();
	                log.setUserId(rs.getInt("user_id"));
	                log.setAction(rs.getString("action"));
	                // 處理可能為 NULL 的 target_id
	                log.setTargetId(
	                    rs.getObject("target_id") != null
	                        ? rs.getInt("target_id")
	                        : null
	                );
	                // 正確讀取 created_at → java.sql.Timestamp
	                log.setCreatedAt(rs.getTimestamp("created_at"));
	                
	                empLogs.add(log);
	            }
	        }
	        return empLogs;
	    }
	}
}
