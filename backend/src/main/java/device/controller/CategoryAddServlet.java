package device.controller;

import device.bean.DeviceCategory;
import device.service.DeviceCategoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import utils.EmpService;

import java.io.IOException;

@WebServlet("/CategoryAddServlet")
public class CategoryAddServlet extends HttpServlet {
	
	// 取得分類服務的單例物件，用來存取分類資料
    private final DeviceCategoryService categoryService = DeviceCategoryService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
        	// 從表單取得分類名稱與分類代號
            String name = request.getParameter("name");
            String categoryIdStr = request.getParameter("categoryId");
            
            // 檢查是否有缺漏輸入
            if (name == null || name.isBlank() || categoryIdStr == null || categoryIdStr.isBlank()) {
                throw new IllegalArgumentException("請填寫完整資訊");
            }
            // 將字串型別的 categoryId 轉換為整數
            int categoryId = Integer.parseInt(categoryIdStr);

            // 封裝為 DeviceCategory 物件
            DeviceCategory category = new DeviceCategory();
            category.setName(name);
            category.setCategoryId(categoryId); // 對應資料表欄位 category_id

            // 呼叫 Service 層新增分類
            boolean success = categoryService.addCategory(category);
            if (success) {
            	
                // 記錄操作
        		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
        		if (success && loginUserId != null) {
        			new EmpService().record(loginUserId, "新增輔具分類", null);
        		}
            	// 新增成功後導向分類列表頁，並附帶狀態參數
                response.sendRedirect(request.getContextPath() + "/CategoryServlet?status=created");
            } else {
            	// 若新增失敗（可能因為已存在），導向錯誤頁面
                request.setAttribute("errorMessage", "新增分類失敗：分類已存在");
                request.getRequestDispatcher("/DevicePage/error.jsp").forward(request, response);
            }

        } catch (Exception e) {
        	// 發生例外時顯示錯誤訊息
            e.printStackTrace();
            request.setAttribute("errorMessage", "新增分類失敗：" + e.getMessage());
            request.getRequestDispatcher("/DevicePage/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
