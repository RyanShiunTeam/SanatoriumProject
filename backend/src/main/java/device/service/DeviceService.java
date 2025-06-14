package device.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import device.dao.DeviceCategoryDAO;
import device.dao.DeviceDAO;
import device.dao.model.Device;

public class DeviceService {

    private DeviceDAO deviceDAO;
    private DeviceCategoryDAO categoryDAO;

    public DeviceService(Connection conn) {
        this.deviceDAO = new DeviceDAO(conn);
        this.categoryDAO = new DeviceCategoryDAO(conn);
    }

    // 新增輔具，檢查ID是否存在，且檢查分類是否存在
    public boolean addDevice(Device device) throws SQLException {
        if (deviceDAO.existsById(device.getId())) {
            return false; // 已有該輔具ID，無法新增
        }
        if (device.getCategory() != null) {
            if (!categoryDAO.existsById(device.getCategory().getId())) {
                throw new SQLException("指定的分類不存在");
            }
        }
        deviceDAO.insert(device);
        return true;
    }

    // 取得所有輔具列表
    public List<Device> getAllDevices() throws SQLException {
        return deviceDAO.findAll();
    }

    // 依ID查詢輔具，找不到回傳null
    public Device getDeviceById(String id) throws SQLException {
        return deviceDAO.findById(id);
    }

    // 依名稱模糊搜尋輔具
    public List<Device> searchDevicesByName(String namePattern) throws SQLException {
        return deviceDAO.findByName(namePattern);
    }

    // 更新輔具，先確認輔具存在，且指定分類存在（若有指定）
    public boolean updateDevice(Device device) throws SQLException {
        if (!deviceDAO.existsById(device.getId())) {
            return false; // 輔具不存在
        }
        if (device.getCategory() != null) {
            if (!categoryDAO.existsById(device.getCategory().getId())) {
                throw new SQLException("指定的分類不存在");
            }
        }
        deviceDAO.update(device);
        return true;
    }

    // 刪除輔具，先確認存在
    public boolean deleteDevice(String id) throws SQLException {
        if (!deviceDAO.existsById(id)) {
            return false;
        }
        deviceDAO.delete(id);
        return true;
    }

    // 計算輔具數量
    public int getDeviceCount() throws SQLException {
        return deviceDAO.count();
    }
}

