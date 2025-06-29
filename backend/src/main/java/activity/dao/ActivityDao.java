package activity.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import activity.bean.Activity;
import utils.HikariCputil;

public class ActivityDao {

    // 新增活動，並回傳自動產生的 ID
    public boolean insert(Activity act) {
        String sql = "INSERT INTO Activity(name, category, limit, date, time, location, instructor, status, description) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = HikariCputil.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        	stmt.setString(1, act.getName());
            stmt.setString(2, act.getCategory());
            stmt.setInt(3, act.getLimit());
            stmt.setString(4, act.getDate());
            stmt.setString(5, act.getTime());
            stmt.setString(6, act.getLocation());
            stmt.setString(7, act.getInstructor());
            stmt.setBoolean(8, act.isStatus());
            stmt.setString(9, act.getDescription());
            
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 模糊查詢活動
    public List<Activity> findActivityByName(String name) {
        final String sql = "SELECT * FROM Activity WHERE name LIKE ?";

        try (Connection conn = HikariCputil.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
        		stmt.setString(1, "%" + name + "%"); 
        	List<Activity> list = new ArrayList<>();
        	try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                Activity act = new Activity();
	                act.setId(rs.getInt("id"));
	                act.setName(rs.getString("name"));
	                act.setCategory(rs.getString("category"));
	                act.setLimit(rs.getInt("limit"));
	                act.setDate(rs.getString("date"));
	                act.setTime(rs.getString("time"));
	                act.setLocation(rs.getString("location"));
	                act.setInstructor(rs.getString("instructor"));
	                act.setStatus(rs.getBoolean("status"));
	                act.setDescription(rs.getString("description"));
	                list.add(act);
	            }
        	}
        	return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null; 
        }     
    }
    // 查詢全部活動
    public List<Activity> findAll() {
        final String sql = "SELECT * FROM Activity";

        try (Connection conn = HikariCputil.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
        	
        	List<Activity> list = new ArrayList<>();
        	try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                Activity act = new Activity();
	                act.setId(rs.getInt("id"));
	                act.setName(rs.getString("name"));
	                act.setCategory(rs.getString("category"));
	                act.setLimit(rs.getInt("limit"));
	                act.setDate(rs.getString("date"));
	                act.setTime(rs.getString("time"));
	                act.setLocation(rs.getString("location"));
	                act.setInstructor(rs.getString("instructor"));
	                act.setStatus(rs.getBoolean("status"));
	                act.setDescription(rs.getString("description"));
	                list.add(act);
	            }
        	}
        	return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null; 
        }     
    }

    // 修改活動
    public boolean update(Activity act) {
    	String sql = "UPDATE Activity SET name=?, category=?, limit=?, date=?, time=?, location=?, instructor=?, status=?, description=? " +
                "WHERE id=?";

        try (Connection conn = HikariCputil.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

        	stmt.setString(1, act.getName());
            stmt.setString(2, act.getCategory());
            stmt.setInt(3, act.getLimit());
            stmt.setString(4, act.getDate());
            stmt.setString(5, act.getTime());
            stmt.setString(6, act.getLocation());
            stmt.setString(7, act.getInstructor());
            stmt.setBoolean(8, act.isStatus());
            stmt.setString(9, act.getDescription());
            stmt.setInt(10, act.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 刪除活動
    public boolean delete(int id) {
    	String sql = "DELETE FROM Activity WHERE id = ?";

        try (Connection conn = HikariCputil.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

        	stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;          
        } catch (SQLException e) {
             e.printStackTrace();
             return false;
        }
    }
    
 // 模糊查詢活動名稱
    public List<Activity> queryByName(String name) {
        List<Activity> list = new ArrayList<>();
        String sql = "SELECT * FROM Activity WHERE name LIKE ?";

        try (Connection conn = HikariCputil.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
            	Activity act = new Activity();
                act.setId(rs.getInt("id"));
                act.setName(rs.getString("name"));
                act.setCategory(rs.getString("category"));
                act.setLimit(rs.getInt("limit"));
                act.setDate(rs.getString("date"));  
                act.setTime(rs.getString("time"));
                act.setLocation(rs.getString("location"));
                act.setInstructor(rs.getString("instructor"));
                act.setStatus(rs.getBoolean("status"));
                act.setDescription(rs.getString("description"));
                list.add(act);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
