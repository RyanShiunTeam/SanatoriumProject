package device.bean;

/**
 * 輔具分類類別
 */
public class DeviceCategory {

    private int id;               // 主鍵 ID
    private String name;          // 分類名稱
    private int categoryId;       // 分類排序（資料表中為 category_id）

    public DeviceCategory() {}

    public DeviceCategory(int id, String name, int categoryId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
    }

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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "DeviceCategory [id=" + id + ", name=" + name + ", categoryId=" + categoryId + "]";
    }
}
