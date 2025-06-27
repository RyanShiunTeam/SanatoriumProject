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
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

/**
 * 照服員控制器基類
 * 通用版本 - 適用於不同開發者和部署環境
 * 支援多種專案結構和配置方式
 * 整合混合式智能搜尋功能
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
        System.out.println("=== BaseCaregiverServlet 初始化 (智能搜尋版本) ===");
        
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
     * 設定預設配置 - 只保留智能搜尋設定
     */
    private void setDefaultConfig() {
        systemConfig.setProperty("upload.max.file.size", "10485760"); // 10MB
        systemConfig.setProperty("upload.max.request.size", "15728640"); // 15MB
        systemConfig.setProperty("project.environment", "development");
        systemConfig.setProperty("upload.sync.source", "true");
        
        // 智能搜尋相關設定
        systemConfig.setProperty("search.full.disk", "false");      // 預設不啟用完整磁碟搜尋
        systemConfig.setProperty("search.timeout", "60");           // 搜尋超時時間（秒）
        systemConfig.setProperty("search.max.depth", "4");          // 最大搜尋深度
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
     * 智能偵測源碼目錄 - 先找 SanatoriumProject 資料夾
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
            
            // 第一階段：搜尋 SanatoriumProject 資料夾
            System.out.println("🔍 階段1：搜尋 SanatoriumProject 資料夾...");
            List<String> sanatoriumRoots = searchSanatoriumProject();
            String foundPath = testSanatoriumPaths(sanatoriumRoots);
            
            if (foundPath != null) {
                return foundPath;
            }
            
            // 第二階段：完整磁碟搜尋（需要明確啟用）
            String enableFullSearch = systemConfig.getProperty("search.full.disk", "false");
            if ("true".equals(enableFullSearch)) {
                System.out.println("🔍 階段2：完整磁碟搜尋 SanatoriumProject...");
                List<String> fullSearchRoots = searchSanatoriumProjectFullDisk();
                foundPath = testSanatoriumPaths(fullSearchRoots);
                
                if (foundPath != null) {
                    return foundPath;
                }
            } else {
                System.out.println("💡 提示：可在 config.properties 中設定 search.full.disk=true 啟用完整磁碟搜尋");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ 源碼目錄偵測失敗: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("❌ 無法偵測到源碼目錄");
        return null;
    }
    
    /**
     * 搜尋 SanatoriumProject 資料夾 - 在常見位置
     */
    private List<String> searchSanatoriumProject() {
        List<String> sanatoriumRoots = new ArrayList<>();
        
        // 定義常見的開發目錄名稱
        List<String> commonDevDirs = Arrays.asList(
            "workspace", "eclipse-workspace", "git", "projects", "dev", "code", 
            "teamspace", "source", "src", "development", "work", "repo", "repositories"
        );
        
        // 取得所有可用的磁碟機
        File[] drives = File.listRoots();
        
        for (File drive : drives) {
            String drivePath = drive.getAbsolutePath();
            System.out.println("🔍 搜尋磁碟機: " + drivePath);
            
            // 直接在根目錄搜尋 SanatoriumProject
            searchSanatoriumInDirectory(drivePath, sanatoriumRoots);
            
            // 在常見開發目錄中搜尋 SanatoriumProject
            for (String devDirName : commonDevDirs) {
                searchSanatoriumInDirectory(drivePath + devDirName, sanatoriumRoots);
            }
            
            // 在用戶目錄下搜尋
            String userHome = System.getProperty("user.home");
            if (userHome != null && userHome.startsWith(drivePath.substring(0, 1))) {
                // 用戶目錄根目錄
                searchSanatoriumInDirectory(userHome, sanatoriumRoots);
                
                // 用戶目錄下的常見開發目錄
                for (String devDirName : commonDevDirs) {
                    searchSanatoriumInDirectory(userHome + "/" + devDirName, sanatoriumRoots);
                    searchSanatoriumInDirectory(userHome + "/Documents/" + devDirName, sanatoriumRoots);
                    searchSanatoriumInDirectory(userHome + "/Desktop/" + devDirName, sanatoriumRoots);
                }
            }
            
            // 檢查當前工作目錄
            String userDir = System.getProperty("user.dir");
            if (userDir != null && userDir.startsWith(drivePath.substring(0, 1))) {
                searchSanatoriumInDirectory(userDir, sanatoriumRoots);
                searchSanatoriumInDirectory(new File(userDir).getParent(), sanatoriumRoots);
            }
        }
        
        return sanatoriumRoots;
    }
    
    /**
     * 在指定目錄中搜尋 SanatoriumProject 資料夾
     */
    private void searchSanatoriumInDirectory(String dirPath, List<String> results) {
        try {
            if (dirPath == null) return;
            
            File dir = new File(dirPath);
            if (!dir.exists() || !dir.isDirectory()) {
                return;
            }
            
            System.out.println("📁 檢查目錄: " + dirPath);
            
            // 直接尋找 SanatoriumProject 資料夾
            File sanatoriumDir = new File(dir, "SanatoriumProject");
            if (sanatoriumDir.exists() && sanatoriumDir.isDirectory()) {
                results.add(sanatoriumDir.getAbsolutePath());
                System.out.println("✅ 找到 SanatoriumProject: " + sanatoriumDir.getAbsolutePath());
            }
            
            // 檢查子目錄（限制一層，避免太深）
            File[] subDirs = dir.listFiles(File::isDirectory);
            if (subDirs != null && subDirs.length < 50) { // 限制檢查的子目錄數量
                for (File subDir : subDirs) {
                    // 跳過明顯不相關的目錄
                    String subDirName = subDir.getName();
                    if (shouldSkipDirectory(subDirName)) {
                        continue;
                    }
                    
                    File subSanatoriumDir = new File(subDir, "SanatoriumProject");
                    if (subSanatoriumDir.exists() && subSanatoriumDir.isDirectory()) {
                        results.add(subSanatoriumDir.getAbsolutePath());
                        System.out.println("✅ 找到 SanatoriumProject: " + subSanatoriumDir.getAbsolutePath());
                    }
                }
            }
            
        } catch (SecurityException e) {
            System.out.println("⚠️ 無權限存取目錄: " + dirPath);
        } catch (Exception e) {
            System.out.println("⚠️ 搜尋目錄時發生錯誤: " + dirPath + " - " + e.getMessage());
        }
    }
    
    /**
     * 完整磁碟搜尋 SanatoriumProject（較慢，需要明確啟用）
     */
    private List<String> searchSanatoriumProjectFullDisk() {
        List<String> sanatoriumRoots = new ArrayList<>();
        
        // 從設定檔讀取搜尋超時時間
        int timeoutSeconds = getConfigValueAsInt("search.timeout", 60);
        long startTime = System.currentTimeMillis();
        
        System.out.println("⚠️ 開始完整磁碟搜尋 SanatoriumProject，這可能需要較長時間（最多 " + timeoutSeconds + " 秒）...");
        
        // 取得所有可用的磁碟機
        File[] drives = File.listRoots();
        
        for (File drive : drives) {
            // 檢查是否超時
            if ((System.currentTimeMillis() - startTime) / 1000 > timeoutSeconds) {
                System.out.println("⏰ 搜尋超時，停止搜尋");
                break;
            }
            
            System.out.println("🔍 完整搜尋磁碟機: " + drive.getAbsolutePath());
            
            // 限制搜尋深度避免太慢
            int maxDepth = getConfigValueAsInt("search.max.depth", 4);
            List<String> foundInDrive = searchSanatoriumInDrive(drive, maxDepth, startTime, timeoutSeconds * 1000);
            sanatoriumRoots.addAll(foundInDrive);
        }
        
        return sanatoriumRoots;
    }
    
    /**
     * 在指定磁碟機中搜尋 SanatoriumProject 資料夾
     */
    private List<String> searchSanatoriumInDrive(File startDir, int maxDepth, long startTime, long timeoutMs) {
        List<String> results = new ArrayList<>();
        
        // 檢查超時
        if ((System.currentTimeMillis() - startTime) > timeoutMs) {
            return results;
        }
        
        if (maxDepth <= 0 || !startDir.exists() || !startDir.isDirectory()) {
            return results;
        }
        
        try {
            File[] children = startDir.listFiles();
            if (children == null || children.length > 200) { // 避免檢查有太多檔案的目錄
                return results;
            }
            
            for (File child : children) {
                // 檢查超時
                if ((System.currentTimeMillis() - startTime) > timeoutMs) {
                    break;
                }
                
                if (!child.isDirectory()) continue;
                
                // 跳過系統目錄、隱藏目錄和明顯不相關的目錄
                String name = child.getName();
                if (shouldSkipDirectory(name)) {
                    continue;
                }
                
                // 檢查是否是 SanatoriumProject 資料夾
                if (name.equals("SanatoriumProject")) {
                    results.add(child.getAbsolutePath());
                    System.out.println("✅ 完整搜尋找到 SanatoriumProject: " + child.getAbsolutePath());
                }
                
                // 繼續往下搜尋（但要限制深度）
                if (maxDepth > 1) {
                    results.addAll(searchSanatoriumInDrive(child, maxDepth - 1, startTime, timeoutMs));
                }
            }
            
        } catch (SecurityException e) {
            // 忽略沒有權限存取的目錄
        } catch (Exception e) {
            // 忽略其他錯誤
        }
        
        return results;
    }
    
    /**
     * 測試 SanatoriumProject 路徑與專案結構的組合
     */
    private String testSanatoriumPaths(List<String> sanatoriumRoots) {
        if (sanatoriumRoots.isEmpty()) {
            System.out.println("📭 沒有找到 SanatoriumProject 資料夾");
            return null;
        }
        
        // 定義 SanatoriumProject 內部可能的專案結構
        List<String> possiblePatterns = Arrays.asList(
            // 直接在根目錄下
            "CaregiverPage/uploads/photos",
            // Maven 標準結構
            "src/main/webapp/CaregiverPage/uploads/photos",
            "backend/src/main/webapp/CaregiverPage/uploads/photos",
            // Eclipse 專案結構  
            "WebContent/CaregiverPage/uploads/photos",
            // 一般 Web 專案結構
            "webapp/CaregiverPage/uploads/photos",
            "web/CaregiverPage/uploads/photos"
        );
        
        System.out.println("🔍 SanatoriumProject搜尋：測試 " + sanatoriumRoots.size() + " 個資料夾 × " + possiblePatterns.size() + " 個模式");
        
        // 測試所有可能的組合
        for (String sanatoriumRoot : sanatoriumRoots) {
            for (String pattern : possiblePatterns) {
                String testPath = sanatoriumRoot + "/" + pattern;
                testPath = testPath.replace("\\", "/").replaceAll("/+", "/");
                
                File testDir = new File(testPath);
                File parentDir = testDir.getParentFile(); // uploads 目錄
                
                System.out.println("🔍 測試路徑: " + testPath + " (父目錄存在: " + (parentDir != null && parentDir.exists()) + ")");
                
                if (parentDir != null && parentDir.exists()) {
                    System.out.println("✅ SanatoriumProject 找到源碼路徑: " + testPath);
                    return testPath;
                }
            }
        }
        
        System.out.println("❌ SanatoriumProject：未找到有效的專案結構");
        return null;
    }
    
    /**
     * 判斷是否應該跳過某個目錄
     */
    private boolean shouldSkipDirectory(String dirName) {
        // 系統目錄
        if (dirName.equals("System Volume Information") ||
            dirName.equals("$RECYCLE.BIN") ||
            dirName.equals("Windows") ||
            dirName.equals("Program Files") ||
            dirName.equals("Program Files (x86)") ||
            dirName.equals("ProgramData") ||
            dirName.equals("Recovery")) {
            return true;
        }
        
        // 隱藏目錄和特殊目錄
        if (dirName.startsWith(".") ||
            dirName.startsWith("$") ||
            dirName.equals("node_modules") ||
            dirName.equals("target") ||
            dirName.equals("build") ||
            dirName.equals("dist") ||
            dirName.equals("bin") ||
            dirName.equals("obj") ||
            dirName.equals("Debug") ||
            dirName.equals("Release")) {
            return true;
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
        System.out.println("=== 開始處理照片上傳 (智能搜尋版本) ===");
        
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
        System.out.println("BaseCaregiverServlet 已銷毀 (智能搜尋版本)");
        super.destroy();
    }
}