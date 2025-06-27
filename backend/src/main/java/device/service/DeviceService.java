package device.service;

import java.math.BigDecimal;
import java.util.List;
import device.bean.Device;
import device.dao.DeviceDAO;
import device.dao.DeviceDAOImpl;


public class DeviceService {

    // 單例模式
    private static DeviceService instance;

    // DAO 物件，用來執行資料存取
    private final DeviceDAO dao;

    // 私有建構子，防止外部直接建立物件
    private DeviceService() {
        this.dao = new DeviceDAOImpl();
    }

    // 取得唯一實例（確保只有一個 service 存在）
    public static synchronized DeviceService getInstance() {
        if (instance == null) {
            instance = new DeviceService();
        }
        return instance;
    }

    // 查詢所有設備（不帶條件）
    public List<Device> getAllDevices() throws Exception {
        return dao.findAll();
    }

    // 根據 ID 查詢設備（整數版本）
    public Device getDeviceById(int id) throws Exception {
        return dao.findById(id);
    }

    // 根據 ID 查詢設備（字串版本，內含格式檢查）
    public Device getDeviceById(String idStr) throws Exception {
        if (idStr == null || idStr.isBlank()) return null;
        try {
            int id = Integer.parseInt(idStr);
            return getDeviceById(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("設備ID格式錯誤");
        }
    }

    // 新增設備，並檢查是否重複（若 ID 已存在則不新增）
    public boolean addDevice(Device device) throws Exception {
        validateDevice(device, false); // 驗證資料
        if (device.getId() != 0 && dao.existsById(device.getId())) {
            return false;
        }
        dao.insert(device);
        return true;
    }

    // 更新設備資料，若資料不存在則回傳 false
    public boolean updateDevice(Device device) throws Exception {
        validateDevice(device, true);
        if (!dao.existsById(device.getId())) {
            return false;
        }
        dao.update(device);
        return true;
    }

    // 根據 ID 刪除設備（整數版）
    public boolean deleteDevice(int id) throws Exception {
        if (!dao.existsById(id)) {
            return false;
        }
        return dao.delete(id);
    }

    // 根據 ID 刪除設備（字串版）
    public boolean deleteDevice(String idStr) throws Exception {
        if (idStr == null || idStr.isBlank()) return false;
        try {
            int id = Integer.parseInt(idStr);
            return deleteDevice(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("設備ID格式錯誤");
        }
    }

    // 檢查指定設備 ID 是否存在（整數版）
    public boolean existsById(int id) throws Exception {
        return dao.existsById(id);
    }

    // 檢查指定設備 ID 是否存在（字串版）
    public boolean existsById(String idStr) throws Exception {
        if (idStr == null || idStr.isBlank()) return false;
        try {
            int id = Integer.parseInt(idStr);
            return existsById(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("設備ID格式錯誤");
        }
    }

    // 模糊搜尋設備名稱（名稱包含輸入字串）
    public List<Device> searchDevicesByName(String namePattern) throws Exception {
        if (namePattern == null) {
            namePattern = "";
        }
        return dao.findByName(namePattern);
    }

    // 查詢設備總筆數
    public int getDeviceCount() throws Exception {
        return dao.count();
    }

    // 查詢所有設備並依指定欄位排序
    public List<Device> getDevicesWithSort(String sortBy, String order) throws Exception {
        return dao.findAllWithSort(sortBy, order);
    }

    // 分頁查詢設備（不含排序）
    public List<Device> getDevicesByPage(int page, int size) throws Exception {
        int offset = (page - 1) * size; // 計算起始筆數
        return dao.findByPage(offset, size);
    }

    // 分頁查詢設備（含排序）
    public List<Device> getDevicesByPageWithSort(int page, int size, String sortBy, String order) throws Exception {
        int offset = (page - 1) * size;
        return dao.findByPageWithSort(offset, size, sortBy, order);
    }

    // 查詢指定分類下的設備清單
    public List<Device> getDevicesByCategoryId(int categoryId) throws Exception {
        return dao.findDevicesByCategoryId(categoryId);
    }

    /**
     * 驗證設備資料正確性
     * @param device 待驗證設備物件
     * @param isUpdate 是否為更新（true 則需驗證 id）
     */
    private void validateDevice(Device device, boolean isUpdate) {
        if (device == null) {
            throw new IllegalArgumentException("設備資料不可為空");
        }
        if (isUpdate && device.getId() <= 0) {
            throw new IllegalArgumentException("設備ID格式錯誤");
        }
        if (device.getName() == null || device.getName().isBlank()) {
            throw new IllegalArgumentException("設備名稱不可為空");
        }
        if (device.getSku() == null || device.getSku().isBlank()) {
            throw new IllegalArgumentException("設備SKU不可為空");
        }
        if (device.getUnitPrice() == null || device.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("設備單價不可為空且不可為負數");
        }
        if (device.getInventory() < 0) {
            throw new IllegalArgumentException("設備庫存不可為負數");
        }
        if (device.getCategory() == null || device.getCategory().getId() <= 0) {
            throw new IllegalArgumentException("設備分類錯誤");
        }
    }
}
