package device.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import utils.EmpService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import device.bean.Device;
import device.service.DeviceService;

@WebServlet("/DeviceExportServlet")
public class DeviceExportServlet extends HttpServlet {
	 // 取得設備服務物件（使用 Singleton）
    private final DeviceService deviceService = DeviceService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	// 設定回應類型為 CSV 並指定檔案名稱
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"devices.csv\"");

        try (PrintWriter writer = response.getWriter()) {
        	// 寫入 CSV 標題列
            writer.println("\"id\",\"name\",\"sku\",\"unitPrice\",\"inventory\",\"description\",\"image\",\"isOnline\",\"categoryId\"");

         // 取得所有設備資料
            List<Device> deviceList = deviceService.getAllDevices();
         // 逐筆寫入設備資料列（以 CSV 格式）
            for (Device d : deviceList) {
                writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%d\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                        d.getId(),
                        d.getName(),
                        d.getSku(),
                        d.getUnitPrice(),
                        d.getInventory(),
                        d.getDescription() == null ? "" : d.getDescription(),
                        d.getImage() == null ? "" : d.getImage(),
                        d.isOnline(),
                        d.getCategoryId()
                );
            }
            // 記錄操作
    		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
    		if (loginUserId != null) {
    			new EmpService().record(loginUserId, "正在偷取公司資料", null);
    		}
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("匯出失敗：" + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
