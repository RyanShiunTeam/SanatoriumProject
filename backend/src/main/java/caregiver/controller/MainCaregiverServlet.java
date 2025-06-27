package caregiver.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 照服員主控制器
 * 負責路由分發到各個 CRUD 操作控制器
 * 適用於 SanatoriumProject 專案結構
 */
@WebServlet("/caregiver/*")
public class MainCaregiverServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        System.out.println("MainCaregiverServlet doGet - PathInfo: " + pathInfo);
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // 預設顯示列表頁面作為主頁 - 轉發到讀取控制器
            request.getRequestDispatcher("/caregiver/read/").forward(request, response);
            
        } else if (pathInfo.equals("/add")) {
            // 新增頁面 - 重導向到靜態 HTML
            String redirectUrl = request.getContextPath() + "/CaregiverPage/add.html";
            System.out.println("重導向到新增頁面: " + redirectUrl);
            response.sendRedirect(redirectUrl);
            
        } else if (pathInfo.equals("/edit")) {
            // 編輯頁面 - 轉發到更新控制器
            String queryString = request.getQueryString();
            String forwardPath = "/caregiver/update/edit" + (queryString != null ? "?" + queryString : "");
            System.out.println("轉發到編輯頁面: " + forwardPath);
            request.getRequestDispatcher(forwardPath).forward(request, response);
            
        } else if (pathInfo.equals("/view")) {
            // 查看詳細資料 - 轉發到讀取控制器
            String queryString = request.getQueryString();
            String forwardPath = "/caregiver/read/view" + (queryString != null ? "?" + queryString : "");
            System.out.println("轉發到查看頁面: " + forwardPath);
            request.getRequestDispatcher(forwardPath).forward(request, response);
            
        } else if (pathInfo.equals("/search")) {
            // 搜尋功能 - 轉發到讀取控制器
            String queryString = request.getQueryString();
            String forwardPath = "/caregiver/read/search" + (queryString != null ? "?" + queryString : "");
            System.out.println("轉發到搜尋功能: " + forwardPath);
            request.getRequestDispatcher(forwardPath).forward(request, response);
            
        } else if (pathInfo.equals("/delete-confirm")) {
            // 刪除確認頁面 - 轉發到刪除控制器
            String queryString = request.getQueryString();
            String forwardPath = "/caregiver/delete/confirm" + (queryString != null ? "?" + queryString : "");
            System.out.println("轉發到刪除確認頁面: " + forwardPath);
            request.getRequestDispatcher(forwardPath).forward(request, response);
            
        } else {
            System.out.println("未知的 GET 路徑: " + pathInfo);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        System.out.println("MainCaregiverServlet doPost - PathInfo: " + pathInfo);
        
        if (pathInfo.equals("/add")) {
            // 新增照服員 - 轉發到新增控制器
            System.out.println("轉發到新增控制器");
            request.getRequestDispatcher("/caregiver/create/add").forward(request, response);
            
        } else if (pathInfo.equals("/update")) {
            // 更新照服員 - 轉發到更新控制器
            System.out.println("轉發到更新控制器");
            request.getRequestDispatcher("/caregiver/update/update").forward(request, response);
            
        } else if (pathInfo.equals("/delete")) {
            // 刪除照服員 - 轉發到刪除控制器
            System.out.println("轉發到刪除控制器");
            request.getRequestDispatcher("/caregiver/delete/delete").forward(request, response);
            
        } else {
            System.out.println("未知的 POST 路徑: " + pathInfo);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}