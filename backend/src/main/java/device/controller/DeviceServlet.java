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
import utils.EmpService;

import java.io.IOException;
import java.util.List;

@WebServlet("/DeviceServlet")
public class DeviceServlet extends HttpServlet {

    private final DeviceService deviceService = DeviceService.getInstance();
    private final DeviceCategoryService categoryService = DeviceCategoryService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	// 取得前端傳來的 action 參數，決定執行哪個功能
        String action = request.getParameter("action");

        try {
        	 // 批次刪除設備
            if ("deleteBatch".equals(action)) {
                String[] ids = request.getParameterValues("id");
                if (ids != null) {
                    for (String id : ids) {
                        deviceService.deleteDevice(id);
                    }
                    // 記錄操作
            		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
            		if (loginUserId != null) {
            			new EmpService().record(loginUserId, "刪除一堆輔具", null);
            		}
                }
                response.sendRedirect(request.getContextPath() + "/DevicePage/deviceList.jsp?status=deleted");

             //  單筆刪除設備
            } else if ("deleteSingle".equals(action)) {
                String id = request.getParameter("id");
                if (id != null && !id.isEmpty()) {
                    deviceService.deleteDevice(id);
                    // 記錄操作
            		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
            		if (loginUserId != null) {
            			new EmpService().record(loginUserId, "刪除輔具", Integer.parseInt(id));
            		}
                }
                response.sendRedirect(request.getContextPath() + "/DevicePage/deviceList.jsp?status=deleted");

             // 模糊搜尋設備名稱
            } else if ("searchByName".equals(action)) {
                String name = request.getParameter("name");
                List<Device> result = deviceService.searchDevicesByName(name);
                request.setAttribute("deviceList", result);
                request.getRequestDispatcher("/DevicePage/deviceList.jsp").forward(request, response);

             // 編輯設備前，先查詢原始資料並轉交給 editDevice.jsp
            } else if ("edit".equals(action)) {
                String id = request.getParameter("id");
                if (id != null && !id.isEmpty()) {
                    Device device = deviceService.getDeviceById(id);
                    if (device != null) {
                        request.setAttribute("device", device);
                     // 查詢所有分類，讓下拉選單可選擇
                        List<DeviceCategory> categoryList = categoryService.getAllCategories();
                        request.setAttribute("categoryList", categoryList);

                        // 傳入前一頁 URL（供返回按鈕使用）
                        String referer = request.getHeader("Referer");
                        if (referer != null) {
                            request.setAttribute("prevPage", referer);
                        }

                        request.getRequestDispatcher("/DevicePage/editDevice.jsp").forward(request, response);
                    } else {
                        request.setAttribute("errorMessage", "找不到設備資料！");
                        request.getRequestDispatcher("/DevicePage/error.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("errorMessage", "未提供設備 ID！");
                    request.getRequestDispatcher("/DevicePage/error.jsp").forward(request, response);
                }

             // ⑤前往新增設備頁面
            } else if ("toAdd".equals(action)) {
                List<DeviceCategory> categoryList = categoryService.getAllCategories();
                request.setAttribute("categoryList", categoryList);
                request.getRequestDispatcher("/DevicePage/addDevice.jsp").forward(request, response);

                // 查詢特定分類下的設備
            } else if ("byCategory".equals(action)) {
                String categoryIdStr = request.getParameter("categoryId");
                if (categoryIdStr != null && !categoryIdStr.isBlank()) {
                    int categoryId = Integer.parseInt(categoryIdStr);
                    List<Device> list = deviceService.getDevicesByCategoryId(categoryId);
                    request.setAttribute("deviceList", list);
                    request.setAttribute("categoryId", categoryId);
                    request.getRequestDispatcher("/DevicePage/deviceList.jsp").forward(request, response);
                    return;
                }
                response.sendRedirect(request.getContextPath() + "/DeviceServlet");

             // 預設顯示設備清單（含分頁與排序功能）
            } else {
                // 分頁查詢邏輯
                String sort = request.getParameter("sort");
                String order = request.getParameter("order");
                String pageStr = request.getParameter("page");

                if (sort == null) sort = "name";    // 預設排序欄位
                if (order == null) order = "asc";   // 預設排序方向

                int page = 1;
                int pageSize = 10;  // 每頁顯示筆數
                if (pageStr != null && !pageStr.isBlank()) {
                    page = Integer.parseInt(pageStr);
                }

                List<Device> list = deviceService.getDevicesByPage(page, pageSize);
                int totalCount = deviceService.getDeviceCount();
                int totalPages = (int) Math.ceil((double) totalCount / pageSize);

                request.setAttribute("deviceList", list);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("sort", sort);
                request.setAttribute("order", order);

                request.getRequestDispatcher("/DevicePage/deviceList.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "處理請求時發生錯誤：" + e.getMessage());
            request.getRequestDispatcher("/DevicePage/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
