package member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import utils.HikariCputil;

public class BackendUserDAO {
	
	// 增加後臺使用者
	public void addBackendUser(BackendUser user) throws SQLException {
		String sql = "INSERT INTO BackendUser (user_name, password, email, role, is_active) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, user.userName());
			pstmt.setString(2, user.passWord());
			pstmt.setString(3, user.email());
			pstmt.setString(4, user.role());
			pstmt.setBoolean(5, user.isActive());
			pstmt.executeUpdate();
		} 
	}
	
	
	// 依照名稱停權後台使用者
	public void deleteBackendUser(String userName) throws SQLException {
		String sql = "UPDATE BackendUser SET is_active = 0 WHERE user_name = ?";
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, userName);
			pstmt.executeUpdate();
		} 
	}
	
	// 依照名稱更新後台使用者密碼
	public void updateBackendUser(String userName, String newPassword) throws SQLException {
		String sql = "UPDATE BackendUser SET password = ? WHERE user_name = ?";
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, newPassword);
			pstmt.setString(2, userName);
			pstmt.executeUpdate();
		} 
	}
	
	// 依照名稱查詢後台使用者
	public BackendUser getBackendUserByName(String userName) throws SQLException {
		String sql = "SELECT * FROM BackendUser WHERE user_name = ?";
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, userName);
			var rs = pstmt.executeQuery();
			if (rs.next()) {
				return new BackendUser(
					rs.getInt("user_id"),
					rs.getString("user_name"),
					rs.getString("password"),
					rs.getString("email"),
					rs.getString("role"),
					rs.getBoolean("is_active"),
					rs.getDate("createdAt").toLocalDate(),
					rs.getDate("updatedAt").toLocalDate()
				);
			} else {
				return null; 
			}
		} 
	}
}
