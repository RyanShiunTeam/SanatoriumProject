package caregiver.controller;

import caregiver.bean.Caregiver;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 照服員刪除操作控制器
 * 處理照服員的刪除功能
 */
@WebServlet("/caregiver/delete/*")
public class CaregiverDeleteServlet extends BaseCaregiverServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/confirm")) {
            // 顯示刪除確認頁面
            showDeleteConfirmation(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/delete")) {
            // 執行刪除操作
            deleteCaregiver(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    /**
     * 顯示刪除確認頁面
     */
    private void showDeleteConfirmation(HttpServletRequest request, HttpServletResponse response) 
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
            request.getRequestDispatcher("/CaregiverPage/delete-confirm.jsp")
                    .forward(request, response);
                    
        } catch (NumberFormatException e) {
            request.setAttribute("error", "無效的照服員編號");
            request.getRequestDispatcher("/CaregiverPage/error.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "載入確認頁面時發生錯誤：" + e.getMessage());
            request.getRequestDispatcher("/CaregiverPage/error.jsp")
                    .forward(request, response);
        }
    }
    
    /**
     * 刪除照服員
     */
    private void deleteCaregiver(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                throw new IllegalArgumentException("缺少照服員編號");
            }
            
            int id = Integer.parseInt(idParam);
            
            // 獲取照服員資訊（用於刪除照片檔案）
            Caregiver caregiver = caregiverService.getCaregiverById(id);
            
            if (caregiver == null) {
                request.getSession().setAttribute("error", "找不到指定的照服員");
                response.sendRedirect(request.getContextPath() + "/caregiver/read/");
                return;
            }
            
            // 刪除照服員
            boolean success = caregiverService.deleteCaregiverById(id);
            
            if (success) {
                // 如果刪除成功且有照片，則刪除照片檔案
                if (caregiver.getPhoto() != null && !caregiver.getPhoto().trim().isEmpty()) {
                    deletePhotoFile(caregiver.getPhoto());
                }
                request.getSession().setAttribute("message", "照服員刪除成功！");
            } else {
                request.getSession().setAttribute("error", "照服員刪除失敗！");
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "無效的照服員編號");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "刪除過程中發生錯誤：" + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/caregiver/read/");
    }
}