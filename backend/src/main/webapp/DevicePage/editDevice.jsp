<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="device.bean.Device" %>
<%@ page import="device.bean.DeviceCategory" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>編輯輔具</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 40px;
        }
        .form-container {
            background-color: white;
            padding: 20px;
            max-width: 600px;
            border: 1px solid #ccc;
            box-shadow: 0 0 5px rgba(0,0,0,0.1);
        }
        label {
            display: block;
            font-weight: bold;
            margin-top: 10px;
        }
        input, select {
            width: 100%;
            padding: 6px;
            margin-top: 4px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        button {
            margin-top: 20px;
            padding: 8px 16px;
            background-color: #007BFF;
            border: none;
            color: white;
            border-radius: 4px;
        }
        button:hover {
            background-color: #0056b3;
        }
        a {
            display: inline-block;
            margin-top: 15px;
            text-decoration: none;
            color: #007BFF;
        }
        .image-preview {
            margin-top: 10px;
            max-width: 100%;
            height: auto;
            display: none;
            border: 1px solid #ccc;
            background-color: #f5f5f5;
            padding: 4px;
        }
    </style>
    
    <!--  圖片預覽 JavaScript -->
    <script>
        function previewImage(event) {
            const reader = new FileReader();
            reader.onload = function () {
                const preview = document.getElementById('preview');
                preview.src = reader.result;
                preview.style.display = 'block';
            }
            if (event.target.files.length > 0) {
                reader.readAsDataURL(event.target.files[0]);
            }
        }
    </script>
</head>
<body>
<div class="form-container">
    <h2>編輯輔具</h2>


	<!--  資料檢查與錯誤處理 -->
    <%
        Device device = (Device) request.getAttribute("device");
        List<DeviceCategory> categories = (List<DeviceCategory>) request.getAttribute("categoryList");
        if (device == null || categories == null) {
    %>
        <p style="color:red;">⚠️ 無法載入資料，請從清單頁重新進入。</p>
        <a href="<%= request.getContextPath() %>/jsp/deviceList.jsp">← 回清單</a>
        <%
            return;
        }
    %>

	<!--  編輯表單內容-->
    <form action="<%= request.getContextPath() %>/DeviceUpdateServlet" method="post" enctype="multipart/form-data">
        <!--  隱藏欄位 這兩個欄位是為了： 確認是哪一筆資料要更新 若沒上傳新圖片，保留原圖 -->
        <input type="hidden" name="id" value="<%= device.getId() %>">
        <input type="hidden" name="originalImage" value="<%= device.getImage() %>">

        <label for="name">名稱</label>
        <input type="text" id="name" name="name" value="<%= device.getName() %>" required>

        <label for="sku">SKU</label>
        <input type="text" id="sku" name="sku" value="<%= device.getSku() %>" required>

        <label for="unitPrice">單價</label>
        <input type="number" id="unitPrice" name="unitPrice" step="0.01" value="<%= device.getUnitPrice() %>" required>

        <label for="inventory">庫存</label>
        <input type="number" id="inventory" name="inventory" value="<%= device.getInventory() %>" required>

        <label for="description">描述</label>
        <input type="text" id="description" name="description" value="<%= device.getDescription() %>">

        <label for="imageFile">圖片上傳</label>
        <input type="file" id="imageFile" name="imageFile" accept="image/*" onchange="previewImage(event)">
        <img id="preview" class="image-preview"
             src="<%= (device.getImage() != null && !device.getImage().isEmpty()) ? request.getContextPath() + device.getImage() : "" %>"
             style="<%= (device.getImage() != null && !device.getImage().isEmpty()) ? "display:block;" : "display:none;" %>"
             alt="預覽圖片">

        <label for="isOnline">是否上架</label>
        <select id="isOnline" name="isOnline">
            <option value="true" <%= device.isOnline() ? "selected" : "" %>>是</option>
            <option value="false" <%= !device.isOnline() ? "selected" : "" %>>否</option>
        </select>
		
		<!--  分類下拉選單（依 categoryList 動態產生）-->
        <label for="categoryId">設備分類</label>
        <select name="categoryId" id="categoryId" required>
            <% for (DeviceCategory cat : categories) { %>
                <option value="<%= cat.getId() %>" <%= (cat.getId() == device.getCategoryId()) ? "selected" : "" %>>
                    <%= cat.getName() %>
                </option>
            <% } %>
        </select>

        <button type="submit">更新</button>
    </form>

	<!--  圖片預覽邏輯-->
    <%
        String prevPage = (String) request.getAttribute("prevPage");
        if (prevPage != null) {
    %>
        <a href="<%= prevPage %>">← 返回上一頁</a> <!--有設定 prevPage 就返回來源，否則預設回清單頁-->
    <%
        } else {
    %>
        <a href="<%= request.getContextPath() %>/DevicePage/deviceList.jsp">← 回清單</a>
    <%
        }
    %>
</div>
</body>
</html>
