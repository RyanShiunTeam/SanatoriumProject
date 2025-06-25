package device.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import device.bean.Device;
import device.bean.DeviceCategory;
import device.util.DbUtil;

public class DeviceDAOImpl implements DeviceDAO {

	// 查詢所有設備，預設依名稱升冪排序
    @Override
    public List<Device> findAll() throws Exception {
        return findAllWithSort("name", "ASC");
    }

    // 根據設備 ID 查詢設備資料與分類資料（LEFT JOIN）
    @Override
    public Device findById(int id) throws Exception {
        String sql = "SELECT d.*, c.id AS c_id, c.name AS c_name, c.category_id AS c_category_id " +
                     "FROM Device d LEFT JOIN DeviceCategory c ON d.category_id = c.id WHERE d.id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToDeviceWithCategory(rs);
                }
            }
        }
        return null;
    }

    // 新增一筆設備資料，並取得資料庫自動產生的 ID
    @Override
    public void insert(Device device) throws Exception {
        String sql = "INSERT INTO Device(name, sku, unitPrice, inventory, description, image, isOnline, category_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, device.getName());
            ps.setString(2, device.getSku());
            ps.setBigDecimal(3, device.getUnitPrice());
            ps.setInt(4, device.getInventory());
            ps.setString(5, device.getDescription());
            ps.setString(6, device.getImage());
            ps.setBoolean(7, device.isOnline());
            ps.setInt(8, device.getCategoryId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    device.setId(rs.getInt(1));
                }
            }
        }
    }

    // 根據 ID 更新設備所有欄位資料
    @Override
    public void update(Device device) throws Exception {
        String sql = "UPDATE Device SET name = ?, sku = ?, unitPrice = ?, inventory = ?, description = ?, image = ?, isOnline = ?, category_id = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, device.getName());
            ps.setString(2, device.getSku());
            ps.setBigDecimal(3, device.getUnitPrice());
            ps.setInt(4, device.getInventory());
            ps.setString(5, device.getDescription());
            ps.setString(6, device.getImage());
            ps.setBoolean(7, device.isOnline());
            ps.setInt(8, device.getCategoryId());
            ps.setInt(9, device.getId());

            ps.executeUpdate();
        }
    }

    // 根據 ID 刪除設備，成功刪除回傳 true
    @Override
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM Device WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // 檢查某設備是否存在（用於預防異常操作）
    @Override
    public boolean existsById(int id) throws Exception {
        String sql = "SELECT 1 FROM Device WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    // 模糊查詢設備名稱，並帶入分類資料
    @Override
    public List<Device> findByName(String namePattern) throws Exception {
        List<Device> list = new ArrayList<>();
        String sql = "SELECT d.*, c.id AS c_id, c.name AS c_name, c.category_id AS c_category_id " +
                     "FROM Device d LEFT JOIN DeviceCategory c ON d.category_id = c.id " +
                     "WHERE d.name LIKE ? ORDER BY d.name";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + namePattern + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToDeviceWithCategory(rs));
                }
            }
        }
        return list;
    }
    
    // 計算設備總筆數
    @Override
    public int count() throws Exception {
        String sql = "SELECT COUNT(*) FROM Device";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // 查詢指定分類下的所有設備
    @Override
    public List<Device> findDevicesByCategoryId(int categoryId) throws Exception {
        List<Device> list = new ArrayList<>();
        String sql = "SELECT d.*, c.id AS c_id, c.name AS c_name, c.category_id AS c_category_id " +
                     "FROM Device d LEFT JOIN DeviceCategory c ON d.category_id = c.id " +
                     "WHERE d.category_id = ? ORDER BY d.name";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToDeviceWithCategory(rs));
                }
            }
        }
        return list;
    }

 // 查詢所有設備，並指定排序欄位與順序（ASC/DESC）
    @Override
    public List<Device> findAllWithSort(String sortBy, String order) throws Exception {
        List<Device> list = new ArrayList<>();

        String safeSort;
        switch (sortBy) {
            case "unitPrice":
                safeSort = "unitPrice";
                break;
            case "inventory":
                safeSort = "inventory";
                break;
            default:
                safeSort = "name";
        }

        String safeOrder = "asc".equalsIgnoreCase(order) ? "ASC" : "DESC";

        String sql = "SELECT d.*, c.id AS c_id, c.name AS c_name, c.category_id AS c_category_id " +
                     "FROM Device d LEFT JOIN DeviceCategory c ON d.category_id = c.id " +
                     "ORDER BY d." + safeSort + " " + safeOrder;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRowToDeviceWithCategory(rs));
            }
        }
        return list;
    }

 // 分頁查詢設備（搭配 OFFSET / FETCH）
    @Override
    public List<Device> findByPage(int offset, int limit) throws Exception {
        List<Device> list = new ArrayList<>();
        String sql = "SELECT d.*, c.id AS c_id, c.name AS c_name, c.category_id AS c_category_id " +
                     "FROM Device d LEFT JOIN DeviceCategory c ON d.category_id = c.id " +
                     "ORDER BY d.name OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, offset); // 從第幾筆開始
            ps.setInt(2, limit); // 一次幾筆資料

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToDeviceWithCategory(rs));
                }
            }
        }
        return list;
    }

 // 分頁查詢設備，並搭配排序欄位與順序
    @Override
    public List<Device> findByPageWithSort(int offset, int limit, String sortBy, String order) throws Exception {
        List<Device> list = new ArrayList<>();
     // 安全排序欄位
        String safeSort;
        switch (sortBy) {
            case "unitPrice":
                safeSort = "unitPrice";
                break;
            case "inventory":
                safeSort = "inventory";
                break;
            default:
                safeSort = "name";
        }

     // 安全排序方向
        String safeOrder = "asc".equalsIgnoreCase(order) ? "ASC" : "DESC";

        String sql = "SELECT d.*, c.id AS c_id, c.name AS c_name, c.category_id AS c_category_id " +
                     "FROM Device d LEFT JOIN DeviceCategory c ON d.category_id = c.id " +
                     "ORDER BY d." + safeSort + " " + safeOrder +
                     " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, offset);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToDeviceWithCategory(rs));
                }
            }
        }
        return list;
    }

 // 將查詢結果轉換為 Device 物件，並連帶分類資訊
    private Device mapRowToDeviceWithCategory(ResultSet rs) throws SQLException {
        Device device = new Device();
        device.setId(rs.getInt("id"));
        device.setName(rs.getString("name"));
        device.setSku(rs.getString("sku"));
        device.setUnitPrice(rs.getBigDecimal("unitPrice"));
        device.setInventory(rs.getInt("inventory"));
        device.setDescription(rs.getString("description"));
        device.setImage(rs.getString("image"));
        device.setOnline(rs.getBoolean("isOnline"));

     // 若分類不為 null，建立分類物件
        int cId = rs.getInt("c_id");
        if (!rs.wasNull()) {
            DeviceCategory category = new DeviceCategory();
            category.setId(cId);
            category.setName(rs.getString("c_name"));
            category.setCategoryId(rs.getInt("c_category_id"));
            device.setCategory(category);
        }

        return device;
    }
}
