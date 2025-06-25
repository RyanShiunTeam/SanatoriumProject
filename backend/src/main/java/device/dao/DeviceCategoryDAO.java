package device.dao;

import java.util.List;
import device.bean.DeviceCategory;

public interface DeviceCategoryDAO {
	
	// 查詢所有分類資料，回傳 DeviceCategory 清單
    List<DeviceCategory> findAll() throws Exception;
    
    // 根據分類主鍵 id 查詢分類資料，找不到則回傳 null
    DeviceCategory findById(int id) throws Exception;
    
    // 新增一筆分類資料，傳入的 category 不需包含 id，新增後會自動回寫 id
    void insert(DeviceCategory category) throws Exception;
    
    // 更新分類資料（依據 category.id 更新其 name 與 category_id）
    void update(DeviceCategory category) throws Exception;

    // 根據主鍵 id 刪除分類資料，回傳是否成功刪除
    boolean delete(int id) throws Exception;

    // 檢查某個 id 是否存在於分類資料中，回傳 true 表示存在
    boolean existsById(int id) throws Exception;
    
    // 模糊搜尋分類名稱，回傳名稱包含關鍵字的分類清單
    List<DeviceCategory> findByNameLike(String namePattern) throws Exception;
    
    // 回傳目前分類資料的總筆數
    int count() throws Exception;
}
