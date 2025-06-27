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
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

/**
 * 照服員控制器基類 - 簡化版
 */
@MultipartConfig(
    location = "",                        
    fileSizeThreshold = 1024 * 1024 * 1,  // 1MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 15     // 15MB
)
public abstract class BaseCaregiverServlet extends HttpServlet {
    
    protected CaregiverService caregiverService;
    private Properties systemConfig;
    
    // 允許的圖片類型
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
        ".jpg", ".jpeg", ".png", ".gif", ".webp"
    );
    
    // 快取已找到的 SanatoriumProject 路徑
    private static String cachedSanatoriumPath = null;
    
    @Override
    public void init() throws ServletException {
        System.out.println("=== BaseCaregiverServlet 初始化 (簡化版) ===");
        
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
     * 初始化上傳目錄 - 簡化版
     */
    private void initializeUploadDirectories() {
        // 部署目錄（運行時使用）
        String deployUploadDir = getServletContext().getRealPath("/CaregiverPage/uploads/photos");
        System.out.println("🚀 部署上傳目錄: " + deployUploadDir);
        createDirectory(deployUploadDir);
        
        // 找到 SanatoriumProject 並建立源碼目錄
        String sourceUploadDir = findSanatoriumProjectUploadDir();
        if (sourceUploadDir != null) {
            createDirectory(sourceUploadDir);
            System.out.println("💾 源碼上傳目錄: " + sourceUploadDir);
        } else {
            System.out.println("⚠️ 未找到 SanatoriumProject 目錄");
        }
    }
    
    /**
     * 尋找 SanatoriumProject 專案目錄並返回 uploads/photos 完整路徑
     */
    private String findSanatoriumProjectUploadDir() {
        // 如果已經快取過，直接使用
        if (cachedSanatoriumPath != null) {
            System.out.println("✅ 使用快取的 SanatoriumProject 路徑: " + cachedSanatoriumPath);
            return cachedSanatoriumPath;
        }
        
        System.out.println("🔍 開始尋找 SanatoriumProject 目錄...");
        
        // 獲取所有磁碟機
        File[] drives = File.listRoots();
        System.out.println("🔍 搜尋磁碟機: " + java.util.Arrays.toString(drives));
        
        for (File drive : drives) {
            System.out.println("🔍 搜尋磁碟機: " + drive.getAbsolutePath());
            
            try {
                String foundPath = searchSanatoriumProject(drive, 0, 4); // 最多搜尋4層深度
                if (foundPath != null) {
                    cachedSanatoriumPath = foundPath; // 快取路徑
                    System.out.println("✅ 找到 SanatoriumProject 上傳目錄: " + foundPath);
                    return foundPath;
                }
            } catch (Exception e) {
                System.out.println("⚠️ 搜尋磁碟機 " + drive.getAbsolutePath() + " 時發生錯誤: " + e.getMessage());
            }
        }
        
        System.out.println("❌ 未找到 SanatoriumProject 目錄");
        return null;
    }
    
    /**
     * 遞迴搜尋 SanatoriumProject 目錄
     */
    private String searchSanatoriumProject(File currentDir, int currentDepth, int maxDepth) {
        // 防止搜尋太深
        if (currentDepth > maxDepth) {
            return null;
        }
        
        try {
            // 檢查當前目錄是否可存取
            if (!currentDir.exists() || !currentDir.isDirectory() || !currentDir.canRead()) {
                return null;
            }
            
            // 跳過系統目錄和隱藏目錄
            String dirName = currentDir.getName().toLowerCase();
            if (shouldSkipDirectory(dirName)) {
                return null;
            }
            
            // 檢查當前目錄是否是 SanatoriumProject
            if (currentDir.getName().equals("SanatoriumProject")) {
                System.out.println("🎯 找到 SanatoriumProject: " + currentDir.getAbsolutePath());
                
                // 檢查是否有正確的專案結構並返回 uploads/photos 完整路徑
                String[] possiblePaths = {
                    // Maven 標準結構
                    currentDir.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "CaregiverPage" + File.separator + "uploads" + File.separator + "photos",
                    // Eclipse Web 專案結構  
                    currentDir.getAbsolutePath() + File.separator + "WebContent" + File.separator + "CaregiverPage" + File.separator + "uploads" + File.separator + "photos",
                    // 簡化結構
                    currentDir.getAbsolutePath() + File.separator + "CaregiverPage" + File.separator + "uploads" + File.separator + "photos"
                };
                
                for (String path : possiblePaths) {
                    File testDir = new File(path);
                    File caregiverPageDir = new File(currentDir.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "CaregiverPage");
                    
                    // 檢查 CaregiverPage 目錄是否存在，如果存在就使用這個路徑
                    if (caregiverPageDir.exists() || testDir.getParentFile().getParentFile().exists()) {
                        System.out.println("✅ 確認 SanatoriumProject 結構: " + path);
                        return path;
                    }
                }
            }
            
            // 遞迴搜尋子目錄
            File[] subDirs = currentDir.listFiles(File::isDirectory);
            if (subDirs != null && subDirs.length < 50) { // 限制子目錄數量避免太慢
                for (File subDir : subDirs) {
                    String result = searchSanatoriumProject(subDir, currentDepth + 1, maxDepth);
                    if (result != null) {
                        return result;
                    }
                }
            }
            
        } catch (SecurityException e) {
            // 忽略無權限存取的目錄
        } catch (Exception e) {
            System.out.println("⚠️ 搜尋目錄時發生錯誤: " + currentDir.getAbsolutePath() + " - " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * 判斷是否應該跳過某個目錄
     */
    private boolean shouldSkipDirectory(String dirName) {
        // 系統目錄和隱藏目錄
        String[] skipDirs = {
            "system volume information", "$recycle.bin", "windows", "program files", 
            "program files (x86)", "programdata", "recovery", "boot", "msocache",
            "system32", "syswow64", ".git", ".svn", ".metadata", "node_modules",
            "target", "build", "bin", "obj", "debug", "release", "temp", "tmp",
            "cache", "appdata", "users"
        };
        
        for (String skipDir : skipDirs) {
            if (dirName.equals(skipDir) || dirName.startsWith(".")) {
                return true;
            }
        }
        
        return false;
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
            } else {
                System.out.println("目錄已存在: " + dirPath);
            }
        } catch (Exception e) {
            System.err.println("建立目錄失敗 " + dirPath + ": " + e.getMessage());
        }
    }
    
    /**
     * 處理照片上傳 - 簡化版
     */
    protected String handlePhotoUpload(HttpServletRequest request) throws IOException, ServletException {
        System.out.println("=== 開始處理照片上傳 ===");
        
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
            syncToSourceDirectory(savedFile, uniqueFileName);
            
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
     * 同步檔案到源碼目錄 - 簡化版
     */
    private void syncToSourceDirectory(File deployedFile, String fileName) {
        try {
            String sourceUploadDir = findSanatoriumProjectUploadDir();
            
            if (sourceUploadDir == null) {
                System.out.println("ℹ️ 未找到 SanatoriumProject 目錄，跳過同步");
                return;
            }
            
            String sourcePath = sourceUploadDir + File.separator + fileName;
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
     * 刪除照片檔案 - 簡化版
     */
    protected void deletePhotoFile(String photoPath) {
        if (photoPath == null || !photoPath.startsWith("/CaregiverPage/uploads/photos/")) {
            return;
        }
        
        try {
            // 刪除部署目錄的檔案
            String realPath = getServletContext().getRealPath(photoPath);
            deleteFile(realPath, "部署目錄");
            
            // 刪除源碼目錄的檔案
            String sourceUploadDir = findSanatoriumProjectUploadDir();
            if (sourceUploadDir != null) {
                String fileName = photoPath.substring(photoPath.lastIndexOf("/") + 1);
                String sourcePath = sourceUploadDir + File.separator + fileName;
                deleteFile(sourcePath, "源碼目錄");
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
        
        // 驗證電話格式
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
        System.out.println("BaseCaregiverServlet 已銷毀 (簡化版)");
        super.destroy();
    }
}