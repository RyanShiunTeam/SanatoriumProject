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
	public BackendUser getBackendUserById(int userID) throws SQLException {
		String sql = "SELECT * FROM BackendUser WHERE user_ID = ? ";
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userID);
			var rs = pstmt.executeQuery();
			if (rs.next()) {
				return new BackendUser(
					rs.getInt("user_ID"),
					rs.getString("user_name"),
					rs.getString("password"),
					rs.getString("email"),
					rs.getString("role"),
					rs.getBoolean("is_active"),
					rs.getDate("createdAt"),
					rs.getDate("updatedAt")
				);
			} else {
				return null; 
			}
		} 
	}
	
	// 查詢 email by userID
	public BackendUser getEmailById(int userID) throws SQLException {
		final String sql = "SELECT email FROM BackendUser WHERE user_ID = ?";
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userID);
			
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                	BackendUser userEmail = new BackendUser();
                	userEmail.setEmail(rs.getString("email"));
                	return userEmail;
	            } else {
	            	return null;
	            }
            }
		}
	}
	
	// 增加後臺員工
	public boolean addBackendUser(String userName, String password, String email) throws SQLException {
		final String sql = "INSERT INTO BackendUser (user_name, password, email) VALUES (?, ?, ?)";
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, userName);
			pstmt.setString(2, password);
			pstmt.setString(3, email);
			return pstmt.executeUpdate() > 0;
		} 
	}
	
	
	// 停權員工 by userId 
	public boolean disableUser(int userID) throws SQLException {
		final String sql = "UPDATE BackendUser SET is_active = 0, updatedAt = SYSUTCDATETIME() WHERE user_ID = ?";
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userID);
			return pstmt.executeUpdate() > 0;
		} 
	}
	
	// 啟用員工 by userId
	public boolean enableUser(int userID) throws SQLException {
		final String sql = "UPDATE BackendUser SET is_active = 1, updatedAt = SYSUTCDATETIME() WHERE user_ID = ?";
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userID);
			return pstmt.executeUpdate() > 0;
		} 
	}
	
	// 修改密碼 by ID
	public boolean updatePwd (int userID, String newPwd) throws SQLException {
		final String sql = "UPDATE BackendUser SET password = ? WHERE user_ID = ?";
		
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, newPwd);
			pstmt.setInt(2, userID);
			return pstmt.executeUpdate() > 0;
		}
	}
	
	// 更新員工資訊 by ID
	public boolean updateBackendUser(int userID, String userName, String email, String role) throws SQLException {
		final String sql = "UPDATE BackendUser SET user_name = ? , email = ?, role = ?, updatedAt = SYSUTCDATETIME() "
				+ "WHERE user_ID = ?";
		
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, userName);
			pstmt.setString(2, email);
			pstmt.setString(3, role);
			pstmt.setInt(4, userID);
			return pstmt.executeUpdate() > 0;
		} 
	}
	
	// 模糊查詢員工 by userName
	public List<BackendUser> getBackendUserByName(String userName) throws SQLException {
		final String sql = "SELECT * FROM BackendUser WHERE user_name LIKE ? AND is_active = 1";
		
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "%" + userName + "%");
			
			List<BackendUser> users = new ArrayList<>();
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					BackendUser userList = new BackendUser();
					userList.setuserID(rs.getInt("user_ID"));
					userList.setUserName(rs.getString("user_name"));
					userList.setPassWord(rs.getString("password"));
					userList.setEmail(rs.getString("email"));
					userList.setRole(rs.getString("role"));
					userList.setActive(rs.getBoolean("is_active"));
					userList.setCreatedAt(rs.getDate("createdAt"));
					users.add(userList);
				}
				return users;
			}
		} 
	}
	
	// 查詢所有員工
	public List<BackendUser> getAllBackendUser() throws SQLException {
		final String sql = "SELECT * FROM BackendUser WHERE is_active = 1";
		
		try (Connection conn = HikariCputil.getDataSource().getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			List<BackendUser> users = new ArrayList<>();
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					BackendUser userList = new BackendUser();
					userList.setuserID(rs.getInt("user_ID"));
					userList.setUserName(rs.getString("user_name"));
					userList.setPassWord(rs.getString("password"));
					userList.setEmail(rs.getString("email"));
					userList.setRole(rs.getString("role"));
					userList.setActive(rs.getBoolean("is_active"));
					userList.setCreatedAt(rs.getDate("createdAt"));
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
				user.setuserID(rs.getInt("user_ID"));
				user.setUserName(rs.getString("user_name"));
				user.setPassWord(rs.getString("password"));
				user.setEmail(rs.getString("email"));
				user.setRole(rs.getString("role"));
				user.setActive(rs.getBoolean("is_active"));
				user.setUpdatedAt(rs.getDate("updatedAt"));
				disabledUsers.add(user);
			}
		} 
		return disabledUsers;
	}
}
