package device.dao;

import java.util.List;
import device.bean.Device;


public interface DeviceDAO {

    // 檢查指定設備 ID 是否存在於資料表中
    boolean existsById(int id) throws Exception;

    // 新增一筆設備資料
    void insert(Device device) throws Exception;

    // 更新指定設備的資料（依據 device_id）
    void update(Device device) throws Exception;

    // 根據設備 ID 刪除資料，回傳是否成功
    boolean delete(int id) throws Exception;

    // 根據設備 ID 查詢單筆設備資料（含分類資訊）
    Device findById(int id) throws Exception;

    // 查詢所有設備資料（通常預設依名稱排序）
    List<Device> findAll() throws Exception;

    // 查詢屬於指定分類 ID 的所有設備
    List<Device> findDevicesByCategoryId(int categoryId) throws Exception;

    // 依設備名稱模糊查詢設備清單（%關鍵字%）
    List<Device> findByName(String namePattern) throws Exception;

    // 回傳設備資料總筆數
    int count() throws Exception;

    // 查詢所有設備並依指定欄位排序（如 unitPrice, inventory, name）
    List<Device> findAllWithSort(String sortBy, String order) throws Exception;

    // 分頁查詢設備資料（offset: 起始筆數, limit: 筆數）
    List<Device> findByPage(int offset, int limit) throws Exception;

    // 分頁查詢設備並排序
    List<Device> findByPageWithSort(int offset, int limit, String sortBy, String order) throws Exception;
}
