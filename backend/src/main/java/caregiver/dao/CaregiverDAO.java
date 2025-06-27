package caregiver.dao;

import java.sql.Connection;



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import caregiver.bean.Caregiver;
import utils.HikariCputil;

public class CaregiverDAO {

	// 查詢全部照服員資料
	public List<Caregiver> getAllCaregivers() throws SQLException {
		List<Caregiver> caregivers = new ArrayList<>();
		String sql = "SELECT * FROM caregivers";
		
		try (Connection conn = HikariCputil.getDataSource().getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			
			while (rs.next()) {
				Caregiver caregiver = new Caregiver(
						rs.getInt("caregiver_Id"),
						rs.getString("chineseName"),
						rs.getBoolean("gender"),
						rs.getString("phone"),
						rs.getString("email"),
						rs.getInt("experience_years"),
						rs.getString("photo")
						);
				caregivers.add(caregiver);
			}
		}
		
		return caregivers;
	}
	// 模糊查詢照服員資料 by ChineseName
	public List<Caregiver> getCaregiversByName(String chineseName) throws SQLException {
	  final String sql = "SELECT * FROM caregivers WHERE chineseName LIKE ? ";
	  
	  try (Connection conn = HikariCputil.getDataSource().getConnection();
				 PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, "%" + chineseName + "%");
				
				List<Caregiver> users = new ArrayList<>();
					try(ResultSet rs = pstmt.executeQuery()) {
						while (rs.next()) {
							Caregiver userList = new Caregiver();
							userList.setCaregiverId(rs.getInt("caregiver_Id"));
							userList.setChineseName(rs.getString("chineseName"));
							userList.setGender(rs.getBoolean("gender"));
							userList.setPhone(rs.getString("phone"));
							userList.setEmail(rs.getString("email"));
							userList.setExperienceYears(rs.getInt("experience_years"));
							userList.setPhoto(rs.getString("photo"));
							users.add(userList);
						}
						return users;
				}
	  }
	}
	
	// 查詢照服員資料 by caregiverId
	public Caregiver getCaregiverById(int caregiverId) throws SQLException {
		String sql = "SELECT * FROM caregivers WHERE caregiver_Id = ?";
		try (Connection conn = HikariCputil.getDataSource().getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, caregiverId);
			var rs = pstmt.executeQuery();
			if (rs.next()) {
				return new Caregiver(
						rs.getInt("caregiver_Id"), 
						rs.getString("chineseName"), 
						rs.getBoolean("gender"),
						rs.getString("phone"), 
						rs.getString("email"), 
						rs.getInt("experience_years"),
						rs.getString("photo"));
			} else {
				return null;
			}
		}
	}
	


	// 新增照服員資料
	public boolean addCaregiver(Caregiver caregiver) throws SQLException {
		String sql = "INSERT INTO caregivers (chineseName, gender, phone, email, experience_years, photo) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection conn = HikariCputil.getDataSource().getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, caregiver.getChineseName());
			pstmt.setBoolean(2, caregiver.getGender());
			pstmt.setString(3, caregiver.getPhone());
			pstmt.setString(4, caregiver.getEmail());
			pstmt.setInt(5, caregiver.getExperienceYears());
			pstmt.setString(6, caregiver.getPhoto());
			return pstmt.executeUpdate() > 0;
		}catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	}
	}

	// 依照照服員編號修改照服員
	public boolean updateCaregiver(Caregiver caregiver) throws SQLException {
		String sql = "UPDATE caregivers SET chineseName=?, gender=?, "
				+ "phone=?, email=?, experience_years=?, photo=? "
				+ "WHERE caregiver_Id=?";

		try (Connection conn = HikariCputil.getDataSource().getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, caregiver.getChineseName());
			pstmt.setBoolean(2, caregiver.getGender());
			pstmt.setString(3, caregiver.getPhone());
			pstmt.setString(4, caregiver.getEmail());
			pstmt.setInt(5, caregiver.getExperienceYears());
			pstmt.setString(6, caregiver.getPhoto());
			pstmt.setInt(7, caregiver.getCaregiverId());
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	}
	}
	//依照照服員編號刪除照服員
	public boolean deleteCaregiverById(int caregiverId) throws SQLException {
	    String sql = "DELETE FROM caregivers WHERE caregiver_Id = ?";

	    try (Connection conn = HikariCputil.getDataSource().getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setInt(1, caregiverId);
	        return pstmt.executeUpdate() > 0; // 若成功刪除回傳 true
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    }
	}
	

}
