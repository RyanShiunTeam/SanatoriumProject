package device.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import device.dao.model.DeviceCategory;

public class DeviceCategoryDAO {

    private Connection conn;

    // 建構子：注入資料庫連線
    public DeviceCategoryDAO(Connection conn) {
        this.conn = conn;
    }

    // 新增一筆輔具分類資料
    public void insert(DeviceCategory category) throws SQLException {
        String sql = "INSERT INTO DeviceCategory (id, name, position) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getId());
            stmt.setString(2, category.getName());
            stmt.setInt(3, category.getPosition());
            stmt.executeUpdate();
        }
    }

    // 查詢所有輔具分類資料
    public List<DeviceCategory> findAll() throws SQLException {
        List<DeviceCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM DeviceCategory ORDER BY position ASC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                DeviceCategory category = new DeviceCategory();
                category.setId(rs.getString("id"));
                category.setName(rs.getString("name"));
                category.setPosition(rs.getInt("position"));
                list.add(category);
            }
        }
        return list;
    }

    // 以ID查詢輔具分類資料（主鍵查詢）
    public DeviceCategory findById(String id) throws SQLException {
        String sql = "SELECT * FROM DeviceCategory WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                DeviceCategory category = new DeviceCategory();
                category.setId(rs.getString("id"));
                category.setName(rs.getString("name"));
                category.setPosition(rs.getInt("position"));
                return category;
            }
        }
        return null;
    }

    // 依名稱模糊查詢分類資料 (LIKE %name%)
    public List<DeviceCategory> findByName(String namePattern) throws SQLException {
        List<DeviceCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM DeviceCategory WHERE name LIKE ? ORDER BY position ASC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + namePattern + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DeviceCategory category = new DeviceCategory();
                category.setId(rs.getString("id"));
                category.setName(rs.getString("name"));
                category.setPosition(rs.getInt("position"));
                list.add(category);
            }
        }
        return list;
    }

    // 更新輔具分類資料
    public void update(DeviceCategory category) throws SQLException {
        String sql = "UPDATE DeviceCategory SET name = ?, position = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getPosition());
            stmt.setString(3, category.getId());
            stmt.executeUpdate();
        }
    }

    // 刪除輔具分類資料（依ID）
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM DeviceCategory WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    // 判斷指定ID的資料是否存在
    public boolean existsById(String id) throws SQLException {
        String sql = "SELECT 1 FROM DeviceCategory WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    // 計算分類資料筆數
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM DeviceCategory";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
