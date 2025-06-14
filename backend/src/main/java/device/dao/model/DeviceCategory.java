package device.dao.model;

import java.util.ArrayList;
import java.util.List;

public class DeviceCategory {
    private String id;
    private String name;
    private List<Device> devices = new ArrayList<>(); // has many

    public DeviceCategory(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addDevice(Device device) {
        device.setCategory(this); // 建立 belongs-to 關係
        devices.add(device);
    }

    // Getters and setters...
}
