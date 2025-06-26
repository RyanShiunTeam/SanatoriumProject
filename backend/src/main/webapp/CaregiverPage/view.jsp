<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>查看照服員 - SanatoriumProject</title>
    <style>
        body {
            font-family: "Microsoft JhengHei", Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 700px;
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
            border-bottom: 2px solid #28a745;
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
        .profile-section {
            display: flex;
            gap: 30px;
            margin-bottom: 30px;
        }
        .photo-section {
            flex: 0 0 180px;
            text-align: center;
        }
        .photo {
            width: 180px;
            height: 180px;
            border-radius: 12px;
            object-fit: cover;
            border: 3px solid #28a745;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .no-photo {
            width: 180px;
            height: 180px;
            border-radius: 12px;
            background: linear-gradient(135deg, #f8f9fa, #e9ecef);
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            border: 3px solid #dee2e6;
            color: #6c757d;
            font-size: 14px;
            text-align: center;
        }
        .no-photo-icon {
            font-size: 40px;
            margin-bottom: 10px;
            opacity: 0.5;
        }
        .info-section {
            flex: 1;
        }
        .info-card {
            background-color: #f8f9fa;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
        }
        .info-row {
            display: flex;
            margin-bottom: 15px;
            align-items: center;
        }
        .info-label {
            font-weight: bold;
            width: 120px;
            color: #495057;
            font-size: 14px;
        }
        .info-value {
            color: #212529;
            font-size: 16px;
            flex: 1;
        }
        .gender-male {
            color: #007bff;
            font-weight: bold;
            background-color: #e3f2fd;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 14px;
        }
        .gender-female {
            color: #e83e8c;
            font-weight: bold;
            background-color: #fce4ec;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 14px;
        }
        .experience-badge {
            background: linear-gradient(135deg, #28a745, #20c997);
            color: white;
            padding: 6px 16px;
            border-radius: 25px;
            font-size: 14px;
            font-weight: bold;
            box-shadow: 0 2px 4px rgba(40,167,69,0.3);
        }
        .contact-link {
            color: #007bff;
            text-decoration: none;
            padding: 4px 8px;
            border-radius: 4px;
            transition: background-color 0.2s;
        }
        .contact-link:hover {
            background-color: #e3f2fd;
            text-decoration: none;
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
            margin-right: 10px;
            transition: all 0.2s;
            display: inline-block;
        }
        .btn:hover {
            background-color: #0056b3;
            transform: translateY(-1px);
            box-shadow: 0 2px 4px rgba(0,0,0,0.2);
        }
        .btn-warning {
            background-color: #ffc107;
            color: #212529;
        }
        .btn-warning:hover {
            background-color: #e0a800;
        }
        .btn-secondary {
            background-color: #6c757d;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
        }
        .btn-danger {
            background-color: #dc3545;
        }
        .btn-danger:hover {
            background-color: #c82333;
        }
        .actions {
            text-align: center;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #dee2e6;
        }
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 6px;
            border-left: 4px solid;
        }
        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            border-left-color: #dc3545;
        }
        .id-badge {
            background: linear-gradient(135deg, #007bff, #0056b3);
            color: white;
            padding: 8px 16px;
            border-radius: 25px;
            font-weight: bold;
            display: inline-block;
            margin-bottom: 10px;
        }
        @media (max-width: 768px) {
            .profile-section {
                flex-direction: column;
                align-items: center;
                gap: 20px;
            }
            .info-row {
                flex-direction: column;
                align-items: flex-start;
                margin-bottom: 10px;
            }
            .info-label {
                width: auto;
                margin-bottom: 5px;
            }
            .actions {
                display: flex;
                flex-direction: column;
                gap: 10px;
            }
            .btn {
                margin-right: 0;
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
            查看詳細資料
        </div>
        
        <div class="header">
            <h1>照服員詳細資料</h1>
        </div>
        
        <c:choose>
            <c:when test="${not empty caregiver}">
                <div class="profile-section">
                    <div class="photo-section">
                        <c:choose>
                            <c:when test="${not empty caregiver.photo}">
                                <img src="${pageContext.request.contextPath}${caregiver.photo}" 
                                     alt="${caregiver.chineseName}的照片" 
                                     class="photo"
                                     onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                <div class="no-photo" style="display: none;">
                                    <div class="no-photo-icon">⚠️</div>
                                    <div>照片載入失敗</div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="no-photo">
                                    <div class="no-photo-icon">👤</div>
                                    <div>暫無照片</div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                        
                        <!-- 照服員編號徽章 -->
                        <div style="margin-top: 15px;">
                            <div class="id-badge">編號 ${caregiver.caregiverId}</div>
                        </div>
                    </div>
                    
                    <div class="info-section">
                        <div class="info-card">
                            <h3 style="margin-top: 0; color: #495057; border-bottom: 2px solid #28a745; padding-bottom: 10px;">基本資料</h3>
                            
                            <div class="info-row">
                                <span class="info-label">姓名：</span>
                                <span class="info-value" style="font-size: 20px; font-weight: bold; color: #28a745;">
                                    ${caregiver.chineseName}
                                </span>
                            </div>
                            
                            <div class="info-row">
                                <span class="info-label">性別：</span>
                                <span class="info-value">
                                    <c:choose>
                                        <c:when test="${caregiver.gender}">
                                            <span class="gender-male">👨 男性</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="gender-female">👩 女性</span>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                            
                            <div class="info-row">
                                <span class="info-label">工作年資：</span>
                                <span class="info-value">
                                    <span class="experience-badge">📅 ${caregiver.experienceYears} 年經驗</span>
                                </span>
                            </div>
                        </div>
                        
                        <div class="info-card">
                            <h3 style="margin-top: 0; color: #495057; border-bottom: 2px solid #007bff; padding-bottom: 10px;">聯絡資訊</h3>
                            
                            <div class="info-row">
                                <span class="info-label">聯絡電話：</span>
                                <span class="info-value">
                                    <a href="tel:${caregiver.phone}" class="contact-link">
                                        📞 ${caregiver.phone}
                                    </a>
                                </span>
                            </div>
                            
                            <div class="info-row">
                                <span class="info-label">電子信箱：</span>
                                <span class="info-value">
                                    <a href="mailto:${caregiver.email}" class="contact-link">
                                        📧 ${caregiver.email}
                                    </a>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="actions">
                    <a href="${pageContext.request.contextPath}/caregiver/edit?id=${caregiver.caregiverId}" 
                       class="btn btn-warning">✏️ 編輯資料</a>
                    <button type="button" class="btn btn-danger" 
                            onclick="deleteCaregiver(${caregiver.caregiverId}, '${caregiver.chineseName}')">
                        🗑️ 刪除照服員
                    </button>
                    <a href="${pageContext.request.contextPath}/caregiver/" 
                       class="btn btn-secondary">📋 返回列表</a>
                    <a href="${pageContext.request.contextPath}/CaregiverPage/add.html" 
                       class="btn">➕ 新增照服員</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-error">
                    <strong>❌ 錯誤：</strong>找不到指定的照服員資料！
                </div>
                <div style="text-align: center;">
                    <a href="${pageContext.request.contextPath}/caregiver/" class="btn btn-secondary">返回列表</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <script>
        function deleteCaregiver(id, name) {
            if (confirm('⚠️ 確定要刪除照服員「' + name + '」嗎？\n\n此操作無法復原！')) {
                // 建立隱藏表單來送出刪除請求
                var form = document.createElement('form');
                form.method = 'POST';
                form.action = '${pageContext.request.contextPath}/caregiver/delete';
                
                var idInput = document.createElement('input');
                idInput.type = 'hidden';
                idInput.name = 'id';
                idInput.value = id;
                
                form.appendChild(idInput);
                document.body.appendChild(form);
                form.submit();
            }
        }
        
        // 檢查照片載入狀態
        document.addEventListener('DOMContentLoaded', function() {
            console.log('照服員詳細頁面載入完成 - SanatoriumProject');
            
            var photo = document.querySelector('.photo');
            if (photo) {
                photo.addEventListener('load', function() {
                    console.log('照片載入成功:', this.src);
                });
                
                photo.addEventListener('error', function() {
                    console.log('照片載入失敗:', this.src);
                    this.style.display = 'none';
                    var noPhotoDiv = this.nextElementSibling;
                    if (noPhotoDiv && noPhotoDiv.classList.contains('no-photo')) {
                        noPhotoDiv.style.display = 'flex';
                        noPhotoDiv.innerHTML = '<div class="no-photo-icon">⚠️</div><div>照片載入失敗</div>';
                    }
                });
            }
            
            // 載入照服員基本資訊到控制台（除錯用）
            const caregiverExists = ${not empty caregiver};
            if (caregiverExists) {
                console.log('照服員資訊:');
                console.log('  編號: ${caregiver.caregiverId}');
                console.log('  姓名: ${caregiver.chineseName}');
                console.log('  性別: ${caregiver.gender ? "男性" : "女性"}');
                console.log('  年資: ${caregiver.experienceYears} 年');
                console.log('  電話: ${caregiver.phone}');
                console.log('  信箱: ${caregiver.email}');
                console.log('  照片: ${caregiver.photo}');
            }
        });
        
        // 聯絡資訊點擊統計（可選功能）
        document.querySelectorAll('.contact-link').forEach(function(link) {
            link.addEventListener('click', function() {
                console.log('聯絡方式點擊:', this.textContent.trim());
            });
        });
    </script>
</body>
</html>