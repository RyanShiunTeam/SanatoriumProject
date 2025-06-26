package caregiver.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 編碼過濾器 - 處理中文編碼問題
 * 確保所有請求和回應都使用 UTF-8 編碼
 */
@WebFilter(filterName = "EncodingFilter", urlPatterns = {"/*"})
public class EncodingFilter implements Filter {

    private String encoding = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 從 web.xml 或註解中獲取編碼設定，如果沒有設定則使用預設的 UTF-8
        String encodingParam = filterConfig.getInitParameter("encoding");
        if (encodingParam != null && !encodingParam.trim().isEmpty()) {
            this.encoding = encodingParam;
        }
        
        // 記錄過濾器初始化
        System.out.println("EncodingFilter 初始化完成，使用編碼：" + this.encoding);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // 設定請求編碼
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding);
        }
        
        // 設定回應編碼
        response.setCharacterEncoding(encoding);
        response.setContentType("text/html; charset=" + encoding);
        
        // 設定回應標頭，防止快取問題
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);
        
        try {
            // 繼續執行過濾器鏈
            chain.doFilter(request, response);
        } catch (Exception e) {
            // 記錄錯誤
            System.err.println("EncodingFilter 處理請求時發生錯誤：" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void destroy() {
        System.out.println("EncodingFilter 已銷毀");
    }
}