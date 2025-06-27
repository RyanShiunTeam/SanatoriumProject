<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="device.bean.DeviceCategory" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>分類清單</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f9f9f9; }
        table { width: 100%; border-collapse: collapse; background-color: white; }
        th, td { padding: 10px; text-align: center; border-bottom: 1px solid #ccc; }
        th { background-color: #eee; }
        .btn { padding: 6px 12px; color: white; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; }
        .edit-btn { background-color: #28a745; }
        .delete-btn { background-color: #dc3545; }
        .add-btn { background-color: #007BFF; margin-bottom: 10px; }
        .refresh-btn { background-color: #17a2b8; }
        .msg { color: green; font-weight: bold; margin-bottom: 10px; }
        .form-group { margin-bottom: 20px; }
    </style>
</head>
<body>
<h2>設備分類清單</h2>

<!-- 新增 / 編輯 / 刪除 成功提示訊息（來自 URL 中 status 參數） -->
<%
    String status = request.getParameter("status");
    if ("created".equals(status)) {
%>
    <div class="msg">✅ 新增成功！</div>
<% } else if ("updated".equals(status)) { %>
    <div class="msg">✅ 修改成功！</div>
<% } else if ("deleted".equals(status)) { %>
    <div class="msg">✅ 刪除成功！</div>
<% } %>

<!--  搜尋區塊：依分類名稱查詢或顯示全部 -->
<div class="form-group">
    <form method="get" action="<%= request.getContextPath() %>/CategoryServlet" style="display:inline-block;">
        <label for="name">分類名稱查詢：</label>
        <input type="text" name="name" id="name" placeholder="輸入名稱關鍵字">
        <input type="hidden" name="action" value="searchByName">
        <input type="submit" class="btn add-btn" value="🔍 查詢">
    </form>

    <form method="get" action="<%= request.getContextPath() %>/CategoryServlet" style="display:inline-block; margin-left: 10px;">
        <button type="submit" class="btn refresh-btn">🔄 顯示全部分類</button>
    </form>
</div>

<!--  新增分類按鈕-->
<form method="get" action="<%= request.getContextPath() %>/DevicePage/addCategory.jsp">
    <button type="submit" class="btn add-btn">➕ 新增分類</button>
</form>

<!--  分類列表表格（使用 categoryList）-->
<table>
    <tr>
        <th>ID</th>
        <th>名稱</th>
        <th>排序</th>
        <th>操作</th>
    </tr>
    <%
        List<DeviceCategory> categoryList = (List<DeviceCategory>) request.getAttribute("categoryList");
        if (categoryList != null && !categoryList.isEmpty()) {
            for (DeviceCategory cat : categoryList) {
    %>
    <tr>
        <td><%= cat.getId() %></td>
        <td><%= cat.getName() %></td>
        <td><%= cat.getCategoryId() %></td>
        <td>
          <!-- 編輯分類 -->
            <a href="<%= request.getContextPath() %>/CategoryServlet?action=edit&id=<%= cat.getId() %>" class="btn edit-btn">✏️ 編輯</a>
            <!-- 刪除分類（帶 confirm）-->
            <a href="<%= request.getContextPath() %>/CategoryDeleteServlet?id=<%= cat.getId() %>" class="btn delete-btn" onclick="return confirm('確定要刪除這筆分類？')">🗑️ 刪除</a>
             <!-- 查看該分類下所有輔具 -->
            <a href="<%= request.getContextPath() %>/DeviceServlet?action=byCategory&categoryId=<%= cat.getId() %>" class="btn refresh-btn">📂 查看輔具</a>
        </td>
    </tr>
    <%
            }
        } else {
    %>
    <tr><td colspan="4">查無資料</td></tr>
    <% } %>
</table>

 <!--  返回輔具清單頁 -->
<a href="<%= request.getContextPath() %>/DevicePage/deviceList.jsp">← 回輔具清單</a>
</body>
</html>
