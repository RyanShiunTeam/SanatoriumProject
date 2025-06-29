package device.controller;

import device.service.DeviceCategoryService;
import device.service.DeviceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import utils.EmpService;

import java.io.IOException;

@WebServlet("/CategoryDeleteServlet")
public class CategoryDeleteServlet extends HttpServlet {
	// 注入分類與設備服務，用於資料操作
    private final DeviceCategoryService categoryService = DeviceCategoryService.getInstance();
    private final DeviceService deviceService = DeviceService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
        	// 從請求中取得分類 ID（字串）
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.isBlank()) {
                throw new IllegalArgumentException("未提供分類 ID");
            }

            // 將字串轉為整數 ID
            int id = Integer.parseInt(idStr);

         // 檢查該分類底下是否還有設備資料（不能直接刪除）
            if (!deviceService.getDevicesByCategoryId(id).isEmpty()) {
            	// 若仍有設備，顯示錯誤訊息並導向錯誤頁面
                request.setAttribute("errorMessage", "❌ 無法刪除：該分類下仍有設備資料！");
                request.getRequestDispatcher("/DevicePage/error.jsp").forward(request, response);
                return;
            }

         // 呼叫 service 執行刪除分類
            boolean success = categoryService.deleteCategory(id);
            if (success) {
                // 記錄操作
        		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
        		if (success && loginUserId != null) {
        			new EmpService().record(loginUserId, "刪除輔具分類", id);
        		}
            	// 成功刪除後重新導向分類清單頁
                response.sendRedirect(request.getContextPath() + "/CategoryServlet?status=deleted");
            } else {
            	// 若刪除失敗（可能已刪除或不存在）
                request.setAttribute("errorMessage", "刪除失敗：分類不存在或已被刪除");
                request.getRequestDispatcher("/DevicePage/error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "刪除分類失敗：" + e.getMessage());
            request.getRequestDispatcher("/DevicePage/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
