package device.controller;

import device.bean.DeviceCategory;
import device.service.DeviceCategoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/CategoryEditServlet")
public class CategoryEditServlet extends HttpServlet {
	// 注入分類服務（使用 Singleton 模式取得）
    private final DeviceCategoryService categoryService = DeviceCategoryService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
        	// 從請求中取得參數：分類 id、名稱、分類代號
            String idStr = request.getParameter("id");
            String name = request.getParameter("name");
            String categoryIdStr = request.getParameter("categoryId");

         // 驗證是否有欄位漏填
            if (idStr == null || idStr.isBlank() || name == null || name.isBlank()
                    || categoryIdStr == null || categoryIdStr.isBlank()) {
                throw new IllegalArgumentException("請填寫完整資訊");
            }

         // 將字串轉換為整數型別
            int id = Integer.parseInt(idStr);
            int categoryId = Integer.parseInt(categoryIdStr);

         // 封裝為 DeviceCategory 物件
            DeviceCategory category = new DeviceCategory(id, name, categoryId);

            // 呼叫 service 層執行更新
            boolean updated = categoryService.updateCategory(category);
            if (updated) {
            	// 更新成功，重新導向分類列表頁，並附加狀態參數
                response.sendRedirect(request.getContextPath() + "/CategoryServlet?status=updated");
            } else {
                request.setAttribute("errorMessage", "分類不存在，無法更新。");
                request.getRequestDispatcher("/DevicePage/error.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "更新分類失敗：" + e.getMessage());
            request.getRequestDispatcher("/DevicePage/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
