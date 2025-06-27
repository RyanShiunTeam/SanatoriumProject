package caregiver.controller;

import caregiver.bean.Caregiver;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 照服員讀取操作控制器
 * 處理列表、搜尋、查看詳細資料等讀取操作
 */
@WebServlet("/caregiver/read/*")
public class CaregiverReadServlet extends BaseCaregiverServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // 顯示照服員列表頁面
            listCaregivers(request, response);
        } else if (pathInfo.equals("/view")) {
            // 顯示照服員詳細資料
            viewCaregiver(request, response);
        } else if (pathInfo.equals("/search")) {
            // 搜尋照服員
            searchCaregivers(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    /**
     * 列出所有照服員
     */
    private void listCaregivers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            List<Caregiver> caregivers = caregiverService.getAllCaregivers();
            request.setAttribute("caregivers", caregivers);
            request.getRequestDispatcher("/CaregiverPage/list.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "獲取照服員列表時發生錯誤：" + e.getMessage());
            request.getRequestDispatcher("/CaregiverPage/error.jsp")
                    .forward(request, response);
        }
    }
    
    /**
     * 搜尋照服員
     */
    private void searchCaregivers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String searchName = request.getParameter("searchName");
            List<Caregiver> caregivers;
            
            if (searchName != null && !searchName.trim().isEmpty()) {
                caregivers = caregiverService.getCaregiversByName(searchName);
            } else {
                caregivers = caregiverService.getAllCaregivers();
            }
            
            request.setAttribute("caregivers", caregivers);
            request.setAttribute("searchName", searchName);
            request.getRequestDispatcher("/CaregiverPage/list.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "搜尋照服員時發生錯誤：" + e.getMessage());
            request.getRequestDispatcher("/CaregiverPage/error.jsp")
                    .forward(request, response);
        }
    }
    
    /**
     * 查看照服員詳細資料
     */
    private void viewCaregiver(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/caregiver/read/");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            Caregiver caregiver = caregiverService.getCaregiverById(id);
            
            if (caregiver == null) {
                request.setAttribute("error", "找不到指定的照服員");
                request.getRequestDispatcher("/CaregiverPage/error.jsp")
                        .forward(request, response);
                return;
            }
            
            request.setAttribute("caregiver", caregiver);
            request.getRequestDispatcher("/CaregiverPage/view.jsp")
                    .forward(request, response);
                    
        } catch (NumberFormatException e) {
            request.setAttribute("error", "無效的照服員編號");
            request.getRequestDispatcher("/CaregiverPage/error.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "查看照服員詳細資料時發生錯誤：" + e.getMessage());
            request.getRequestDispatcher("/CaregiverPage/error.jsp")
                    .forward(request, response);
        }
    }
}