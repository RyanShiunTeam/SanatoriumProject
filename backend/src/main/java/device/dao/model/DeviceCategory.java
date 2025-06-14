package device.dao.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeviceCategory {
    private String id;
    private String name;
    private int position;

    // has-many 關係：這個分類有多個輔具
    private List<Device> devices = new ArrayList<>();

    // 建構子
    public DeviceCategory(String id, String name, int position) {
    	this.id = UUID.randomUUID().toString().replace("-", ""); // 自動產生
        this.name = name;
        this.position = position;
    }

    public DeviceCategory() {
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public void addDevice(Device device) {
        device.setCategory(this); // 建立 belongs-to 關係
        this.devices.add(device);
    }

    @Override
    public String toString() {
        return "DeviceCategory{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", position=" + position +
                ", deviceCount=" + devices.size() +
                '}';
    }
}
