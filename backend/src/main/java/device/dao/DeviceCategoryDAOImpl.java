package device.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import device.bean.DeviceCategory;
import device.util.DbUtil;

public class DeviceCategoryDAOImpl implements DeviceCategoryDAO {

	// 查詢所有分類，依 category_id 排序
    @Override
    public List<DeviceCategory> findAll() throws Exception {
        List<DeviceCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM DeviceCategory ORDER BY category_id";
        
     // 使用 try-with-resources 自動關閉連線與資源
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
     // 將每筆查詢結果封裝成 DeviceCategory 物件並加入 list
            while (rs.next()) {
                DeviceCategory cat = new DeviceCategory();
                cat.setId(rs.getInt("id"));
                cat.setName(rs.getString("name"));
                cat.setCategoryId(rs.getInt("category_id"));
                list.add(cat);
            }
        }

        return list;
    }

    // 根據主鍵 id 查詢單筆分類資料
    @Override
    public DeviceCategory findById(int id) throws Exception {
        String sql = "SELECT * FROM DeviceCategory WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DeviceCategory cat = new DeviceCategory();
                    cat.setId(rs.getInt("id"));
                    cat.setName(rs.getString("name"));
                    cat.setCategoryId(rs.getInt("category_id"));  // 將結果資料轉為物件屬性
                    return cat;
                }
            }
        }
        return null; // 查無資料時回傳 null
    }

 // 新增分類資料，並取得自動產生的主鍵
    @Override
    public void insert(DeviceCategory cat) throws Exception {
        String sql = "INSERT INTO DeviceCategory(name, category_id) VALUES (?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cat.getName());
            ps.setInt(2, cat.getCategoryId());  // 🔄 修改欄位名稱
            ps.executeUpdate();
            
            // 取得資料庫自動產生的 id 並設定回物件中
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cat.setId(rs.getInt(1));
                }
            }
        }
    }
    
    // 根據 ID 更新分類的名稱與 category_id
    @Override
    public void update(DeviceCategory cat) throws Exception {
        String sql = "UPDATE DeviceCategory SET name = ?, category_id = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cat.getName());
            ps.setInt(2, cat.getCategoryId());  
            ps.setInt(3, cat.getId());
            ps.executeUpdate();
        }
    }

 // 根據 ID 刪除分類資料，回傳是否成功
    @Override
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM DeviceCategory WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0; // 若有刪除資料則回傳 true
        }
    }
    
    // 檢查指定 ID 的分類是否存在
    @Override
    public boolean existsById(int id) throws Exception {
        String sql = "SELECT 1 FROM DeviceCategory WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    
 // 模糊搜尋分類名稱（使用 LIKE）並依 category_id 排序
    @Override
    public List<DeviceCategory> findByNameLike(String namePattern) throws Exception {
        List<DeviceCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM DeviceCategory WHERE name LIKE ? ORDER BY category_id";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + namePattern + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DeviceCategory cat = new DeviceCategory();
                    cat.setId(rs.getInt("id"));
                    cat.setName(rs.getString("name"));
                    cat.setCategoryId(rs.getInt("category_id"));  
                    list.add(cat);
                }
            }
        }
        return list;
    }
    
    // 查詢分類總筆數
    @Override
    public int count() throws Exception {
        String sql = "SELECT COUNT(*) FROM DeviceCategory";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);  // 回傳統計結果
            }
            return 0;
        }
    }
}
