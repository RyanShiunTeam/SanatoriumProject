<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>操作結果 - 照服員管理系統</title>
    <style>
        body {
            font-family: "Microsoft JhengHei", Arial, sans-serif;
            margin: 0;
            padding: 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .result-container {
            background-color: white;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            padding: 40px;
            max-width: 500px;
            width: 90%;
            text-align: center;
        }
        .success-icon {
            font-size: 60px;
            color: #28a745;
            margin-bottom: 20px;
        }
        .error-icon {
            font-size: 60px;
            color: #dc3545;
            margin-bottom: 20px;
        }
        .result-title {
            font-size: 24px;
            margin-bottom: 15px;
            font-weight: bold;
        }
        .success-title {
            color: #28a745;
        }
        .error-title {
            color: #dc3545;
        }
        .result-message {
            font-size: 16px;
            color: #6c757d;
            margin-bottom: 30px;
            line-height: 1.6;
        }
        .btn {
            display: inline-block;
            padding: 12px 25px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 25px;
            font-size: 16px;
            font-weight: bold;
            margin: 10px;
            transition: all 0.3s ease;
        }
        .btn:hover {
            background-color: #0056b3;
            transform: translateY(-2px);
        }
        .btn-success {
            background-color: #28a745;
        }
        .btn-success:hover {
            background-color: #218838;
        }
        .btn-secondary {
            background-color: #6c757d;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
        }
    </style>
</head>
<body>
    <div class="result-container">
        <c:choose>
            <c:when test="${success}">
                <div class="success-icon">✅</div>
                <h1 class="result-title success-title">操作成功！</h1>
                <div class="result-message">
                    ${message != null ? message : '操作已成功完成！'}
                </div>
                <a href="${pageContext.request.contextPath}/caregiver/" class="btn btn-success">回到列表</a>
                <a href="${pageContext.request.contextPath}/CaregiverPage/add.html" class="btn btn-secondary">繼續新增</a>
            </c:when>
            <c:otherwise>
                <div class="error-icon">❌</div>
                <h1 class="result-title error-title">操作失敗！</h1>
                <div class="result-message">
                    ${message != null ? message : '操作過程中發生錯誤，請稍後再試。'}
                </div>
                <a href="javascript:history.back()" class="btn btn-secondary">返回上一頁</a>
                <a href="${pageContext.request.contextPath}/caregiver/" class="btn">回到列表</a>
            </c:otherwise>
        </c:choose>
    </div>
    
    <script>
        // 3秒後自動跳轉（成功時）
        <c:if test="${success}">
            setTimeout(function() {
                window.location.href = '${pageContext.request.contextPath}/caregiver/';
            }, 3000);
        </c:if>
    </script>
</body>
</html>