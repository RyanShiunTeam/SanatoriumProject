<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>匯入結果</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f9f9f9; }
        .container { background-color: white; padding: 25px 30px; border: 1px solid #ccc; max-width: 700px; }
        h2 { color: #333; }
        .success { color: green; font-weight: bold; }
        .warning { color: #e69500; }
        .error { color: red; font-weight: bold; }
        ul { padding-left: 20px; }
        ul li { margin-bottom: 6px; line-height: 1.5; }
        a {
            display: inline-block;
            margin-top: 20px;
            text-decoration: none;
            color: #007BFF;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>匯入結果</h2>

    <p>📦 匯入總筆數：<%= request.getAttribute("totalCount") %> 筆</p>
    <p class="success">✅ 成功匯入：<%= request.getAttribute("successCount") %> 筆</p>
    <p class="warning">⚠️ 略過（分類不存在）：<%= request.getAttribute("skipCount") %> 筆</p>
    <p class="error">❌ 格式錯誤：<%= request.getAttribute("errorCount") %> 筆</p>

    <%
        List<String> errors = (List<String>) request.getAttribute("errorMessages");
        if (errors != null && !errors.isEmpty()) {
    %>
        <h3 class="error">錯誤詳情：</h3>
        <ul>
        <% for (String msg : errors) { %>
            <li><%= msg %></li>
        <% } %>
        </ul>
    <%
        }
    %>

    <a href="<%= request.getContextPath() %>/DevicePage/importDevice.jsp">↺ 再次匯入</a><br>
    <a href="<%= request.getContextPath() %>/DevicePage/deviceList.jsp">← 回輔具清單</a>
</div>
</body>
</html>
