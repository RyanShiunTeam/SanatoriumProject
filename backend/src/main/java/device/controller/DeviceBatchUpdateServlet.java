package device.controller;

import device.bean.Device;
import device.service.DeviceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import utils.EmpService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/DeviceBatchUpdateServlet")
public class DeviceBatchUpdateServlet extends HttpServlet {
	 // 注入設備服務層
    private final DeviceService deviceService = DeviceService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
        	// 從表單取得批次欄位資料（陣列）
            String[] ids = request.getParameterValues("id");
            String[] names = request.getParameterValues("name");
            String[] skus = request.getParameterValues("sku");
            String[] unitPrices = request.getParameterValues("unitPrice");
            String[] inventories = request.getParameterValues("inventory");
            String[] descriptions = request.getParameterValues("description");
            String[] images = request.getParameterValues("image");
            String[] isOnlines = request.getParameterValues("isOnline");
            System.out.println(ids);

            // 檢查是否有任何選取的資料
            if (ids == null || ids.length == 0) {
                throw new IllegalArgumentException("未選擇任何資料進行批次修改");
            }

         // 確保所有欄位長度一致，避免資料錯置
            int length = ids.length;
            if (names.length != length || skus.length != length || unitPrices.length != length
                    || inventories.length != length || descriptions.length != length
                    || images.length != length || isOnlines.length != length) {
                throw new IllegalArgumentException("傳入資料不完整");
            }

         // 用來儲存已更新欄位的設備清單
            List<Device> updatedDevices = new ArrayList<>();

         // 依序處理每一筆資料
            for (int i = 0; i < length; i++) {
            	// 根據 ID 查詢原始設備資料
                Device device = deviceService.getDeviceById(ids[i]);
                if (device != null) {
                	// 更新欄位值
                    device.setName(names[i]);
                    device.setSku(skus[i]);
                    device.setUnitPrice(new BigDecimal(unitPrices[i]));
                    device.setInventory(Integer.parseInt(inventories[i]));
                    device.setDescription(descriptions[i]);
                    device.setImage(images[i]);
                    device.setOnline(Boolean.parseBoolean(isOnlines[i]));
                    
                    updatedDevices.add(device);// 加入更新清單
                }
            }

            // 執行更新動作（逐筆呼叫 service）
            for (Device device : updatedDevices) {
                deviceService.updateDevice(device);
            }
            
            // 記錄操作
    		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
    		if (loginUserId != null) {
    			new EmpService().record(loginUserId, "批次修改輔具", null);
    		}

         // 更新成功後導向列表頁
            response.sendRedirect(request.getContextPath() + "/DeviceServlet?status=batchUpdated");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "批次更新失敗：" + e.getMessage());
            request.getRequestDispatcher("/DevicePage/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
