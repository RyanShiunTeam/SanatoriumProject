<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="device.bean.Device" %>
<%@ page import="device.bean.DeviceCategory" %>
<%@ page import="device.service.DeviceService" %>
<%@ page import="device.service.DeviceCategoryService" %>

<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>批次編輯輔具</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 30px;
            background-color: #f9f9f9;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background-color: #fff;
            margin-top: 20px;
        }
        th, td {
            padding: 10px;
            text-align: center;
            border-bottom: 1px solid #ccc;
        }
        th {
            background-color: #f0f0f0;
        }
        input[type="text"], input[type="number"], select {
            width: 95%;
            padding: 4px;
            border-radius: 4px;
            border: 1px solid #ccc;
            box-sizing: border-box;
        }
        .submit-btn {
            margin-top: 20px;
            padding: 10px 16px;
            background-color: #007BFF;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .submit-btn:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<h2>批次修改輔具資料</h2>

<!-- 送出資料到DeviceBatchUpdateServlet -->
<form method="post" action="<%= request.getContextPath() %>/DeviceBatchUpdateServlet">
    <table>
        <tr>
            <th>ID</th>
            <th>名稱</th>
            <th>SKU</th>
            <th>單價</th>
            <th>庫存</th>
            <th>描述</th>
            <th>圖片路徑</th>
            <th>是否上架</th>
            <th>分類</th>
        </tr>

        <%
            String[] ids = request.getParameterValues("id"); // 勾選要批次編輯的 ID
            DeviceService deviceService = DeviceService.getInstance();
            DeviceCategoryService categoryService = DeviceCategoryService.getInstance();
            List<DeviceCategory> categoryList = categoryService.getAllCategories();

            if (ids != null) {
                for (String idStr : ids) {
                    int id = Integer.parseInt(idStr);
                    Device device = deviceService.getDeviceById(id);
                    if (device != null) {
        %>
        <!-- 每筆設備欄位（input[] 格式，送出陣列） -->
        <tr>
            <td>
                <%= device.getId() %>
                <input type="hidden" name="id" value="<%= device.getId() %>">
            </td>
            <td><input type="text" name="name" value="<%= device.getName() %>"></td>
            <td><input type="text" name="sku" value="<%= device.getSku() %>"></td>
            <td><input type="number" step="0.01" name="unitPrice" value="<%= device.getUnitPrice() %>"></td>
            <td><input type="number" name="inventory" value="<%= device.getInventory() %>"></td>
            <td><input type="text" name="description" value="<%= device.getDescription() != null ? device.getDescription() : "" %>"></td>
            <td><input type="text" name="image" value="<%= device.getImage() != null ? device.getImage() : "" %>"></td>
            <td>
                <select name="isOnline">
                    <option value="true" <%= device.isOnline() ? "selected" : "" %>>是</option>
                    <option value="false" <%= !device.isOnline() ? "selected" : "" %>>否</option>
                </select>
            </td>
            <td>
                <select name="categoryId">
                    <%
                        for (DeviceCategory category : categoryList) {
                            boolean selected = device.getCategory() != null && category.getId() == device.getCategory().getId();
                    %>
                        <option value="<%= category.getId() %>" <%= selected ? "selected" : "" %>>
                            <%= category.getName() %>
                        </option>
                    <%
                        }
                    %>
                </select>
            </td>
        </tr>
        <%
                    }
                }
            } else {
        %>
        <tr><td colspan="9">請從清單頁選擇要批次編輯的輔具。</td></tr>
        <%
            }
        %>
    </table>
    <!--表單送出按鈕） -->
    <button type="submit" class="submit-btn">送出修改</button>
</form>
</body>
</html>
