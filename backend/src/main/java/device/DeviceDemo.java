package device;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import device.dao.model.Device;
import device.dao.model.DeviceCategory;
import device.service.DeviceCategoryService;
import device.service.DeviceService;

public class DeviceDemo {

    static final String deviceCategoryCsv = "C:\\csv\\deviceCategory.csv";
    static final String deviceCsv = "C:\\csv\\device.csv";

    public static void main(String[] args) {
        // 載入 jdbc.properties 檔案
        String propertyFilePath = "src/main/resource/jdbc.properties";

        try (InputStream input = new FileInputStream(propertyFilePath)) {
            Properties props = new Properties();
            props.load(input);

            String jdbcUrl = props.getProperty("url");
            String user = props.getProperty("user");
            String password = props.getProperty("password");

            try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
                 Scanner scanner = new Scanner(System.in)) {
                conn.setAutoCommit(false);

                DeviceCategoryService categoryService = new DeviceCategoryService(conn);
                DeviceService deviceService = new DeviceService(conn);

                boolean exit = false;
                while (!exit) {
                    printMenu();
                    System.out.print("請輸入選項：");
                    String inputStr = scanner.nextLine().trim();

                    switch (inputStr) {
                        case "1":
                            importCategories(categoryService);
                            conn.commit();
                            System.out.println("輔具分類匯入完成！");
                            break;
                        case "2":
                            printCategories(categoryService);
                            break;
                        case "3":
                            addCategory(scanner, categoryService);
                            conn.commit();
                            break;
                        case "4":
                            updateCategory(scanner, categoryService);
                            conn.commit();
                            break;
                        case "5":
                            deleteCategory(scanner, categoryService);
                            conn.commit();
                            break;
                        case "6":
                            importDevices(deviceService, categoryService);
                            conn.commit();
                            System.out.println("輔具資料匯入完成！");
                            break;
                        case "7":
                            printDevices(deviceService);
                            break;
                        case "8":
                            addDevice(scanner, deviceService, categoryService);
                            conn.commit();
                            break;
                        case "9":
                            updateDevice(scanner, deviceService, categoryService);
                            conn.commit();
                            break;
                        case "10":
                            deleteDevice(scanner, deviceService);
                            conn.commit();
                            break;
                        case "0":
                            exit = true;
                            System.out.println("程式結束");
                            break;
                        default:
                            System.out.println("無效選項，請重新輸入。");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            System.err.println("無法讀取 jdbc.properties 檔案：" + propertyFilePath);
            ex.printStackTrace();
        }
    }

    private static void printMenu() {
        System.out.println("\n==== 功能選單 ====");
        System.out.println("1. 匯入輔具分類（CSV）");
        System.out.println("2. 顯示所有輔具分類");
        System.out.println("3. 新增輔具分類");
        System.out.println("4. 更新輔具分類");
        System.out.println("5. 刪除輔具分類");
        System.out.println("6. 匯入輔具資料（CSV）");
        System.out.println("7. 顯示所有輔具資料");
        System.out.println("8. 新增輔具資料");
        System.out.println("9. 更新輔具資料");
        System.out.println("10. 刪除輔具資料");
        System.out.println("0. 離開");
    }

    // --- 輔具分類相關 ---

    private static void importCategories(DeviceCategoryService categoryService) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(deviceCategoryCsv))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) { isFirstLine = false; continue; }
                String[] cols = line.replace("\"", "").split(",");
                if (cols.length >= 3) {
                    DeviceCategory cat = new DeviceCategory();
                    cat.setId(cols[0].trim());
                    cat.setName(cols[1].trim());
                    cat.setPosition(Integer.parseInt(cols[2].trim()));
                    categoryService.addCategory(cat);
                }
            }
        }
    }

    private static void printCategories(DeviceCategoryService categoryService) throws Exception {
        List<DeviceCategory> categories = categoryService.getAllCategories();
        System.out.println("==== 所有輔具分類 ====");
        for (DeviceCategory c : categories) {
            System.out.printf("ID:%s 名稱:%s 排序:%d\n", c.getId(), c.getName(), c.getPosition());
        }
    }

    private static void addCategory(Scanner scanner, DeviceCategoryService categoryService) throws Exception {
        //System.out.println("請輸入輔具分類ID：");
        //String id = scanner.nextLine().trim();
        System.out.println("請輸入分類名稱：");
        String name = scanner.nextLine().trim();
        System.out.println("請輸入分類排序（整數）：");
        int position = Integer.parseInt(scanner.nextLine().trim());

        DeviceCategory cat = new DeviceCategory();
        //cat.setId(id);
        cat.setName(name);
        cat.setPosition(position);

        categoryService.addCategory(cat);
        System.out.println("新增成功！");
    }

    private static void updateCategory(Scanner scanner, DeviceCategoryService categoryService) throws Exception {
    	//System.out.println("請輸入要更新的輔具分類ID：");
    	//String id = scanner.nextLine().trim();
        DeviceCategory cat = categoryService.getCategoryById(id);
        if (cat == null) {
            System.out.println("找不到該分類ID");
            return;
        }
        System.out.printf("目前名稱: %s，請輸入新名稱（ENTER保留不變）：\n", cat.getName());
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) cat.setName(name);

        System.out.printf("目前排序: %d，請輸入新排序（ENTER保留不變）：\n", cat.getPosition());
        String posStr = scanner.nextLine().trim();
        if (!posStr.isEmpty()) cat.setPosition(Integer.parseInt(posStr));

        categoryService.updateCategory(cat);
        System.out.println("更新成功！");
    }

    private static void deleteCategory(Scanner scanner, DeviceCategoryService categoryService) throws Exception {
        System.out.println("請輸入要刪除的輔具分類ID：");
        String id = scanner.nextLine().trim();
        categoryService.deleteCategory(id);
        System.out.println("刪除成功！");
    }

    // --- 輔具相關 ---

    private static void importDevices(DeviceService deviceService, DeviceCategoryService categoryService) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(deviceCsv))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) { isFirstLine = false; continue; }
                String[] cols = line.replace("\"", "").split(",");
                if (cols.length >= 9) {
                    Device device = new Device();
                    device.setId(cols[0].trim());
                    device.setName(cols[1].trim());
                    device.setSku(cols[2].trim());
                    device.setUnitPrice(new BigDecimal(cols[3].trim()));
                    device.setInventory(Integer.parseInt(cols[4].trim()));
                    device.setDescription(cols[5].trim());
                    device.setImage(cols[6].trim());
                    device.setOnline(Boolean.parseBoolean(cols[7].trim()));

                    DeviceCategory category = categoryService.getCategoryById(cols[8].trim());
                    device.setCategory(category);

                    deviceService.addDevice(device);
                }
            }
        }
    }

    private static void printDevices(DeviceService deviceService) throws Exception {
        List<Device> devices = deviceService.getAllDevices();
        System.out.println("==== 所有輔具 ====");
        for (Device d : devices) {
            System.out.printf("ID:%s 名稱:%s SKU:%s 價格:%s 庫存:%d 上架:%s 分類:%s\n",
                    d.getId(), d.getName(), d.getSku(), d.getUnitPrice().toString(),
                    d.getInventory(), d.isOnline() ? "是" : "否",
                    d.getCategory() != null ? d.getCategory().getName() : "無分類");
        }
    }

    private static void addDevice(Scanner scanner, DeviceService deviceService, DeviceCategoryService categoryService) throws Exception {
        System.out.println("請輸入輔具ID：");
        String id = scanner.nextLine().trim();

        System.out.println("請輸入商品名稱：");
        String name = scanner.nextLine().trim();

        System.out.println("請輸入商品貨號(SKU)：");
        String sku = scanner.nextLine().trim();

        System.out.println("請輸入單價(數字)：");
        BigDecimal price = new BigDecimal(scanner.nextLine().trim());

        System.out.println("請輸入庫存數量(整數)：");
        int inventory = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("請輸入商品描述：");
        String desc = scanner.nextLine().trim();

        System.out.println("請輸入商品圖片路徑：");
        String image = scanner.nextLine().trim();

        System.out.println("是否上架(true/false)：");
        boolean isOnline = Boolean.parseBoolean(scanner.nextLine().trim());

        System.out.println("請輸入分類ID：");
        String catId = scanner.nextLine().trim();
        DeviceCategory category = categoryService.getCategoryById(catId);
        if (category == null) {
            System.out.println("找不到該分類ID，請先新增分類或重新輸入");
            return;
        }

        Device device = new Device();
        device.setId(id);
        device.setName(name);
        device.setSku(sku);
        device.setUnitPrice(price);
        device.setInventory(inventory);
        device.setDescription(desc);
        device.setImage(image);
        device.setOnline(isOnline);
        device.setCategory(category);

        deviceService.addDevice(device);
        System.out.println("新增成功！");
    }

    private static void updateDevice(Scanner scanner, DeviceService deviceService, DeviceCategoryService categoryService) throws Exception {
        System.out.println("請輸入要更新的輔具ID：");
        String id = scanner.nextLine().trim();
        Device device = deviceService.getDeviceById(id);
        if (device == null) {
            System.out.println("找不到該輔具ID");
            return;
        }

        System.out.printf("目前名稱: %s，請輸入新名稱（ENTER保留不變）：\n", device.getName());
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) device.setName(name);

        System.out.printf("目前SKU: %s，請輸入新SKU（ENTER保留不變）：\n", device.getSku());
        String sku = scanner.nextLine().trim();
        if (!sku.isEmpty()) device.setSku(sku);

        System.out.printf("目前單價: %s，請輸入新單價（ENTER保留不變）：\n", device.getUnitPrice());
        String priceStr = scanner.nextLine().trim();
        if (!priceStr.isEmpty()) device.setUnitPrice(new BigDecimal(priceStr));

        System.out.printf("目前庫存: %d，請輸入新庫存（ENTER保留不變）：\n", device.getInventory());
        String invStr = scanner.nextLine().trim();
        if (!invStr.isEmpty()) device.setInventory(Integer.parseInt(invStr));

        System.out.printf("目前描述: %s，請輸入新描述（ENTER保留不變）：\n", device.getDescription());
        String desc = scanner.nextLine().trim();
        if (!desc.isEmpty()) device.setDescription(desc);

        System.out.printf("目前圖片路徑: %s，請輸入新路徑（ENTER保留不變）：\n", device.getImage());
        String img = scanner.nextLine().trim();
        if (!img.isEmpty()) device.setImage(img);

        System.out.printf("目前是否上架: %s，請輸入(true/false)（ENTER保留不變）：\n", device.isOnline());
        String onlineStr = scanner.nextLine().trim();
        if (!onlineStr.isEmpty()) device.setOnline(Boolean.parseBoolean(onlineStr));

        System.out.printf("目前分類ID: %s，請輸入新分類ID（ENTER保留不變）：\n", 
            device.getCategory() != null ? device.getCategory().getId() : "無分類");
        String catId = scanner.nextLine().trim();
        if (!catId.isEmpty()) {
            DeviceCategory category = categoryService.getCategoryById(catId);
            if (category == null) {
                System.out.println("找不到該分類ID，分類不變更");
            } else {
                device.setCategory(category);
            }
        }

        deviceService.updateDevice(device);
        System.out.println("更新成功！");
    }

    private static void deleteDevice(Scanner scanner, DeviceService deviceService) throws Exception {
        System.out.println("請輸入要刪除的輔具ID：");
        String id = scanner.nextLine().trim();
        deviceService.deleteDevice(id);
        System.out.println("刪除成功！");
    }
}

