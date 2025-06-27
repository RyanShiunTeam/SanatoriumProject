package roomType.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import roomType.model.RoomType;

import javax.sql.DataSource;
import utils.HikariCputil;

public class RoomTypeDao {

    private DataSource dataSource;

    public RoomTypeDao() {
        this.dataSource = HikariCputil.getDataSource();
    }

    // 新增房型
    public void insertRoomType(RoomType room) throws SQLException {
        String sql = "INSERT INTO RoomType (name, price, capacity, description, special_features, image_url) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getName());
            ps.setInt(2, room.getPrice());
            ps.setDouble(3, room.getCapacity());
            ps.setString(4, room.getDescription());
            ps.setString(5, room.getSpecialFeatures());
            ps.setString(6, room.getImageUrl());
            ps.executeUpdate();
        }
    }

    // 查詢全部房型
    public List<RoomType> getAllRoomTypes() throws SQLException {
        List<RoomType> list = new ArrayList<>();
        String sql = "SELECT * FROM RoomType ORDER BY id";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RoomType room = new RoomType();
                room.setId(rs.getInt("id"));
                room.setName(rs.getString("name"));
                room.setPrice(rs.getInt("price"));
                room.setCapacity(rs.getInt("capacity"));
                room.setDescription(rs.getString("description"));
                room.setSpecialFeatures(rs.getString("special_features")); 
                room.setImageUrl(rs.getString("image_url"));
                list.add(room);
            }
        }
        return list;
    }

    // 根據 ID 查詢房型
    public RoomType getRoomTypeById(int id) throws SQLException {
        String sql = "SELECT * FROM RoomType WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RoomType room = new RoomType();
                    room.setId(rs.getInt("id"));
                    room.setName(rs.getString("name"));
                    room.setPrice(rs.getInt("price"));
                    room.setCapacity(rs.getInt("capacity"));
                    room.setDescription(rs.getString("description"));
                    room.setSpecialFeatures(rs.getString("special_features")); 
                    room.setImageUrl(rs.getString("image_url"));
                    return room;
                }
            }
        }
        return null;
    }
    
    public List<RoomType> getRoomTypesByCapacity(int capacity) throws SQLException {
        List<RoomType> list = new ArrayList<>();
        String sql = "SELECT * FROM RoomType WHERE capacity = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, capacity);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RoomType room = new RoomType();
                    room.setId(rs.getInt("id"));
                    room.setName(rs.getString("name"));
                    room.setPrice(rs.getInt("price"));
                    room.setCapacity(rs.getInt("capacity"));
                    room.setDescription(rs.getString("description"));
                    room.setSpecialFeatures(rs.getString("special_features"));
                    room.setImageUrl(rs.getString("image_url"));
                    list.add(room);
                }
            }
        }
        return list;
    }

    // 更新房型資料
    public boolean updateRoomType(RoomType room) throws SQLException {
        String sql = "UPDATE RoomType SET name=?, price=?, capacity=?, description=?, special_features=?, image_url=? WHERE id=?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getName());
            ps.setInt(2, room.getPrice());
            ps.setDouble(3, room.getCapacity());
            ps.setString(4, room.getDescription());
            ps.setString(5, room.getSpecialFeatures()); 
            ps.setString(6, room.getImageUrl());
            ps.setInt(7, room.getId());
            ps.executeUpdate();
        }
		return false;
    }

    // 刪除房型
    public boolean deleteRoomType(int id) {
        String sql = "DELETE FROM RoomType WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 查詢價格區間房型
    public List<RoomType> getRoomTypesByPriceRange(int minPrice, int maxPrice) throws SQLException {
        List<RoomType> list = new ArrayList<>();
        String sql = "SELECT * FROM RoomType WHERE price BETWEEN ? AND ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, minPrice);
            ps.setInt(2, maxPrice);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RoomType room = new RoomType();
                    room.setId(rs.getInt("id"));
                    room.setName(rs.getString("name"));
                    room.setPrice(rs.getInt("price"));
                    room.setCapacity(rs.getInt("capacity"));
                    room.setDescription(rs.getString("description"));
                    room.setSpecialFeatures(rs.getString("special_features")); 
                    room.setImageUrl(rs.getString("image_url"));
                    list.add(room);
                }
            }
        }
        return list;
    }

    // 模糊查詢描述
    public List<RoomType> getRoomTypesByDescriptionKeyword(String keyword) throws SQLException {
        List<RoomType> list = new ArrayList<>();
        String sql = "SELECT * FROM RoomType WHERE description LIKE ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RoomType room = new RoomType();
                    room.setId(rs.getInt("id"));
                    room.setName(rs.getString("name"));
                    room.setPrice(rs.getInt("price"));
                    room.setCapacity(rs.getInt("capacity"));
                    room.setDescription(rs.getString("description"));
                    room.setSpecialFeatures(rs.getString("special_features")); 
                    room.setImageUrl(rs.getString("image_url"));
                    list.add(room);
                }
            }
        }
        return list;
    }
}