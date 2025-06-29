package device.controller;

import device.bean.Device;
import device.bean.DeviceCategory;
import device.service.DeviceCategoryService;
import device.service.DeviceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import utils.EmpService;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
//此 Servlet 用來處理設備更新（含圖片上傳）
@WebServlet("/DeviceUpdateServlet")
//若檔案 ≤ 1MB：暫存於記憶體  若檔案 > 1MB 且 ≤ 5MB：暫存為磁碟上的暫存檔  若檔案 > 5MB：直接擋掉，請求失敗
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024)
public class DeviceUpdateServlet extends HttpServlet {
    private final DeviceService deviceService = DeviceService.getInstance();
    private final DeviceCategoryService categoryService = DeviceCategoryService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
        	// 解析與轉型所有欄位資料
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String sku = request.getParameter("sku");
            String unitPriceStr = request.getParameter("unitPrice");
            String inventoryStr = request.getParameter("inventory");
            String description = request.getParameter("description");
            String isOnlineStr = request.getParameter("isOnline");
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String originalImage = request.getParameter("originalImage");

            // 驗證必要欄位是否填寫
            if (name == null || name.isBlank() || sku == null || sku.isBlank()
                    || unitPriceStr == null || unitPriceStr.isBlank()
                    || inventoryStr == null || inventoryStr.isBlank()) {
                throw new IllegalArgumentException("請填寫完整資訊！");
            }

            BigDecimal unitPrice = new BigDecimal(unitPriceStr);
            int inventory = Integer.parseInt(inventoryStr);
            boolean isOnline = Boolean.parseBoolean(isOnlineStr);

         // 處理圖片上傳
            Part imagePart = request.getPart("imageFile");
            String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
            String imagePath = originalImage; // 預設保留原圖

            if (fileName != null && !fileName.isBlank()) {
            	// 有上傳新圖片 → 儲存新圖片並替換原圖路徑
                String uploadPath = getServletContext().getRealPath("/images");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

             // 產生不重複的新檔名
                String newFileName = java.util.UUID.randomUUID().toString() + "_" + fileName;
                File file = new File(uploadDir, newFileName);
                imagePart.write(file.getAbsolutePath());  // 寫入檔案
                
                imagePath = "/images/" + newFileName;
            }

            // 查詢原始設備資料
            Device device = deviceService.getDeviceById(id);
            if (device == null) {
                request.setAttribute("errorMessage", "找不到指定的設備");
                request.getRequestDispatcher("/DevicePage/error.jsp").forward(request, response);
                return;
            }

         // 更新欄位值
            device.setName(name);
            device.setSku(sku);
            device.setUnitPrice(unitPrice);
            device.setInventory(inventory);
            device.setDescription(description);
            device.setImage(imagePath);          // 若未上傳新圖，保留原圖
            device.setOnline(isOnline);

         // 查詢分類資料並驗證
            DeviceCategory category = categoryService.getCategoryById(categoryId);
            if (category == null) {
                throw new IllegalArgumentException("分類不存在");
            }
            device.setCategory(category);

         // 執行更新
            boolean success = deviceService.updateDevice(device);
            // 記錄操作
    		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
    		if (success && loginUserId != null) {
    			new EmpService().record(loginUserId, "修改輔具", id);
    		}
            // 更新成功後導向列表
            response.sendRedirect(request.getContextPath() + "/DeviceServlet?status=updated");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "更新失敗：" + e.getMessage());
            request.getRequestDispatcher("/DevicePage/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
