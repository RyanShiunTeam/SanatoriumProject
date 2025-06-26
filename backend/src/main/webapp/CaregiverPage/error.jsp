<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<%@ page import="java.util.Date" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>系統錯誤 - SanatoriumProject</title>
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
        .error-container {
            background-color: white;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            padding: 40px;
            max-width: 600px;
            width: 90%;
            text-align: center;
            position: relative;
            overflow: hidden;
        }
        .error-container::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 5px;
            background: linear-gradient(90deg, #ff6b6b, #feca57, #48dbfb, #ff9ff3);
        }
        .project-badge {
            position: absolute;
            top: 15px;
            right: 15px;
            background-color: #007bff;
            color: white;
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 12px;
            font-weight: bold;
        }
        .error-icon {
            font-size: 80px;
            color: #ff6b6b;
            margin-bottom: 20px;
            animation: shake 0.5s ease-in-out infinite alternate;
        }
        @keyframes shake {
            0% { transform: translateX(0); }
            100% { transform: translateX(10px); }
        }
        .error-title {
            font-size: 28px;
            color: #2c3e50;
            margin-bottom: 15px;
            font-weight: bold;
        }
        .error-message {
            font-size: 16px;
            color: #7f8c8d;
            margin-bottom: 30px;
            line-height: 1.6;
        }
        .error-details {
            background-color: #f8f9fa;
            border-left: 4px solid #ff6b6b;
            padding: 15px;
            margin: 20px 0;
            text-align: left;
            border-radius: 0 8px 8px 0;
        }
        .error-details h4 {
            color: #e74c3c;
            margin: 0 0 10px 0;
            font-size: 14px;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        .error-details p {
            color: #555;
            margin: 5px 0;
            font-family: 'Courier New', monospace;
            font-size: 13px;
            word-break: break-all;
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
            box-shadow: 0 4px 15px rgba(0,123,255,0.3);
        }
        .btn:hover {
            background-color: #0056b3;
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(0,123,255,0.4);
        }
        .btn-secondary {
            background-color: #6c757d;
            box-shadow: 0 4px 15px rgba(108,117,125,0.3);
        }
        .btn-secondary:hover {
            background-color: #5a6268;
            box-shadow: 0 6px 20px rgba(108,117,125,0.4);
        }
        .btn-success {
            background-color: #28a745;
            box-shadow: 0 4px 15px rgba(40,167,69,0.3);
        }
        .btn-success:hover {
            background-color: #218838;
            box-shadow: 0 6px 20px rgba(40,167,69,0.4);
        }
        .btn-warning {
            background-color: #ffc107;
            color: #212529;
            box-shadow: 0 4px 15px rgba(255,193,7,0.3);
        }
        .btn-warning:hover {
            background-color: #e0a800;
            box-shadow: 0 6px 20px rgba(255,193,7,0.4);
        }
        .actions {
            margin-top: 30px;
        }
        .system-info {
            background-color: #e9ecef;
            border-radius: 8px;
            padding: 15px;
            margin-top: 20px;
            font-size: 12px;
            color: #6c757d;
        }
        .toggle-details {
            background: none;
            border: none;
            color: #007bff;
            cursor: pointer;
            text-decoration: underline;
            font-size: 14px;
            margin-top: 10px;
        }
        .toggle-details:hover {
            color: #0056b3;
        }
        #technicalDetails {
            display: none;
        }
        .back-link {
            position: absolute;
            top: 20px;
            left: 20px;
            color: #007bff;
            text-decoration: none;
            font-size: 14px;
        }
        .back-link:hover {
            text-decoration: underline;
        }
        .breadcrumb {
            margin-bottom: 20px;
            font-size: 14px;
            color: #6c757d;
            text-align: left;
        }
        .breadcrumb a {
            color: #007bff;
            text-decoration: none;
        }
        .breadcrumb a:hover {
            text-decoration: underline;
        }
        @media (max-width: 768px) {
            .actions {
                display: flex;
                flex-direction: column;
                align-items: center;
            }
            .btn {
                margin: 5px 0;
                width: 80%;
                text-align: center;
            }
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="project-badge">SanatoriumProject</div>
        
        <a href="javascript:history.back()" class="back-link">← 返回上一頁</a>
        
        <div class="breadcrumb">
            <a href="<%= request.getContextPath() %>/backHome.html">系統首頁</a> > 
            <a href="<%= request.getContextPath() %>/caregiver/">照服員管理</a> > 
            系統錯誤
        </div>
        
        <div class="error-icon">⚠️</div>
        
        <h1 class="error-title">SanatoriumProject 系統錯誤</h1>
        
        <div class="error-message">
            很抱歉，照服員管理系統在處理您的請求時遇到了問題。<br>
            請稍後再試，或聯繫系統管理員。
        </div>
        
        <!-- 顯示自定義錯誤訊息 -->
        <%
            String error = (String) request.getAttribute("error");
            if (error != null && !error.trim().isEmpty()) {
        %>
            <div class="error-details">
                <h4>📋 錯誤詳情</h4>
                <p><%= error %></p>
            </div>
        <%
            }
        %>
        
        <!-- 顯示 Exception 資訊 -->
        <%
            Exception requestException = (Exception) request.getAttribute("exception");
            if (requestException != null) {
        %>
            <div class="error-details">
                <h4>🔧 系統錯誤</h4>
                <p><strong>錯誤類型：</strong><%= requestException.getClass().getSimpleName() %></p>
                <p><strong>錯誤訊息：</strong><%= requestException.getMessage() %></p>
            </div>
        <%
            }
        %>
        
        <!-- JSP 錯誤頁面的內建變數 -->
        <%
            Exception pageException = pageContext.getException();
            if (pageException != null) {
        %>
            <div class="error-details">
                <h4>🚨 系統異常</h4>
                <p><strong>異常類型：</strong><%= pageException.getClass().getSimpleName() %></p>
                <p><strong>異常訊息：</strong><%= pageException.getMessage() %></p>
            </div>
        <%
            }
        %>
        
        <button class="toggle-details" onclick="toggleTechnicalDetails()">
            🔍 顯示技術詳情
        </button>
        
        <div id="technicalDetails" class="system-info">
            <h4>🖥️ 技術資訊</h4>
            <p><strong>專案：</strong>SanatoriumProject - 照服員管理模組</p>
            <p><strong>請求 URI：</strong><%= request.getRequestURI() %></p>
            <p><strong>請求方法：</strong><%= request.getMethod() %></p>
            <p><strong>Context Path：</strong><%= request.getContextPath() %></p>
            <p><strong>Server Info：</strong><%= application.getServerInfo() %></p>
            <p><strong>用戶代理：</strong><%= request.getHeader("User-Agent") %></p>
            <p><strong>時間戳記：</strong><%= new Date() %></p>
            <p><strong>會話 ID：</strong><%= session.getId() %></p>
            <p><strong>Remote Addr：</strong><%= request.getRemoteAddr() %></p>
        </div>
        
        <div class="actions">
            <a href="<%= request.getContextPath() %>/caregiver/" class="btn btn-success">
                🏠 回到照服員首頁
            </a>
            <a href="<%= request.getContextPath() %>/backHome.html" class="btn btn-warning">
                🏢 回到系統首頁
            </a>
            <a href="javascript:history.back()" class="btn btn-secondary">
                ⬅️ 返回上一頁
            </a>