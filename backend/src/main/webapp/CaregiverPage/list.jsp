<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="caregiver.bean.Caregiver" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>照服員管理系統 - SanatoriumProject</title>
    <style>
        body {
            font-family: "Microsoft JhengHei", Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #007bff;
        }
        .header-left {
            display: flex;
            align-items: center;
            gap: 15px;
        }
        .back-btn {
            padding: 8px 16px;
            background-color: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            border: none;
            cursor: pointer;
        }
        .back-btn:hover {
            background-color: #5a6268;
        }
        .search-bar {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }
        .search-bar input {
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            flex: 1;
        }
        .btn {
            padding: 8px 16px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            border: none;
            cursor: pointer;
            font-size: 14px;
            transition: all 0.2s;
        }
        .btn:hover {
            background-color: #0056b3;
            transform: translateY(-1px);
        }
        .btn-danger {
            background-color: #dc3545;
        }
        .btn-danger:hover {
            background-color: #c82333;
        }
        .btn-success {
            background-color: #28a745;
        }
        .btn-success:hover {
            background-color: #218838;
        }
        .btn-warning {
            background-color: #ffc107;
            color: #212529;
        }
        .btn-warning:hover {
            background-color: #e0a800;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f8f9fa;
            font-weight: bold;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .actions {
            display: flex;
            gap: 5px;
        }
        .alert {
            padding: 10px;
            margin-bottom: 20px;
            border-radius: 4px;
        }
        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .gender {
            color: #007bff;
            font-weight: bold;
        }
        .experience {
            background-color: #e9ecef;
            padding: 2px 6px;
            border-radius: 12px;
            font-size: 12px;
        }
        /* 刪除確認對話框樣式 */
        .delete-modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.5);
        }
        .delete-modal-content {
            background-color: white;
            margin: 15% auto;
            padding: 20px;
            border-radius: 8px;
            width: 400px;
            text-align: center;
            box-shadow: 0 4px 6px rgba(0,0,0,0.3);
        }
        .delete-modal h3 {
            color: #dc3545;
            margin-bottom: 15px;
        }
        .delete-modal-buttons {
            margin-top: 20px;
            display: flex;
            gap: 10px;
            justify-content: center;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <div class="header-left">
                <a href="<%=request.getContextPath()%>/backHome.html" class="back-btn">← 返回主系統</a>
                <h1>照服員管理系統</h1>
            </div>
            <a href="<%=request.getContextPath()%>/CaregiverPage/add.html" class="btn btn-success">新增照服員</a>
        </div>
        
        <!-- 顯示訊息 -->
        <%
            String message = (String) session.getAttribute("message");
            String error = (String) session.getAttribute("error");
            
            if (message != null) {
        %>
            <div class="alert alert-success">
                <%= message %>
            </div>
        <%
                session.removeAttribute("message");
            }
            
            if (error != null) {
        %>
            <div class="alert alert-error">
                <%= error %>
            </div>
        <%
                session.removeAttribute("error");
            }
        %>
        
        <!-- 搜尋欄 -->
        <form action="<%=request.getContextPath()%>/caregiver/search" method="get" class="search-bar">
            <input type="text" name="searchName" placeholder="請輸入照服員姓名..." value="<%=request.getParameter("searchName") != null ? request.getParameter("searchName") : ""%>">
            <button type="submit" class="btn">搜尋</button>
            <a href="<%=request.getContextPath()%>/caregiver/" class="btn btn-warning">顯示全部</a>
        </form>
        
        <!-- 照服員列表 -->
        <table>
            <thead>
                <tr>
                    <th>編號</th>
                    <th>照片</th>
                    <th>姓名</th>
                    <th>性別</th>
                    <th>聯絡電話</th>
                    <th>電子信箱</th>
                    <th>工作年資</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <%
                    @SuppressWarnings("unchecked")
                    List<Caregiver> caregivers = (List<Caregiver>) request.getAttribute("caregivers");
                    
                    if (caregivers != null && !caregivers.isEmpty()) {
                        for (Caregiver caregiver : caregivers) {
                %>
                    <tr>
                        <td><%= caregiver.getCaregiverId() %></td>
                        <td>
                            <% 
                                String photoPath = caregiver.getPhoto();
                                if (photoPath != null && !photoPath.trim().isEmpty()) { 
                            %>
                                <img src="<%=request.getContextPath()%><%= photoPath %>" 
                                     alt="照服員照片" 
                                     style="width: 50px; height: 50px; object-fit: cover; border-radius: 50%; border: 2px solid #ddd;"
                                     onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                <div style="width: 50px; height: 50px; background-color: #f0f0f0; border-radius: 50%; display: none; align-items: center; justify-content: center; color: #999; font-size: 12px;">
                                    載入失敗
                                </div>
                            <% } else { %>
                                <div style="width: 50px; height: 50px; background-color: #f0f0f0; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: #999; font-size: 12px;">
                                    無照片
                                </div>
                            <% } %>
                        </td>
                        <td><%= caregiver.getChineseName() %></td>
                        <td>
                            <span class="gender">
                                <%= caregiver.getGender() ? "男" : "女" %>
                            </span>
                        </td>
                        <td>
                            <a href="tel:<%= caregiver.getPhone() %>" style="color: #007bff; text-decoration: none;">
                                <%= caregiver.getPhone() %>
                            </a>
                        </td>
                        <td>
                            <a href="mailto:<%= caregiver.getEmail() %>" style="color: #007bff; text-decoration: none;">
                                <%= caregiver.getEmail() %>
                            </a>
                        </td>
                        <td>
                            <span class="experience"><%= caregiver.getExperienceYears() %> 年</span>
                        </td>
                        <td>
                            <div class="actions">
                                <a href="<%=request.getContextPath()%>/caregiver/view?id=<%= caregiver.getCaregiverId() %>" class="btn">查看</a>
                                <a href="<%=request.getContextPath()%>/caregiver/edit?id=<%= caregiver.getCaregiverId() %>" class="btn btn-warning">編輯</a>
                                <button type="button" 
                                        class="btn btn-danger" 
                                        onclick="confirmDelete(<%= caregiver.getCaregiverId() %>, '<%= caregiver.getChineseName().replace("'", "\\'") %>')">
                                    刪除
                                </button>
                            </div>
                        </td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="8" style="text-align: center; color: #666;">
                            <div style="padding: 40px;">
                                <div style="font-size: 48px; color: #dee2e6; margin-bottom: 16px;">📋</div>
                                <div style="font-size: 18px; margin-bottom: 8px;">目前沒有照服員資料</div>
                                <div style="font-size: 14px; color: #6c757d;">
                                    <a href="<%=request.getContextPath()%>/CaregiverPage/add.html" style="color: #007bff; text-decoration: none;">點此新增第一筆照服員資料</a>
                                </div>
                            </div>
                        </td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
        
        <!-- 頁面底部資訊 -->
        <div style="margin-top: 20px; padding-top: 15px; border-top: 1px solid #dee2e6; text-align: center; color: #6c757d; font-size: 12px;">
            <p>
                總共 <%= (request.getAttribute("caregivers") != null) ? ((List<Caregiver>)request.getAttribute("caregivers")).size() : 0 %> 筆照服員資料
                <% if (request.getParameter("searchName") != null && !request.getParameter("searchName").trim().isEmpty()) { %>
                    | 搜尋關鍵字：「<%= request.getParameter("searchName") %>」
                <% } %>
            </p>
        </div>
    </div>
    
    <!-- 自定義刪除確認對話框 -->
    <div id="deleteModal" class="delete-modal">
        <div class="delete-modal-content">
            <h3>⚠️ 確認刪除</h3>
            <p id="deleteMessage">確定要刪除此照服員嗎？</p>
            <p style="color: #666; font-size: 14px;">此操作無法復原！</p>
            <div class="delete-modal-buttons">
                <button type="button" class="btn btn-danger" onclick="executeDelete()">確定刪除</button>
                <button type="button" class="btn" onclick="cancelDelete()">取消</button>
            </div>
        </div>
    </div>
    
    <!-- 隱藏的刪除表單 -->
    <form id="deleteForm" method="POST" action="<%=request.getContextPath()%>/caregiver/delete" style="display: none;">
        <input type="hidden" id="deleteId" name="id" value="">
    </form>
    
    <script>
        // 全域變數
        let deleteTargetId = null;
        let deleteTargetName = null;
        
        /**
         * 顯示刪除確認對話框
         */
        function confirmDelete(id, name) {
            deleteTargetId = id;
            deleteTargetName = name;
            
            // 更新對話框內容
            document.getElementById('deleteMessage').textContent = 
                '確定要刪除照服員「' + name + '」嗎？';
            
            // 顯示對話框
            document.getElementById('deleteModal').style.display = 'block';
        }
        
        /**
         * 執行刪除操作
         */
        function executeDelete() {
            if (deleteTargetId === null) {
                alert('錯誤：無效的刪除目標');
                return;
            }
            
            // 設定隱藏表單的 ID 值
            document.getElementById('deleteId').value = deleteTargetId;
            
            // 隱藏對話框
            document.getElementById('deleteModal').style.display = 'none';
            
            // 提交表單
            document.getElementById('deleteForm').submit();
        }
        
        /**
         * 取消刪除操作
         */
        function cancelDelete() {
            // 重置變數
            deleteTargetId = null;
            deleteTargetName = null;
            
            // 隱藏對話框
            document.getElementById('deleteModal').style.display = 'none';
        }
        
        /**
         * 點擊對話框外部時關閉
         */
        window.onclick = function(event) {
            const modal = document.getElementById('deleteModal');
            if (event.target === modal) {
                cancelDelete();
            }
        }
        
        /**
         * ESC 鍵關閉對話框
         */
        document.addEventListener('keydown', function(event) {
            if (event.key === 'Escape') {
                cancelDelete();
            }
        });
        
        // 自動隱藏成功訊息
        document.addEventListener('DOMContentLoaded', function() {
            const successAlert = document.querySelector('.alert-success');
            if (successAlert) {
                setTimeout(function() {
                    successAlert.style.opacity = '0';
                    setTimeout(function() {
                        successAlert.style.display = 'none';
                    }, 300);
                }, 3000);
            }
        });
    </script>
</body>
</html>