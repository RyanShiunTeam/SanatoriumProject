package device.dao.model;

public class Device {
    private String id;
    private String name;           // 商品名稱
    private String sku;            // 商品貨號
    private java.math.BigDecimal unitPrice; // 單價（用 BigDecimal 表示金額）
    private int inventory;         // 庫存
    private String description;    // 商品描述
    private String image;          // 商品圖片
    private boolean isOnline;      // 是否上架

    private DeviceCategory category;  // 所屬分類（外鍵）

    // 無參建構子
    public Device() {
    }

    // 全參建構子
    public Device(String id, String name, String sku, java.math.BigDecimal unitPrice,
                  int inventory, String description, String image, boolean isOnline,
                  DeviceCategory category) {
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

    // Getter & Setter
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public java.math.BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(java.math.BigDecimal unitPrice) {
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

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", unitPrice=" + unitPrice +
                ", inventory=" + inventory +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", isOnline=" + isOnline +
                ", category=" + (category != null ? category.getName() : "未分類") +
                '}';
    }
}
