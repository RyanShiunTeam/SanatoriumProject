<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="device.bean.Device" %>
<%@ page import="device.bean.DeviceCategory" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>分類下的輔具清單</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; background-color: #f9f9f9; }
        h2 { color: #333; }
        table {
            width: 100%;
            border-collapse: collapse;
            background-color: white;
            box-shadow: 0 0 5px rgba(0,0,0,0.1);
            margin-top: 15px;
        }
        th, td {
            padding: 10px;
            text-align: center;
            border-bottom: 1px solid #ddd;
        }
        th { background-color: #f0f0f0; }
        .back-link {
            display: inline-block;
            margin-top: 30px;
            text-decoration: none;
            color: #007BFF;
        }
        img.device-image {
            width: 80px;
            height: auto;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
    </style>
</head>
<body>

<%
    DeviceCategory category = (DeviceCategory) request.getAttribute("category");
    List<Device> deviceList = (List<Device>) request.getAttribute("deviceList");
%>

<h2>📂 分類：<%= category != null ? category.getName() : "未知分類" %></h2>

 <!-- 顯示輔具清單表格 -->
<table>
    <tr>
        <th>ID</th>
        <th>名稱</th>
        <th>SKU</th>
        <th>價格</th>
        <th>圖片</th>
        <th>庫存</th>
        <th>是否上架</th>
    </tr>
     <!-- 動態資料列 -->
    <%
        if (deviceList != null && !deviceList.isEmpty()) {
            for (Device d : deviceList) {
    %>
    <tr>
        <td><%= d.getId() %></td>
        <td><%= d.getName() %></td>
        <td><%= d.getSku() %></td>
        <td><%= d.getUnitPrice() %></td>
        <td>
            <% if (d.getImage() != null && !d.getImage().isEmpty()) { %>
                <img src="<%= request.getContextPath() + d.getImage() %>" class="device-image" alt="圖片">
            <% } else { %> 無 <% } %>
        </td>
        <td><%= d.getInventory() %></td>
        <td><%= d.isOnline() ? "是" : "否" %></td>
    </tr>
    <%
            }
        } else {
    %>
    <tr><td colspan="7">查無資料</td></tr>
    <% } %>
</table>
<!-- 返回連結 -->
<a href="<%= request.getContextPath() %>/CategoryServlet">← 回分類清單</a>

</body>
</html>
