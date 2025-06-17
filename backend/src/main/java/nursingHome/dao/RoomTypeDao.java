package nursingHome.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import device.dao.model.DeviceCategory;
import nursingHome.dao.model.RoomType;

import java.util.ArrayList;
import java.util.List;

public class RoomTypeDao {
    private Connection conn;

    public RoomTypeDao(Connection conn) {
        this.conn = conn;
    }

    // 新增房型
    public void insertRoomType(RoomType room) throws SQLException {
        String sql = "INSERT INTO RoomType (name, price, area, description, image_url) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getName());
            ps.setInt(2, room.getPrice());
            ps.setDouble(3, room.getArea());
            ps.setString(4, room.getDescription());
            ps.setString(6, room.getImageUrl());
            ps.executeUpdate();
        }
    }

    // 查詢全部房型
    public List<RoomType> getAllRoomTypes() throws SQLException {
        List<RoomType> list = new ArrayList<>();
        String sql = "SELECT * FROM RoomType ORDER BY id";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RoomType room = new RoomType();
                room.setId(rs.getInt("id"));
                room.setName(rs.getString("name"));
                room.setPrice(rs.getInt("price"));
                room.setArea(rs.getDouble("area"));
                room.setDescription(rs.getString("description"));
                room.setImageUrl(rs.getString("image_url"));
                list.add(room);
            }
        }
        return list;
    }

    // 根據 ID 查詢房型
    public RoomType getRoomTypeById(int id) throws SQLException {
        String sql = "SELECT * FROM RoomType WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RoomType room = new RoomType();
                    room.setId(rs.getInt("id"));
                    room.setName(rs.getString("name"));
                    room.setPrice(rs.getInt("price"));
                    room.setArea(rs.getDouble("area"));
                    room.setDescription(rs.getString("description"));
                    room.setImageUrl(rs.getString("image_url"));
                    return room;
                }
            }
        }
        return null;
    }

    // 更新房型資料
    public void updateRoomType(RoomType room) throws SQLException {
        String sql = "UPDATE RoomType SET name=?, price=?, area=?, description=?, image_url=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getName());
            ps.setInt(2, room.getPrice());
            ps.setDouble(3, room.getArea());
            ps.setString(4, room.getDescription());
            ps.setString(6, room.getImageUrl());
            ps.setInt(7, room.getId());
            ps.executeUpdate();
        }
    }

    // 刪除房型
    public void deleteRoomType(int id) throws SQLException {
        String sql = "DELETE FROM RoomType WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        
        
    }
    public List<RoomType> getRoomTypesByPriceRange(int minPrice, int maxPrice) throws SQLException {
        List<RoomType> list = new ArrayList<>();
        String sql = "SELECT * FROM RoomType WHERE price BETWEEN ? AND ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, minPrice);
            ps.setInt(2, maxPrice);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RoomType room = new RoomType();
                    room.setId(rs.getInt("id"));
                    room.setName(rs.getString("name"));
                    room.setPrice(rs.getInt("price"));
                    room.setArea(rs.getDouble("area"));
                    room.setDescription(rs.getString("description"));
                    room.setImageUrl(rs.getString("image_url"));
                    list.add(room);
                }
            }
        }
        return list;
    }
    
    public List<RoomType> getRoomTypesByDescriptionKeyword(String keyword) throws SQLException {
        List<RoomType> list = new ArrayList<>();
        String sql = "SELECT * FROM RoomType WHERE description LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RoomType room = new RoomType();
                    room.setId(rs.getInt("id"));
                    room.setName(rs.getString("name"));
                    room.setPrice(rs.getInt("price"));
                    room.setArea(rs.getDouble("area"));
                    room.setDescription(rs.getString("description"));
                    room.setImageUrl(rs.getString("image_url"));
                    list.add(room);
                }
            }
        }
        return list;
    }


}
