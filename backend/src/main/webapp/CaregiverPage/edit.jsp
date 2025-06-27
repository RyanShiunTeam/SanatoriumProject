<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>編輯照服員 - SanatoriumProject</title>
    <style>
        body {
            font-family: "Microsoft JhengHei", Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .header {
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 15px;
            border-bottom: 2px solid #ffc107;
        }
        .breadcrumb {
            margin-bottom: 20px;
            font-size: 14px;
            color: #6c757d;
        }
        .breadcrumb a {
            color: #007bff;
            text-decoration: none;
        }
        .breadcrumb a:hover {
            text-decoration: underline;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }
        input[type="text"], input[type="email"], input[type="tel"], input[type="number"], input[type="url"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            box-sizing: border-box;
        }
        input[type="radio"] {
            margin-right: 5px;
        }
        .radio-group {
            display: flex;
            gap: 20px;
            margin-top: 5px;
        }
        .radio-item {
            display: flex;
            align-items: center;
        }
        .btn {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            border: none;
            cursor: pointer;
            font-size: 14px;
        }
        .btn:hover {
            background-color: #0056b3;
        }
        .btn-secondary {
            background-color: #6c757d;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
        }
        .btn-warning {
            background-color: #ffc107;
            color: #212529;
        }
        .btn-warning:hover {
            background-color: #e0a800;
        }
        .form-actions {
            display: flex;
            gap: 10px;
            justify-content: center;
            margin-top: 30px;
        }
        .required {
            color: red;
        }
        .alert {
            padding: 10px;
            margin-bottom: 20px;
            border-radius: 4px;
        }
        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .id-display {
            background-color: #f8f9fa;
            padding: 10px;
            border-radius: 4px;
            color: #6c757d;
            font-weight: bold;
        }
        .photo-section {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 15px;
        }
        .current-photo {
            text-align: center;
            margin-bottom: 10px;
        }
        .photo-preview-container {
            text-align: center;
            margin-top: 10px;
        }
        #preview {
            max-width: 150px;
            max-height: 150px;
            border: 1px solid #ddd;
            border-radius: 4px;
            display: none;
        }
        @media (max-width: 768px) {
            .form-actions {
                flex-direction: column;
            }
            .btn {
                margin-bottom: 10px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- 麵包屑導航 -->
        <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/backHome.html">系統首頁</a> > 
            <a href="${pageContext.request.contextPath}/caregiver/">照服員管理</a> > 
            編輯照服員
        </div>
        
        <div class="header">
            <h1>編輯照服員</h1>
        </div>
        
        <!-- 顯示錯誤訊息 -->
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-error">
                ${sessionScope.error}
                <c:remove var="error" scope="session"/>
            </div>
        </c:if>
        
        <c:if test="${not empty caregiver}">
            <!-- 修改表單路徑 -->
            <form action="${pageContext.request.contextPath}/caregiver/update/update" method="post" enctype="multipart/form-data">
                <!-- 隱藏的ID欄位 -->
                <input type="hidden" name="caregiverId" value="${caregiver.caregiverId}">
                
                <div class="form-group">
                    <label>照服員編號</label>
                    <div class="id-display">編號：${caregiver.caregiverId}</div>
                </div>
                
                <div class="form-group">
                    <label for="chineseName">中文姓名 <span class="required">*</span></label>
                    <input type="text" id="chineseName" name="chineseName" value="${caregiver.chineseName}" required>
                </div>
                
                <div class="form-group">
                    <label>性別 <span class="required">*</span></label>
                    <div class="radio-group">
                        <div class="radio-item">
                            <input type="radio" id="male" name="gender" value="true" ${caregiver.gender ? 'checked' : ''} required>
                            <label for="male">男性</label>
                        </div>
                        <div class="radio-item">
                            <input type="radio" id="female" name="gender" value="false" ${!caregiver.gender ? 'checked' : ''} required>
                            <label for="female">女性</label>
                        </div>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="phone">聯絡電話 <span class="required">*</span></label>
                    <input type="tel" id="phone" name="phone" value="${caregiver.phone}" required>
                </div>
                
                <div class="form-group">
                    <label for="email">電子信箱 <span class="required">*</span></label>
                    <input type="email" id="email" name="email" value="${caregiver.email}" required>
                </div>
                
                <div class="form-group">
                    <label for="experienceYears">工作年資 <span class="required">*</span></label>
                    <input type="number" id="experienceYears" name="experienceYears" value="${caregiver.experienceYears}" min="0" max="50" required>
                </div>
                
                <div class="form-group">
                    <label>照片管理</label>
                    <div class="photo-section">
                        <div class="current-photo">
                            <strong>目前照片：</strong><br>
                            <c:choose>
                                <c:when test="${not empty caregiver.photo}">
                                    <img src="${pageContext.request.contextPath}${caregiver.photo}" 
                                         alt="目前照片" 
                                         style="max-width: 150px; max-height: 150px; border: 1px solid #ddd; border-radius: 4px; margin-top: 10px;"
                                         onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">
                                    <div style="color: #dc3545; margin-top: 10px; display: none;">照片載入失敗</div>
                                </c:when>
                                <c:otherwise>
                                    <div style="color: #6c757d; margin-top: 10px; padding: 20px; border: 2px dashed #dee2e6; border-radius: 4px;">
                                        目前沒有照片
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        
                        <label for="photo" style="margin-top: 15px;">更新照片（選擇檔案將替換現有照片）</label>
                        <input type="file" id="photo" name="photo" accept="image/*">
                        <small style="color: #6c757d; font-size: 12px;">
                            支援格式：JPG, PNG, GIF, WebP | 檔案大小限制：10MB
                        </small>
                        
                        <div class="photo-preview-container">
                            <div id="previewLabel" style="margin-top: 10px; font-weight: bold; color: #28a745; display: none;">新照片預覽：</div>
                            <img id="preview" src="#" alt="新照片預覽">
                        </div>
                    </div>
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-warning">更新照服員</button>
                    <a href="${pageContext.request.contextPath}/caregiver/" class="btn btn-secondary">取消</a>
                    <a href="${pageContext.request.contextPath}/caregiver/view?id=${caregiver.caregiverId}" class="btn">查看詳細</a>
                </div>
            </form>
        </c:if>
        
        <c:if test="${empty caregiver}">
            <div class="alert alert-error">
                找不到指定的照服員資料！
            </div>
            <div style="text-align: center;">
                <a href="${pageContext.request.contextPath}/caregiver/" class="btn btn-secondary">返回列表</a>
            </div>
        </c:if>
    </div>
    
    <script>
        // 照片預覽功能
        document.getElementById('photo').addEventListener('change', function(e) {
            const file = e.target.files[0];
            const preview = document.getElementById('preview');
            const previewLabel = document.getElementById('previewLabel');
            
            if (file) {
                // 檢查檔案類型
                if (!file.type.startsWith('image/')) {
                    alert('請選擇圖片檔案！支援格式：JPG, PNG, GIF, WebP');
                    this.value = '';
                    preview.style.display = 'none';
                    previewLabel.style.display = 'none';
                    return;
                }
                
                // 檢查檔案大小（10MB）
                if (file.size > 10 * 1024 * 1024) {
                    alert('檔案大小不能超過 10MB！');
                    this.value = '';
                    preview.style.display = 'none';
                    previewLabel.style.display = 'none';
                    return;
                }
                
                const reader = new FileReader();
                reader.onload = function(e) {
                    preview.src = e.target.result;
                    preview.style.display = 'block';
                    previewLabel.style.display = 'block';
                };
                reader.readAsDataURL(file);
            } else {
                preview.style.display = 'none';
                previewLabel.style.display = 'none';
            }
        });

        // 表單驗證
        document.querySelector('form')?.addEventListener('submit', function(e) {
            var chineseName = document.getElementById('chineseName').value.trim();
            var phone = document.getElementById('phone').value.trim();
            var email = document.getElementById('email').value.trim();
            var experienceYears = document.getElementById('experienceYears').value;
            var gender = document.querySelector('input[name="gender"]:checked');
            
            if (!chineseName || !phone || !email || !experienceYears || !gender) {
                alert('請填寫所有必填欄位！');
                e.preventDefault();
                return false;
            }
            
            // 驗證電話格式
            var phoneRegex = /^[0-9\-\+\s()]+$/;
            if (!phoneRegex.test(phone)) {
                alert('請輸入有效的電話號碼！');
                e.preventDefault();
                return false;
            }
            
            // 驗證經驗年數
            if (experienceYears < 0 || experienceYears > 50) {
                alert('工作年資必須在 0-50 年之間！');
                e.preventDefault();
                return false;
            }
            
            // 驗證信箱格式
            var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                alert('請輸入有效的電子信箱格式！');
                e.preventDefault();
                return false;
            }
            
            // 確認更新
            if (!confirm('確定要更新此照服員的資料嗎？')) {
                e.preventDefault();
                return false;
            }
            
            console.log('表單驗證通過，準備提交更新');
            return true;
        });
        
        // 頁面載入完成後的初始化
        document.addEventListener('DOMContentLoaded', function() {
            console.log('編輯頁面載入完成 - SanatoriumProject');
            
            // 檢查是否有照服員資料
            const caregiverExists = ${not empty caregiver};
            console.log('照服員資料存在:', caregiverExists);
            
            // 如果有照服員資料，載入基本資訊到控制台
            if (caregiverExists) {
                console.log('載入照服員編號: ${caregiver.caregiverId}');
                console.log('載入照服員姓名: ${caregiver.chineseName}');
            }
        });
    </script>
</body>
</html>