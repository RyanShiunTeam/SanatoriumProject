package device.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import device.dao.DeviceCategoryDAO;
import device.dao.model.DeviceCategory;

public class DeviceCategoryService {

    private DeviceCategoryDAO dao;

    public DeviceCategoryService(Connection conn) {
        this.dao = new DeviceCategoryDAO(conn);
    }

    // 新增分類，檢查是否已存在
    public boolean addCategory(DeviceCategory category) throws SQLException {
        if (dao.existsById(category.getId())) {
            // 已存在不新增，商業邏輯決定回傳false或拋例外
            return false;
        }
        dao.insert(category);
        return true;
    }

    // 取得所有分類
    public List<DeviceCategory> getAllCategories() throws SQLException {
        return dao.findAll();
    }

    // 依ID取得分類，找不到回傳null
    public DeviceCategory getCategoryById(String id) throws SQLException {
        return dao.findById(id);
    }

    // 依名稱模糊搜尋分類
    public List<DeviceCategory> searchCategoriesByName(String namePattern) throws SQLException {
        return dao.findByName(namePattern);
    }

    // 更新分類，先確認存在
    public boolean updateCategory(DeviceCategory category) throws SQLException {
        if (!dao.existsById(category.getId())) {
            // 不存在無法更新
            return false;
        }
        dao.update(category);
        return true;
    }

    // 刪除分類，先確認存在
    public boolean deleteCategory(String id) throws SQLException {
        if (!dao.existsById(id)) {
            return false;
        }
        dao.delete(id);
        return true;
    }

    // 計算分類數量
    public int getCategoryCount() throws SQLException {
        return dao.count();
    }
}

