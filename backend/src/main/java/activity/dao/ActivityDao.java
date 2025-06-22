package activity.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import activity.bean.Activity;
import utils.HikariCputil;

public class ActivityDao {

    // 新增活動，並回傳自動產生的 ID
    public int insert(Activity a) {
        String sql = "INSERT INTO Activity(name, category, limit, participant, date, time, location, instructor, status, registrationstart, registrationend, description) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = HikariCputil.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, a.getName());
            stmt.setString(2, a.getCategory());
            stmt.setInt(3, a.getLimit());
            stmt.setInt(4, a.getParticipant());
            stmt.setString(5, a.getDate());
            stmt.setString(6, a.getTime());
            stmt.setString(7, a.getLocation());
            stmt.setString(8, a.getInstructor());
            stmt.setString(9, a.getStatus());
            stmt.setString(10, a.getRegistrationStart());
            stmt.setString(11, a.getRegistrationEnd());
            stmt.setString(12, a.getDescription());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    System.out.println("新增成功，活動 ID 為: " + generatedId);
                }
            } else {
                System.out.println("新增失敗，未影響任何列。");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    // 查單一活動
    public Activity findById(int id) {
        String sql = "SELECT * FROM Activity WHERE id = ?";
        Activity a = null;

        try (Connection conn = HikariCputil.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                a = new Activity();
                a.setName(rs.getString("name"));
                a.setCategory(rs.getString("category"));
                a.setLimit(rs.getInt("limit"));
                a.setParticipant(rs.getInt("participant"));
                a.setDate(rs.getString("date"));
                a.setTime(rs.getString("time"));
                a.setLocation(rs.getString("location"));
                a.setInstructor(rs.getString("instructor"));
                a.setStatus(rs.getString("status"));
                a.setRegistrationStart(rs.getString("registrationstart"));
                a.setRegistrationEnd(rs.getString("registrationend"));
                a.setDescription(rs.getString("description"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return a;
    }
    // 查詢全部活動
    public List<Activity> findAll() {
        List<Activity> list = new ArrayList<>();
        String sql = "SELECT * FROM Activity";

        try (Connection conn = HikariCputil.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Activity a = new Activity();
                a.setName(rs.getString("name"));
                a.setCategory(rs.getString("category"));
                a.setLimit(rs.getInt("limit"));
                a.setParticipant(rs.getInt("participant"));
                a.setDate(rs.getString("date"));
                a.setTime(rs.getString("time"));
                a.setLocation(rs.getString("location"));
                a.setInstructor(rs.getString("instructor"));
                a.setStatus(rs.getString("status"));
                a.setRegistrationStart(rs.getString("registrationstart"));
                a.setRegistrationEnd(rs.getString("registrationend"));
                a.setDescription(rs.getString("description"));
                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // 依據name修改活動
    public boolean update(Activity a) {
        String sql = "UPDATE Activity SET category=?, limit=?, participant=?, date=?, time=?, location=?, instructor=?, status=?, registrationstart=?, registrationend=?, description=? " +
                     "WHERE name=?";

        try (Connection conn = HikariCputil.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, a.getCategory());
            stmt.setInt(2, a.getLimit());
            stmt.setInt(3, a.getParticipant());
            stmt.setString(4, a.getDate());
            stmt.setString(5, a.getTime());
            stmt.setString(6, a.getLocation());
            stmt.setString(7, a.getInstructor());
            stmt.setString(8, a.getStatus());
            stmt.setString(9, a.getRegistrationStart());
            stmt.setString(10, a.getRegistrationEnd());
            stmt.setString(11, a.getDescription());
            stmt.setString(12, a.getName());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 依據name刪除活動
    public boolean delete(String name) {
        String sql = "DELETE FROM Activity WHERE name = ?";

        try (Connection conn = HikariCputil.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            return stmt.executeUpdate() > 0;          
        } catch (SQLException e) {
             e.printStackTrace();
             return false;
        }
    }
}
