package caregiver.controller;

import caregiver.service.CaregiverService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

/**
 * 照服員控制器基類
 * 通用版本 - 適用於不同開發者和部署環境
 * 支援多種專案結構和配置方式
 */
@MultipartConfig(
    location = "",                        
    fileSizeThreshold = 1024 * 1024 * 1,  // 1MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 15     // 15MB
)
public abstract class BaseCaregiverServlet extends HttpServlet {
    
    protected CaregiverService caregiverService;
    
    // 系統設定
    private Properties systemConfig;
    
    // 允許的圖片類型
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    private static final List<String> ALLOWED_EXTENSIONS = List.of(
        ".jpg", ".jpeg", ".png", ".gif", ".webp"
    );
    
    @Override
    public void init() throws ServletException {
        System.out.println("=== BaseCaregiverServlet 初始化 (通用版本) ===");
        
        // 載入系統設定
        loadSystemConfig();
        
        // 初始化服務
        caregiverService = new CaregiverService();
        System.out.println("CaregiverService 初始化完成");
        
        // 初始化上傳目錄
        try {
            initializeUploadDirectories();
        } catch (Exception e) {
            System.err.println("初始化上傳目錄失敗: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 載入系統設定
     */
    private void loadSystemConfig() {
        systemConfig = new Properties();
        
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                systemConfig.load(is);
                System.out.println("✅ 已載入 config.properties 設定檔");
            } else {
                System.out.println("⚠️ 未找到 config.properties，使用預設設定");
                setDefaultConfig();
            }
        } catch (IOException e) {
            System.err.println("載入設定檔失敗: " + e.getMessage());
            setDefaultConfig();
        }
    }
    
    /**
     * 設定預設配置
     */
    private void setDefaultConfig() {
        systemConfig.setProperty("upload.max.file.size", "10485760"); // 10MB
        systemConfig.setProperty("upload.max.request.size", "15728640"); // 15MB
        systemConfig.setProperty("project.environment", "development");
        systemConfig.setProperty("upload.sync.source", "true");
    }
    
    /**
     * 智能偵測並初始化上傳目錄
     */
    private void initializeUploadDirectories() {
        // 部署目錄（運行時使用）
        String deployUploadDir = getServletContext().getRealPath("/CaregiverPage/uploads/photos");
        
        // 嘗試智能偵測源碼目錄
        String sourceUploadDir = detectSourceDirectory();
        
        System.out.println("🚀 部署上傳目錄: " + deployUploadDir);
        System.out.println("💾 源碼上傳目錄: " + (sourceUploadDir != null ? sourceUploadDir : "未偵測到"));
        
        // 建立部署目錄
        createDirectory(deployUploadDir);
        
        // 建立源碼目錄（如果偵測到且設定允許同步）
        if (sourceUploadDir != null && isSourceSyncEnabled()) {
            createDirectory(sourceUploadDir);
        }
    }
    
    /**
     * 檢查是否啟用源碼同步
     */
    private boolean isSourceSyncEnabled() {
        String environment = systemConfig.getProperty("project.environment", "development");
        String syncEnabled = systemConfig.getProperty("upload.sync.source", "true"); // 預設啟用
        
        // 在開發環境預設啟用同步
        return "development".equals(environment) && "true".equals(syncEnabled);
    }
    
    /**
     * 智能偵測源碼目錄 - 通用版本
     */
    private String detectSourceDirectory() {
        try {
            String deployPath = getServletContext().getRealPath("/");
            String contextPath = getServletContext().getContextPath();
            
            // 移除開頭的斜線獲取專案名稱
            String projectName = contextPath.startsWith("/") ? contextPath.substring(1) : contextPath;
            if (projectName.isEmpty()) {
                projectName = "ROOT"; // 預設專案名稱
            }
            
            System.out.println("🔍 專案名稱: " + projectName);
            System.out.println("🔍 部署路徑: " + deployPath);
            System.out.println("🔍 Context路徑: " + contextPath);
            System.out.println("🔍 當前工作目錄: " + System.getProperty("user.dir"));
            System.out.println("🔍 用戶目錄: " + System.getProperty("user.home"));
            
            // 多種可能的源碼路徑模式
            List<String> possiblePatterns = Arrays.asList(
                // Maven 標準結構
                "src/main/webapp/CaregiverPage/uploads/photos",
                "backend/src/main/webapp/CaregiverPage/uploads/photos",
                // Eclipse 專案結構  
                "WebContent/CaregiverPage/uploads/photos",
                // 一般 Web 專案結構
                "webapp/CaregiverPage/uploads/photos",
                "web/CaregiverPage/uploads/photos"
            );
            
            // 多種可能的根目錄
            List<String> possibleRoots = new java.util.ArrayList<>(Arrays.asList(
                // 當前工作目錄
                System.getProperty("user.dir"),
                // 您的具體路徑
                "D:/teamspace/" + projectName,
                "D:/teamspace/" + projectName + "/backend",
                // 其他常見的 teamspace 位置
                "C:/teamspace/" + projectName,
                "C:/teamspace/" + projectName + "/backend",
                // 標準工作空間位置
                System.getProperty("user.home") + "/workspace/" + projectName,
                System.getProperty("user.home") + "/eclipse-workspace/" + projectName,
                System.getProperty("user.home") + "/Documents/workspace/" + projectName,
                // Windows 常見位置
                "C:/workspace/" + projectName,
                "D:/workspace/" + projectName,
                "C:/Users/" + System.getProperty("user.name") + "/workspace/" + projectName,
                "D:/Users/" + System.getProperty("user.name") + "/workspace/" + projectName,
                // 其他可能位置
                "C:/projects/" + projectName,
                "D:/projects/" + projectName,
                "C:/dev/" + projectName,
                "D:/dev/" + projectName,
                // 特殊情況：如果專案在子目錄
                System.getProperty("user.dir") + "/" + projectName,
                System.getProperty("user.dir") + "/backend",
                // 從部署路徑推算的 teamspace 路徑
                "D:/teamspace/SanatoriumProject",
                "D:/teamspace/SanatoriumProject/backend",
                "C:/teamspace/SanatoriumProject",
                "C:/teamspace/SanatoriumProject/backend"
            ));
            
            // 如果是從部署路徑推算
            if (deployPath != null) {
                possibleRoots.addAll(extractPossibleRootsFromDeployPath(deployPath, projectName));
            }
            
            System.out.println("🔍 開始測試 " + possibleRoots.size() + " 個根目錄 × " + possiblePatterns.size() + " 個模式");
            
            // 測試所有可能的組合
            for (String root : possibleRoots) {
                for (String pattern : possiblePatterns) {
                    String testPath = root + "/" + pattern;
                    testPath = testPath.replace("\\", "/").replaceAll("/+", "/");
                    
                    File testDir = new File(testPath);
                    File parentDir = testDir.getParentFile(); // uploads 目錄
                    
                    System.out.println("🔍 測試路徑: " + testPath + " (父目錄存在: " + (parentDir != null && parentDir.exists()) + ")");
                    
                    if (parentDir != null && parentDir.exists()) {
                        System.out.println("✅ 找到源碼路徑: " + testPath);
                        return testPath;
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ 源碼目錄偵測失敗: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("❌ 無法偵測到源碼目錄");
        return null;
    }
    
    /**
     * 從部署路徑推算可能的源碼根目錄
     */
    private List<String> extractPossibleRootsFromDeployPath(String deployPath, String projectName) {
        List<String> roots = new java.util.ArrayList<>();
        
        try {
            // 標準化路徑
            deployPath = deployPath.replace("\\", "/");
            
            // Eclipse 環境 (.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps)
            if (deployPath.contains(".metadata")) {
                int metadataIndex = deployPath.indexOf(".metadata");
                String workspace = deployPath.substring(0, metadataIndex);
                roots.add(workspace + projectName);
                roots.add(workspace + projectName + "/backend");
            }
            
            // Tomcat webapps 目錄
            if (deployPath.contains("webapps")) {
                int webappsIndex = deployPath.indexOf("webapps");
                String tomcatPath = deployPath.substring(0, webappsIndex);
                String parentPath = new File(tomcatPath).getParent();
                if (parentPath != null) {
                    roots.add(parentPath + "/" + projectName);
                    roots.add(parentPath + "/workspace/" + projectName);
                }
            }
            
            // IntelliJ IDEA 環境
            if (deployPath.contains("target")) {
                int targetIndex = deployPath.indexOf("target");
                String projectRoot = deployPath.substring(0, targetIndex);
                roots.add(projectRoot);
            }
            
        } catch (Exception e) {
            System.out.println("推算源碼路徑時發生錯誤: " + e.getMessage());
        }
        
        return roots;
    }
    
    /**
     * 建立目錄
     */
    private void createDirectory(String dirPath) {
        try {
            if (dirPath == null || dirPath.trim().isEmpty()) {
                return;
            }
            
            File dir = new File(dirPath);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                System.out.println("建立目錄 " + dirPath + ": " + (created ? "✅ 成功" : "❌ 失敗"));
                
                if (!created) {
                    // 嘗試檢查權限問題
                    File parent = dir.getParentFile();
                    if (parent != null && !parent.exists()) {
                        System.out.println("父目錄不存在: " + parent.getAbsolutePath());
                    } else if (parent != null && !parent.canWrite()) {
                        System.out.println("父目錄無寫入權限: " + parent.getAbsolutePath());
                    }
                }
            } else {
                System.out.println("目錄已存在: " + dirPath);
            }
        } catch (Exception e) {
            System.err.println("建立目錄失敗 " + dirPath + ": " + e.getMessage());
        }
    }
    
    /**
     * 處理照片上傳
     */
    protected String handlePhotoUpload(HttpServletRequest request) throws IOException, ServletException {
        System.out.println("=== 開始處理照片上傳 (通用版本) ===");
        
        try {
            Part photoPart = request.getPart("photo");
            
            if (photoPart == null || photoPart.getSize() == 0) {
                System.out.println("沒有上傳照片");
                return null;
            }
            
            String fileName = photoPart.getSubmittedFileName();
            System.out.println("上傳的檔案名: " + fileName);
            
            if (fileName == null || fileName.trim().isEmpty()) {
                return null;
            }
            
            // 驗證檔案
            validateUploadedFile(photoPart, fileName);
            
            // 產生唯一檔名
            String uniqueFileName = generateUniqueFileName(fileName);
            System.out.println("產生的唯一檔名: " + uniqueFileName);
            
            // 取得上傳目錄
            String uploadDir = getServletContext().getRealPath("/CaregiverPage/uploads/photos");
            
            // 確保目錄存在
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }
            
            // 儲存檔案
            String filePath = uploadDir + File.separator + uniqueFileName;
            System.out.println("儲存到: " + filePath);
            
            // 使用 Part.write() 方法儲存
            photoPart.write(filePath);
            
            // 驗證檔案是否成功儲存
            File savedFile = new File(filePath);
            if (!savedFile.exists() || savedFile.length() == 0) {
                throw new IOException("檔案儲存失敗");
            }
            
            System.out.println("✅ 檔案儲存成功，大小: " + savedFile.length() + " bytes");
            
            // 同步到源碼目錄（開發環境）
            if (isSourceSyncEnabled()) {
                syncToSourceDirectory(savedFile, uniqueFileName);
            }
            
            // 回傳相對路徑
            String relativePath = "/CaregiverPage/uploads/photos/" + uniqueFileName;
            System.out.println("🔗 資料庫儲存路徑: " + relativePath);
            return relativePath;
            
        } catch (Exception e) {
            System.err.println("❌ 照片上傳失敗: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 驗證上傳的檔案
     */
    private void validateUploadedFile(Part photoPart, String fileName) {
        // 驗證檔案類型
        String contentType = photoPart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("只能上傳圖片檔案！");
        }
        
        // 從設定檔讀取檔案大小限制
        long maxFileSize = Long.parseLong(systemConfig.getProperty("upload.max.file.size", "10485760"));
        if (photoPart.getSize() > maxFileSize) {
            throw new IllegalArgumentException("檔案大小不能超過 " + (maxFileSize / 1024 / 1024) + "MB！");
        }
        
        // 獲取檔案副檔名
        String fileExtension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            fileExtension = fileName.substring(dotIndex).toLowerCase();
        }
        
        // 驗證副檔名
        if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
            throw new IllegalArgumentException("不支援的檔案格式！支援格式：" + String.join(", ", ALLOWED_EXTENSIONS));
        }
    }
    
    /**
     * 產生唯一檔名
     */
    private String generateUniqueFileName(String originalFileName) {
        String fileExtension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0) {
            fileExtension = originalFileName.substring(dotIndex).toLowerCase();
        }
        
        return UUID.randomUUID().toString() + fileExtension;
    }
    
    /**
     * 同步檔案到源碼目錄（開發環境用）
     */
    private void syncToSourceDirectory(File deployedFile, String fileName) {
        try {
            String sourceUploadDir = detectSourceDirectory();
            
            if (sourceUploadDir == null) {
                System.out.println("ℹ️ 未偵測到源碼目錄，跳過同步");
                return;
            }
            
            String sourcePath = sourceUploadDir + "/" + fileName;
            System.out.println("🎯 同步到源碼路徑: " + sourcePath);
            
            Path sourceDir = Paths.get(sourcePath).getParent();
            if (!Files.exists(sourceDir)) {
                Files.createDirectories(sourceDir);
                System.out.println("📁 建立源碼目錄: " + sourceDir);
            }
            
            // 複製檔案到源碼目錄
            Files.copy(deployedFile.toPath(), Paths.get(sourcePath), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("📋 已同步到源碼目錄: " + sourcePath);
            
            // 驗證檔案是否成功複製
            File sourceFile = new File(sourcePath);
            if (sourceFile.exists()) {
                System.out.println("✅ 源碼目錄檔案驗證成功，大小: " + sourceFile.length() + " bytes");
            } else {
                System.out.println("❌ 源碼目錄檔案驗證失敗");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ 同步到源碼目錄失敗（不影響功能）: " + e.getMessage());
        }
    }
    
    /**
     * 刪除照片檔案
     */
    protected void deletePhotoFile(String photoPath) {
        if (photoPath == null || !photoPath.startsWith("/CaregiverPage/uploads/photos/")) {
            return;
        }
        
        try {
            // 刪除部署目錄的檔案
            String realPath = getServletContext().getRealPath(photoPath);
            deleteFile(realPath, "部署目錄");
            
            // 如果啟用源碼同步，也刪除源碼目錄的檔案
            if (isSourceSyncEnabled()) {
                String sourceUploadDir = detectSourceDirectory();
                if (sourceUploadDir != null) {
                    String fileName = photoPath.substring(photoPath.lastIndexOf("/") + 1);
                    String sourcePath = sourceUploadDir + "/" + fileName;
                    deleteFile(sourcePath, "源碼目錄");
                }
            }
            
        } catch (Exception e) {
            System.err.println("刪除照片檔案時發生錯誤：" + e.getMessage());
        }
    }
    
    /**
     * 刪除檔案的輔助方法
     */
    private void deleteFile(String filePath, String description) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                boolean deleted = file.delete();
                System.out.println("刪除" + description + "照片: " + filePath + " - " + (deleted ? "✅ 成功" : "❌ 失敗"));
            } else {
                System.out.println(description + "照片檔案不存在: " + filePath);
            }
        } catch (Exception e) {
            System.err.println("刪除" + description + "檔案失敗: " + e.getMessage());
        }
    }
    
    /**
     * 驗證照服員基本資料
     */
    protected void validateCaregiverData(String chineseName, String phone, String email, 
                                       String experienceYearsStr, String genderStr) {
        System.out.println("=== 驗證照服員資料 ===");
        
        // 檢查必填欄位
        if (isNullOrEmpty(chineseName) || isNullOrEmpty(phone) || 
            isNullOrEmpty(email) || isNullOrEmpty(experienceYearsStr) || 
            isNullOrEmpty(genderStr)) {
            throw new IllegalArgumentException("請填寫所有必填欄位");
        }
        
        // 驗證姓名長度
        if (chineseName.trim().length() > 50) {
            throw new IllegalArgumentException("姓名長度不能超過 50 個字元");
        }
        
        // 驗證電話格式（更嚴格的驗證）
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)\\+]", "");
        if (!cleanPhone.matches("^[0-9]{8,15}$")) {
            throw new IllegalArgumentException("電話號碼格式不正確（應為 8-15 位數字）");
        }
        
        // 驗證信箱格式
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("電子信箱格式不正確");
        }
        
        // 驗證信箱長度
        if (email.length() > 100) {
            throw new IllegalArgumentException("電子信箱長度不能超過 100 個字元");
        }
        
        // 驗證年資
        try {
            int years = Integer.parseInt(experienceYearsStr.trim());
            if (years < 0 || years > 50) {
                throw new IllegalArgumentException("工作年資必須在 0-50 年之間");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("工作年資必須是有效的數字");
        }
        
        // 驗證性別
        if (!"true".equals(genderStr) && !"false".equals(genderStr)) {
            throw new IllegalArgumentException("性別選項無效");
        }
        
        System.out.println("✅ 資料驗證通過");
    }
    
    /**
     * 檢查字串是否為空
     */
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * 獲取系統設定值
     */
    protected String getConfigValue(String key, String defaultValue) {
        return systemConfig.getProperty(key, defaultValue);
    }
    
    /**
     * 獲取系統設定值（整數）
     */
    protected int getConfigValueAsInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(systemConfig.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            System.err.println("設定值轉換失敗: " + key + " = " + systemConfig.getProperty(key));
            return defaultValue;
        }
    }
    
    @Override
    public void destroy() {
        System.out.println("BaseCaregiverServlet 已銷毀");
        super.destroy();
    }
}