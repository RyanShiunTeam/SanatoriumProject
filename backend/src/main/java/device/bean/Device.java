package device.bean;

import java.math.BigDecimal;

public class Device {
    private int id;  // 設備的唯一識別碼（主鍵）
    private String name;
    private String sku;
    private BigDecimal unitPrice;  // 設備的單價（使用 BigDecimal 處理金額避免精度問題）
    private int inventory;     
    private String description;
    private String image;               // 設備圖片檔名或路徑
    private boolean isOnline;            // 是否上架（true 表示已上架）
    private DeviceCategory category;

    public Device() {}

    public Device(int id, String name, String sku, BigDecimal unitPrice, int inventory,
                  String description, String image, boolean isOnline, DeviceCategory category) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.unitPrice = unitPrice;
        this.inventory = inventory;
        this.description = description;
        this.image = image;
        this.isOnline = isOnline;
        this.category = category;
    }
    
    //getter and setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public DeviceCategory getCategory() {
        return category;
    }

    public void setCategory(DeviceCategory category) {
        this.category = category;
    }
    // 取得分類 ID，若分類為 null 則回傳 0（避免 NullPointerException）
    public int getCategoryId() {
        return category != null ? category.getId() : 0;
    }

    @Override
    public String toString() {
        return "Device [id=" + id + ", name=" + name + ", sku=" + sku + ", unitPrice=" + unitPrice +
               ", inventory=" + inventory + ", description=" + description + ", image=" + image +
               ", isOnline=" + isOnline + ", category=" + (category != null ? category.getName() : "無") + "]";
    }
}
