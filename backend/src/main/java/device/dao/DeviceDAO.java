package device.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import device.dao.model.Device;
import device.dao.model.DeviceCategory;

public class DeviceDAO {

    private Connection conn;

    // 建構子：注入資料庫連線
    public DeviceDAO(Connection conn) {
        this.conn = conn;
    }

    // 新增一筆輔具資料
    public void insert(Device device) throws SQLException {
        String sql = "INSERT INTO Device (id, name, sku, unitPrice, inventory, description, image, isOnline, category_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, device.getId());
            stmt.setString(2, device.getName());
            stmt.setString(3, device.getSku());
            stmt.setBigDecimal(4, device.getUnitPrice());
            stmt.setInt(5, device.getInventory());
            stmt.setString(6, device.getDescription());
            stmt.setString(7, device.getImage());
            stmt.setBoolean(8, device.isOnline());
            stmt.setString(9, device.getCategory() != null ? device.getCategory().getId() : null);
            stmt.executeUpdate();
        }
    }

    // 查詢所有輔具資料 (同時取得分類名稱，用 JOIN)
    public List<Device> findAll() throws SQLException {
        List<Device> list = new ArrayList<>();
        String sql = "SELECT d.*, c.name AS category_name, c.position AS category_position " +
                     "FROM Device d LEFT JOIN DeviceCategory c ON d.category_id = c.id ORDER BY d.name";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Device device = new Device();
                device.setId(rs.getString("id"));
                device.setName(rs.getString("name"));
                device.setSku(rs.getString("sku"));
                device.setUnitPrice(rs.getBigDecimal("unitPrice"));
                device.setInventory(rs.getInt("inventory"));
                device.setDescription(rs.getString("description"));
                device.setImage(rs.getString("image"));
                device.setOnline(rs.getBoolean("isOnline"));

                String catId = rs.getString("category_id");
                if (catId != null) {
                    DeviceCategory cat = new DeviceCategory();
                    cat.setId(catId);
                    cat.setName(rs.getString("category_name"));
                    cat.setPosition(rs.getInt("category_position"));
                    device.setCategory(cat);
                } else {
                    device.setCategory(null);
                }
                list.add(device);
            }
        }
        return list;
    }

    // 以ID查詢輔具資料（單筆）
    public Device findById(String id) throws SQLException {
        String sql = "SELECT d.*, c.name AS category_name, c.position AS category_position " +
                     "FROM Device d LEFT JOIN DeviceCategory c ON d.category_id = c.id WHERE d.id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Device device = new Device();
                device.setId(rs.getString("id"));
                device.setName(rs.getString("name"));
                device.setSku(rs.getString("sku"));
                device.setUnitPrice(rs.getBigDecimal("unitPrice"));
                device.setInventory(rs.getInt("inventory"));
                device.setDescription(rs.getString("description"));
                device.setImage(rs.getString("image"));
                device.setOnline(rs.getBoolean("isOnline"));

                String catId = rs.getString("category_id");
                if (catId != null) {
                    DeviceCategory cat = new DeviceCategory();
                    cat.setId(catId);
                    cat.setName(rs.getString("category_name"));
                    cat.setPosition(rs.getInt("category_position"));
                    device.setCategory(cat);
                }
                return device;
            }
        }
        return null;
    }

    // 依名稱模糊查詢輔具資料 (LIKE %name%)
    public List<Device> findByName(String namePattern) throws SQLException {
        List<Device> list = new ArrayList<>();
        String sql = "SELECT d.*, c.name AS category_name, c.position AS category_position " +
                     "FROM Device d LEFT JOIN DeviceCategory c ON d.category_id = c.id " +
                     "WHERE d.name LIKE ? ORDER BY d.name";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + namePattern + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Device device = new Device();
                device.setId(rs.getString("id"));
                device.setName(rs.getString("name"));
                device.setSku(rs.getString("sku"));
                device.setUnitPrice(rs.getBigDecimal("unitPrice"));
                device.setInventory(rs.getInt("inventory"));
                device.setDescription(rs.getString("description"));
                device.setImage(rs.getString("image"));
                device.setOnline(rs.getBoolean("isOnline"));

                String catId = rs.getString("category_id");
                if (catId != null) {
                    DeviceCategory cat = new DeviceCategory();
                    cat.setId(catId);
                    cat.setName(rs.getString("category_name"));
                    cat.setPosition(rs.getInt("category_position"));
                    device.setCategory(cat);
                }
                list.add(device);
            }
        }
        return list;
    }

    // 更新輔具資料
    public void update(Device device) throws SQLException {
        String sql = "UPDATE Device SET name=?, sku=?, unitPrice=?, inventory=?, description=?, image=?, isOnline=?, category_id=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, device.getName());
            stmt.setString(2, device.getSku());
            stmt.setBigDecimal(3, device.getUnitPrice());
            stmt.setInt(4, device.getInventory());
            stmt.setString(5, device.getDescription());
            stmt.setString(6, device.getImage());
            stmt.setBoolean(7, device.isOnline());
            stmt.setString(8, device.getCategory() != null ? device.getCategory().getId() : null);
            stmt.setString(9, device.getId());
            stmt.executeUpdate();
        }
    }

    // 刪除輔具資料（依ID）
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM Device WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    // 判斷指定ID的資料是否存在
    public boolean existsById(String id) throws SQLException {
        String sql = "SELECT 1 FROM Device WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    // 計算輔具資料筆數
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Device";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}

