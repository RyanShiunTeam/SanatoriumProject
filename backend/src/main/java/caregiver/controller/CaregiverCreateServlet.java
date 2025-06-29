package caregiver.controller;

import caregiver.bean.Caregiver;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.EmpService;

import java.io.IOException;

/**
 * 照服員新增操作控制器
 * 處理照服員的新增功能
 * 適用於 SanatoriumProject 專案結構
 */
@WebServlet("/caregiver/create/*")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,  // 1MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 15     // 15MB
)
public class CaregiverCreateServlet extends BaseCaregiverServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("=== CaregiverCreateServlet doGet 被呼叫 ===");
        
        String pathInfo = request.getPathInfo();
        System.out.println("PathInfo: " + pathInfo);
        
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/add")) {
            // 重導向到靜態 HTML 新增頁面 (SanatoriumProject 路徑)
            String redirectUrl = request.getContextPath() + "/CaregiverPage/add.html";
            System.out.println("重導向到: " + redirectUrl);
            response.sendRedirect(redirectUrl);
        } else {
            System.out.println("404 錯誤 - 未知路徑: " + pathInfo);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("=== CaregiverCreateServlet doPost 被呼叫 ===");
        System.out.println("MultipartConfig 已設定：支援檔案上傳");
        
        String pathInfo = request.getPathInfo();
        System.out.println("PathInfo: " + pathInfo);
        
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/add")) {
            // 新增照服員
            addCaregiver(request, response);
        } else {
            System.out.println("404 錯誤 - 未知路徑: " + pathInfo);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    /**
     * 新增照服員
     */
    private void addCaregiver(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("=== 開始新增照服員 (SanatoriumProject) ===");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Content Type: " + request.getContentType());
        System.out.println("Character Encoding: " + request.getCharacterEncoding());
        
        try {
            // 確保請求編碼正確
            if (request.getCharacterEncoding() == null) {
                request.setCharacterEncoding("UTF-8");
                System.out.println("設定請求編碼為 UTF-8");
            }
            
            // 檢查是否是 multipart 請求
            if (request.getContentType() != null && request.getContentType().startsWith("multipart/")) {
                System.out.println("這是 multipart 請求，開始解析...");
                
                // 直接從 parts 中獲取參數值
                String chineseName = getPartValue(request, "chineseName");
                String phone = getPartValue(request, "phone");
                String email = getPartValue(request, "email");
                String experienceYearsStr = getPartValue(request, "experienceYears");
                String genderStr = getPartValue(request, "gender");
                
                System.out.println("從 Parts 獲取的表單參數:");
                System.out.println("  chineseName: '" + chineseName + "'");
                System.out.println("  phone: '" + phone + "'");
                System.out.println("  email: '" + email + "'");
                System.out.println("  experienceYears: '" + experienceYearsStr + "'");
                System.out.println("  gender: '" + genderStr + "'");
                
                // 檢查是否成功獲取參數
                if (chineseName == null && phone == null && email == null && 
                    experienceYearsStr == null && genderStr == null) {
                    
                    System.err.println("警告：無法從 multipart 請求中獲取任何參數");
                    
                    // 列出所有 parts 進行除錯
                    var parts = request.getParts();
                    System.out.println("Parts 數量: " + parts.size());
                    for (Part part : parts) {
                        System.out.println("Part: " + part.getName() + 
                                         ", Size: " + part.getSize() + 
                                         ", ContentType: " + part.getContentType());
                        
                        // 列出所有 headers
                        for (String headerName : part.getHeaderNames()) {
                            System.out.println("  Header: " + headerName + " = " + part.getHeader(headerName));
                        }
                    }
                    
                    request.getSession().setAttribute("error", "表單資料解析失敗，請重新填寫");
                    response.sendRedirect(request.getContextPath() + "/CaregiverPage/add.html");
                    return;
                }
                
                // 驗證必填欄位
                validateCaregiverData(chineseName, phone, email, experienceYearsStr, genderStr);
                System.out.println("表單驗證通過");
                
                // 建立照服員物件
                Caregiver caregiver = new Caregiver();
                caregiver.setChineseName(chineseName.trim());
                caregiver.setGender(Boolean.parseBoolean(genderStr));
                caregiver.setPhone(phone.trim());
                caregiver.setEmail(email.trim());
                caregiver.setExperienceYears(Integer.parseInt(experienceYearsStr));
                
                System.out.println("照服員物件建立完成: " + caregiver.toString());
                
                // 處理照片上傳
                System.out.println("開始處理照片上傳");
                String photoPath = handlePhotoUpload(request);
                caregiver.setPhoto(photoPath);
                System.out.println("照片路徑: " + photoPath);
                
                // 測試資料庫連線
                System.out.println("測試資料庫連線...");
                if (caregiverService == null) {
                    System.err.println("錯誤：caregiverService 為 null");
                    throw new RuntimeException("Service 未初始化");
                }
                
                // 儲存照服員
                System.out.println("開始儲存照服員到資料庫");
                boolean success = caregiverService.addCaregiver(caregiver);
                System.out.println("儲存結果: " + success);
                
                if (success) {
                    System.out.println("新增成功，準備重導向");
                    request.getSession().setAttribute("message", "照服員新增成功！");
                    String redirectUrl = request.getContextPath() + "/caregiver/read/";
                    System.out.println("重導向到: " + redirectUrl);
                    response.sendRedirect(redirectUrl);
                    // 記錄操作
            		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
            		if (success && loginUserId != null) {
            			new EmpService().record(loginUserId, "新增照服員", null);
            		}
                    
                } else {
                    System.out.println("新增失敗");
                    request.getSession().setAttribute("error", "照服員新增失敗！");
                    String redirectUrl = request.getContextPath() + "/CaregiverPage/add.html";
                    System.out.println("重導向到: " + redirectUrl);
                    response.sendRedirect(redirectUrl);
                }
                
            } else {
                System.err.println("不是 multipart 請求，無法處理檔案上傳");
                request.getSession().setAttribute("error", "表單格式錯誤");
                response.sendRedirect(request.getContextPath() + "/CaregiverPage/add.html");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("數字格式錯誤: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "工作年資必須是有效的數字");
            response.sendRedirect(request.getContextPath() + "/CaregiverPage/add.html");
        } catch (IllegalArgumentException e) {
            System.err.println("參數錯誤: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/CaregiverPage/add.html");
        } catch (Exception e) {
            System.err.println("新增過程發生錯誤: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "新增過程中發生錯誤：" + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/CaregiverPage/add.html");
        }
        
        System.out.println("=== 新增照服員流程結束 ===");
    }
    
    /**
     * 從 multipart request 中獲取指定 part 的字符串值
     */
    private String getPartValue(HttpServletRequest request, String partName) {
        try {
            Part part = request.getPart(partName);
            if (part == null) {
                System.out.println("Part '" + partName + "' 不存在");
                return null;
            }
            
            System.out.println("處理 Part: " + partName + ", Size: " + part.getSize());
            
            if (part.getSize() == 0) {
                System.out.println("Part '" + partName + "' 大小為 0，返回空字串");
                return "";
            }
            
            // 讀取 part 內容
            byte[] bytes = part.getInputStream().readAllBytes();
            String value = new String(bytes, "UTF-8").trim();
            
            System.out.println("Part '" + partName + "' 的值: '" + value + "'");
            return value;
            
        } catch (Exception e) {
            System.err.println("讀取 Part '" + partName + "' 時發生錯誤: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}