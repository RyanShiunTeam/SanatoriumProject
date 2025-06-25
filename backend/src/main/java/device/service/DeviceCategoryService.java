package device.service;

import java.util.List;
import device.bean.DeviceCategory;
import device.dao.DeviceCategoryDAO;
import device.dao.DeviceCategoryDAOImpl;

/**
 * 輔具分類業務邏輯處理類別
 */
public class DeviceCategoryService {

    private static DeviceCategoryService instance;
    private final DeviceCategoryDAO dao;

    // 私有建構子 - 單例模式
    private DeviceCategoryService() {
        this.dao = new DeviceCategoryDAOImpl();
    }

    // 同步取得單例實例
    public static synchronized DeviceCategoryService getInstance() {
        if (instance == null) {
            instance = new DeviceCategoryService();
        }
        return instance;
    }

    // 取得所有分類清單（排序由 DAO 決定）
    public List<DeviceCategory> getAllCategories() throws Exception {
        return dao.findAll();  // 有正確呼叫 DAO
    }

    // 依 ID 查詢單一分類
    public DeviceCategory getCategoryById(int id) throws Exception {
        return dao.findById(id);
    }

    // 新增分類（名稱必填）
    public boolean addCategory(DeviceCategory category) throws Exception {
        if (category == null || category.getName() == null || category.getName().isBlank()) {
            throw new IllegalArgumentException("分類名稱不可為空");
        }
        // 不需要檢查 ID 是否存在，因為資料表使用 IDENTITY 自動編號
        dao.insert(category);
        return true;
    }

    // 更新分類（必須存在且名稱非空）
    public boolean updateCategory(DeviceCategory category) throws Exception {
        if (category == null || category.getName() == null || category.getName().isBlank()) {
            throw new IllegalArgumentException("分類名稱不可為空");
        }
        if (!dao.existsById(category.getId())) {
            return false;
        }
        dao.update(category);
        return true;
    }

    // 刪除分類
    public boolean deleteCategory(int id) throws Exception {
        if (!dao.existsById(id)) {
            return false;
        }
        return dao.delete(id);
    }

    // 檢查分類是否存在
    public boolean existsById(int id) throws Exception {
        return dao.existsById(id);
    }

    // 模糊查詢分類名稱
    public List<DeviceCategory> searchCategoriesByName(String namePattern) throws Exception {
        if (namePattern == null) {
            namePattern = "";
        }
        return dao.findByNameLike(namePattern);
    }

    // 計算分類總數
    public int getCategoryCount() throws Exception {
        return dao.count();
    }
}
