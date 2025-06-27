package device.controller;

import device.bean.Device;
import device.bean.DeviceCategory;
import device.service.DeviceCategoryService;
import device.service.DeviceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;

@WebServlet("/DeviceAddServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024)
public class DeviceAddServlet extends HttpServlet {
	// 注入設備與分類的服務層
    private final DeviceService deviceService = DeviceService.getInstance();
    private final DeviceCategoryService categoryService = DeviceCategoryService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
        	// 取得表單欄位資料
            String name = request.getParameter("name");
            String sku = request.getParameter("sku");
            String unitPriceStr = request.getParameter("unitPrice");
            String inventoryStr = request.getParameter("inventory");
            String description = request.getParameter("description");
            String isOnlineStr = request.getParameter("isOnline");
            String categoryIdStr = request.getParameter("categoryId");
            
            System.out.println("準備新增: " + name);
         // 驗證必填欄位是否完整
            if (name == null || name.isBlank() || sku == null || sku.isBlank() ||
                unitPriceStr == null || unitPriceStr.isBlank() || inventoryStr == null || inventoryStr.isBlank() || categoryIdStr == null || categoryIdStr.isBlank()) {
                throw new IllegalArgumentException("請填寫完整資訊！");
            }

         // 轉型欄位值
            BigDecimal unitPrice = new BigDecimal(unitPriceStr);
            int inventory = Integer.parseInt(inventoryStr);
            boolean isOnline = "true".equalsIgnoreCase(isOnlineStr) || "1".equals(isOnlineStr);

         // 取得分類資訊（驗證是否存在）
            int categoryId = Integer.parseInt(categoryIdStr);
            DeviceCategory category = categoryService.getCategoryById(categoryId);
            if (category == null) {
                throw new IllegalArgumentException("分類不存在");
            }

         // 處理圖片上傳（imageFile 為 input name）
            Part imagePart = request.getPart("imageFile");
            String imagePath = ""; // 預設為空（無圖片）
            if (imagePart != null && imagePart.getSize() > 0) {
            	// 取得原始檔名（保留副檔名）
                String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();

             // 準備圖片儲存資料夾路徑（Web 專案中的 /images）
                String uploadPath = getServletContext().getRealPath("/images");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

             // 產生新檔名：UUID_原始名稱
                String newFileName = java.util.UUID.randomUUID().toString() + "_" + fileName;
             // 寫入硬碟
                File file = new File(uploadDir, newFileName);
                imagePart.write(file.getAbsolutePath());
             // 設定圖片相對路徑（儲存在資料庫）
                imagePath = "/images/" + newFileName;
            }

            // 封裝 Device 物件
            Device device = new Device();
            device.setName(name);
            device.setSku(sku);
            device.setUnitPrice(unitPrice);
            device.setInventory(inventory);
            device.setDescription(description);
            device.setImage(imagePath);  // 圖片路徑
            device.setOnline(isOnline);
            device.setCategory(category); // 將分類一併設定

            // 新增設備資料
            deviceService.addDevice(device);

         // 新增成功後導向列表頁
            response.sendRedirect(request.getContextPath() + "/DeviceServlet?status=created");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("success", false);
            request.setAttribute("message", "新增失敗：" + e.getMessage());
            request.getRequestDispatcher("/DevicePage/result.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
