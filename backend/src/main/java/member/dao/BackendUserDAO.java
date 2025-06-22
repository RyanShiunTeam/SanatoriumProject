package member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import member.bean.BackendUser;
import utils.HikariCputil;

public class BackendUserDAO {
	
	// 查詢員工資料 by userId
	public BackendUser getBackendUserById(int userId) throws SQLException {
		String sql = "SELECT * FROM BackendUser WHERE user_ID = ? ";
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			var rs = pstmt.executeQuery();
			if (rs.next()) {
				return new BackendUser(
					rs.getInt("user_ID"),
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
	
	
	// 增加後臺員工
	public boolean addBackendUser(BackendUser user) throws SQLException {
		final String sql = "INSERT INTO BackendUser (user_name, password, email) VALUES (?, ?, ?)";
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassWord());
			pstmt.setString(3, user.getEmail());
			return pstmt.executeUpdate() > 0;
		} 
	}
	
	
	// 停權員工 by userId 
	public boolean disableUser(int userId) throws SQLException {
		final String sql = "UPDATE BackendUser SET is_active = 0 WHERE user_ID = ?";
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			return pstmt.executeUpdate() > 0;
		} 
	}
	
	// 啟用員工 by userId
	public boolean enableUser(int userId) throws SQLException {
		final String sql = "UPDATE BackendUser SET is_active = 1 WHERE user_ID = ?";
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			return pstmt.executeUpdate() > 0;
		} 
	}
	
	// 依照名稱更新員工資訊
	public boolean updateBackendUser(BackendUser user) throws SQLException {
		final String sql = "UPDATE BackendUser SET user_name = ?, password = ?, email = ?, role = ? "
				+ "WHERE user_ID = ?";
		
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassWord());
			pstmt.setString(3, user.getEmail());
			pstmt.setString(4, user.getRole());
			pstmt.setInt(5, user.getUserId());
			return pstmt.executeUpdate() > 0;
		} 
	}
	
	// 依照名稱模糊查詢員工
	public List<BackendUser> getBackendUserByName(String userName) throws SQLException {
		final String sql = "SELECT * FROM BackendUser WHERE user_name LIKE ? AND is_active = 1";
		
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "%" + userName + "%");
			
			List<BackendUser> users = new ArrayList<>();
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					BackendUser userList = new BackendUser();
					userList.setUserId(rs.getInt("user_ID"));
					userList.setUserName(rs.getString("user_name"));
					userList.setPassWord(rs.getString("password"));
					userList.setEmail(rs.getString("email"));
					userList.setRole(rs.getString("role"));
					userList.setActive(rs.getBoolean("is_active"));
					userList.setCreatedAt(rs.getDate("createdAt").toLocalDate());
					users.add(userList);
				}
				return users;
			}

		} 
	}
	
	// 查詢所有職等
	public List<String> getAllRoles() throws SQLException {
		String sql = "SELECT DISTINCT role FROM BackendUser";
		List<String> roles = new ArrayList<>();
		
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				roles.add(rs.getString("role"));
			}
		} 
		return roles;
	}
	
	// 查詢停權名單 (is_active = 0)	
	public List<BackendUser> getDisabledUsers() throws SQLException {
		String sql = "SELECT * FROM BackendUser WHERE is_active = 0";
		List<BackendUser> disabledUsers = new ArrayList<>();
		
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				BackendUser user = new BackendUser();
				user.setUserId(rs.getInt("user_ID"));
				user.setUserName(rs.getString("user_name"));
				user.setPassWord(rs.getString("password"));
				user.setEmail(rs.getString("email"));
				user.setRole(rs.getString("role"));
				user.setActive(rs.getBoolean("is_active"));
				user.setUpdatedAt(rs.getDate("updatedAt").toLocalDate());
				disabledUsers.add(user);
			}
		} 
		return disabledUsers;
	}
}
