package device.controller;

import device.bean.Device;
import device.bean.DeviceCategory;
import device.service.DeviceCategoryService;
import device.service.DeviceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/DeviceImportServlet")
@MultipartConfig
public class DeviceImportServlet extends HttpServlet {
	
	// 注入服務層
    private final DeviceService deviceService = DeviceService.getInstance();
    private final DeviceCategoryService categoryService = DeviceCategoryService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	 // 成功、跳過、錯誤計數器
        int successCount = 0;
        int skipCount = 0;
        int errorCount = 0;
        int lineNum =0; // 行號（用於顯示錯誤行數）
        List<String> errorMessages = new ArrayList<>();

        try {
        	// 取得上傳的檔案部份（input name="csvFile"）
            Part filePart = request.getPart("csvFile");

            try (InputStream fileContent = filePart.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent, "UTF-8"))) {

                String line;
                

                while ((line = reader.readLine()) != null) {
                    lineNum++;

                 // 忽略第一行標題列（若含有 name 和 sku）
                    if (lineNum == 1 && line.toLowerCase().contains("name") && line.toLowerCase().contains("sku")) {
                        continue; // 略過標題列
                    }

                 // 將每行依逗號分隔，最多接受 8 欄
                    String[] fields = line.split(",", -1);
                    if (fields.length < 8) {
                        errorMessages.add("第 " + lineNum + " 行欄位不足（共 " + fields.length + " 欄）");
                        errorCount++;
                        continue;
                    }

                    try {
                    	// 資料解析與驗證
                        String name = fields[0].trim();
                        if (name.isEmpty()) throw new IllegalArgumentException("名稱不能為空");

                        String sku = fields[1].trim();
                        BigDecimal unitPrice = new BigDecimal(fields[2].trim());
                        int inventory = Integer.parseInt(fields[3].trim());
                        String description = fields[4].trim();
                        String image = fields[5].trim();
                        if (image.isEmpty()) image = "default.jpg";
                        boolean isOnline = fields[6].trim().equalsIgnoreCase("true") || fields[6].trim().equals("1");
                        int categoryId = Integer.parseInt(fields[7].trim());

                     // 驗證分類是否存在
                        DeviceCategory category = categoryService.getCategoryById(categoryId);
                        if (category == null) {
                            skipCount++;
                            errorMessages.add("第 " + lineNum + " 行分類不存在（ID: " + categoryId + "）");
                            continue;
                        }

                     // 封裝成 Device 物件
                        Device device = new Device();
                        device.setName(name);
                        device.setSku(sku);
                        device.setUnitPrice(unitPrice);
                        device.setInventory(inventory);
                        device.setDescription(description);
                        device.setImage(image);
                        device.setOnline(isOnline);
                        device.setCategory(category);

                        // 檢查是否已有同名設備（若有則更新，否則新增）
                        List<Device> existingDevices = deviceService.searchDevicesByName(name);
                        if (!existingDevices.isEmpty()) {
                            Device existing = existingDevices.get(0);
                            device.setId(existing.getId());
                            deviceService.updateDevice(device);
                        } else {
                            deviceService.addDevice(device);
                        }

                        successCount++;

                    } catch (Exception e) {
                        e.printStackTrace();
                        errorMessages.add("第 " + lineNum + " 行錯誤：" + e.getMessage());
                        errorCount++;
                    }
                }
            }

         // 將結果統計資料傳回 JSP 顯示
            request.setAttribute("successCount", successCount);
            request.setAttribute("skipCount", skipCount);
            request.setAttribute("errorCount", errorCount);
            request.setAttribute("errorMessages", errorMessages);
            request.setAttribute("totalCount", lineNum - 1);
            request.getRequestDispatcher("/DevicePage/importResult.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "匯入失敗：" + e.getMessage());
            request.getRequestDispatcher("/DevicePage/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
