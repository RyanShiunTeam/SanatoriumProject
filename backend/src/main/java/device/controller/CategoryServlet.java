package device.controller;

import device.bean.Device;
import device.bean.DeviceCategory;
import device.service.DeviceCategoryService;
import device.service.DeviceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/CategoryServlet")
public class CategoryServlet extends HttpServlet {

	// 注入服務層：分類與設備
    private final DeviceCategoryService categoryService = DeviceCategoryService.getInstance();
    private final DeviceService deviceService = DeviceService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	// 取得 action 參數，用來區分不同操作
        String action = request.getParameter("action");

        try {
            if ("searchByName".equals(action)) {
            	// 搜尋分類名稱（模糊比對）
                String name = request.getParameter("name");
                List<DeviceCategory> list = categoryService.searchCategoriesByName(name);
                request.setAttribute("categoryList", list);
                request.getRequestDispatcher("/DevicePage/categoryList.jsp").forward(request, response);

            } else if ("edit".equals(action)) {
            	// 編輯分類：先查詢要編輯的分類資料並轉交至 JSP 編輯頁面
                String id = request.getParameter("id");
                DeviceCategory category = categoryService.getCategoryById(Integer.parseInt(id));
                request.setAttribute("category", category);
                request.getRequestDispatcher("/DevicePage/editCategory.jsp").forward(request, response);

            } else if ("viewDevices".equals(action)) {
            	//  查看指定分類下所有設備（根據分類 ID）
                String idStr = request.getParameter("id");
                if (idStr != null && !idStr.isBlank()) {
                    int id = Integer.parseInt(idStr);
                    DeviceCategory category = categoryService.getCategoryById(id);
                    if (category != null) {
                    	// 取得該分類底下所有設備並傳給前端
                        List<Device> deviceList = deviceService.getDevicesByCategoryId(id);
                        request.setAttribute("category", category);
                        request.setAttribute("deviceList", deviceList);
                        request.getRequestDispatcher("/DevicePage/deviceListByCategory.jsp").forward(request, response);
                        return;
                    } else {
                    	// 查無分類時顯示錯誤
                        request.setAttribute("errorMessage", "找不到分類");
                        request.getRequestDispatcher("/DevicePage/error.jsp").forward(request, response);
                        return;
                    }
                }

            } else {
            	// 預設：列出所有分類（未指定 action）
                List<DeviceCategory> list = categoryService.getAllCategories();
                request.setAttribute("categoryList", list);
                request.getRequestDispatcher("/DevicePage/categoryList.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "分類處理失敗：" + e.getMessage());
            request.getRequestDispatcher("/DevicePage/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
