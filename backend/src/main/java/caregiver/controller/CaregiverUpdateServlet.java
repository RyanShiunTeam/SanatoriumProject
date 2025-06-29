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
 * 照服員更新操作控制器
 * 處理照服員的更新功能
 */
@WebServlet("/caregiver/update/*")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,  // 1MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 15     // 15MB
)
public class CaregiverUpdateServlet extends BaseCaregiverServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("=== CaregiverUpdateServlet doGet 被呼叫 ===");
        
        String pathInfo = request.getPathInfo();
        System.out.println("PathInfo: " + pathInfo);
        
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/edit")) {
            // 顯示編輯表單
            editCaregiverForm(request, response);
        } else {
            System.out.println("404 錯誤 - 未知路徑: " + pathInfo);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("=== CaregiverUpdateServlet doPost 被呼叫 ===");
        System.out.println("MultipartConfig 已設定：支援檔案上傳");
        
        String pathInfo = request.getPathInfo();
        System.out.println("PathInfo: " + pathInfo);
        
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/update")) {
            // 更新照服員
            updateCaregiver(request, response);
        } else {
            System.out.println("404 錯誤 - 未知路徑: " + pathInfo);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    /**
     * 顯示編輯表單
     */
    private void editCaregiverForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("=== 顯示編輯表單 ===");
        
        String idParam = request.getParameter("id");
        System.out.println("照服員 ID 參數: " + idParam);
        
        if (idParam == null || idParam.trim().isEmpty()) {
            System.out.println("缺少照服員 ID，重導向到列表");
            response.sendRedirect(request.getContextPath() + "/caregiver/read/");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            System.out.println("解析照服員 ID: " + id);
            
            Caregiver caregiver = caregiverService.getCaregiverById(id);
            
            if (caregiver == null) {
                System.out.println("找不到 ID 為 " + id + " 的照服員");
                request.setAttribute("error", "找不到指定的照服員");
                request.getRequestDispatcher("/CaregiverPage/error.jsp")
                        .forward(request, response);
                return;
            }
            
            System.out.println("找到照服員: " + caregiver.toString());
            request.setAttribute("caregiver", caregiver);
            request.getRequestDispatcher("/CaregiverPage/edit.jsp")
                    .forward(request, response);
                    
        } catch (NumberFormatException e) {
            System.err.println("無效的照服員 ID: " + idParam);
            request.setAttribute("error", "無效的照服員編號");
            request.getRequestDispatcher("/CaregiverPage/error.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            System.err.println("載入編輯表單時發生錯誤: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "載入編輯表單時發生錯誤：" + e.getMessage());
            request.getRequestDispatcher("/CaregiverPage/error.jsp")
                    .forward(request, response);
        }
    }
    
    /**
     * 更新照服員
     */
    private void updateCaregiver(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("=== 開始更新照服員 ===");
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
                String caregiverIdStr = getPartValue(request, "caregiverId");
                String chineseName = getPartValue(request, "chineseName");
                String phone = getPartValue(request, "phone");
                String email = getPartValue(request, "email");
                String experienceYearsStr = getPartValue(request, "experienceYears");
                String genderStr = getPartValue(request, "gender");
                
                System.out.println("從 Parts 獲取的表單參數:");
                System.out.println("  caregiverId: '" + caregiverIdStr + "'");
                System.out.println("  chineseName: '" + chineseName + "'");
                System.out.println("  phone: '" + phone + "'");
                System.out.println("  email: '" + email + "'");
                System.out.println("  experienceYears: '" + experienceYearsStr + "'");
                System.out.println("  gender: '" + genderStr + "'");
                
                // 驗證照服員ID
                if (caregiverIdStr == null || caregiverIdStr.trim().isEmpty()) {
                    System.err.println("缺少照服員編號");
                    throw new IllegalArgumentException("缺少照服員編號");
                }
                
                // 驗證必填欄位
                validateCaregiverData(chineseName, phone, email, experienceYearsStr, genderStr);
                System.out.println("表單驗證通過");
                
                // 建立照服員物件
                Caregiver caregiver = new Caregiver();
                caregiver.setCaregiverId(Integer.parseInt(caregiverIdStr));
                caregiver.setChineseName(chineseName.trim());
                caregiver.setGender(Boolean.parseBoolean(genderStr));
                caregiver.setPhone(phone.trim());
                caregiver.setEmail(email.trim());
                caregiver.setExperienceYears(Integer.parseInt(experienceYearsStr));
                
                System.out.println("照服員物件建立完成: " + caregiver.toString());
                
                // 處理照片更新
                System.out.println("開始處理照片更新");
                String newPhotoPath = handlePhotoUpload(request);
                if (newPhotoPath != null) {
                    // 如果有上傳新照片，使用新照片
                    caregiver.setPhoto(newPhotoPath);
                    System.out.println("使用新照片: " + newPhotoPath);
                } else {
                    // 如果沒有上傳新照片，保持原有照片
                    Caregiver existingCaregiver = caregiverService.getCaregiverById(caregiver.getCaregiverId());
                    caregiver.setPhoto(existingCaregiver != null ? existingCaregiver.getPhoto() : null);
                    System.out.println("保持原有照片: " + caregiver.getPhoto());
                }
                
                // 更新照服員
                System.out.println("開始更新照服員到資料庫");
                boolean success = caregiverService.updateCaregiver(caregiver);
                System.out.println("更新結果: " + success);
                
                if (success) {
                    // 記錄操作
            		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
            		if (success && loginUserId != null) {
            			new EmpService().record(loginUserId, "修改照服員", caregiver.getCaregiverId());
            		}
                	
                    System.out.println("更新成功，準備重導向");
                    request.getSession().setAttribute("message", "照服員資料更新成功！");
                    String redirectUrl = request.getContextPath() + "/caregiver/read/";
                    System.out.println("重導向到: " + redirectUrl);
                    response.sendRedirect(redirectUrl);
                } else {
                    System.out.println("更新失敗");
                    request.getSession().setAttribute("error", "照服員資料更新失敗！");
                    String redirectUrl = request.getContextPath() + "/caregiver/update/edit?id=" + caregiverIdStr;
                    System.out.println("重導向到: " + redirectUrl);
                    response.sendRedirect(redirectUrl);
                }
                
            } else {
                System.err.println("不是 multipart 請求，無法處理檔案上傳");
                request.getSession().setAttribute("error", "表單格式錯誤");
                response.sendRedirect(request.getContextPath() + "/caregiver/read/");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("數字格式錯誤: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "照服員編號或工作年資必須是有效的數字");
            response.sendRedirect(request.getContextPath() + "/caregiver/read/");
        } catch (IllegalArgumentException e) {
            System.err.println("參數錯誤: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/caregiver/read/");
        } catch (Exception e) {
            System.err.println("更新過程發生錯誤: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "更新過程中發生錯誤：" + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/caregiver/read/");
        }
        
        System.out.println("=== 更新照服員流程結束 ===");
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