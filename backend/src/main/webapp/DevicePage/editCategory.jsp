<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="device.bean.DeviceCategory" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>編輯分類</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background-color: #f9f9f9;
        }
        .container {
            background-color: white;
            padding: 30px;
            max-width: 500px;
            border: 1px solid #ccc;
        }
        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
        }
        input {
            width: 100%;
            padding: 6px;
            margin-top: 6px;
            box-sizing: border-box;
        }
        .btn {
            margin-top: 25px;
            background-color: #28a745;
            color: white;
            padding: 8px 16px;
            border: none;
            cursor: pointer;
            border-radius: 4px;
        }
        .btn:hover {
            background-color: #218838;
        }
        .error {
            color: red;
            margin-bottom: 10px;
        }
        a {
            display: inline-block;
            margin-top: 20px;
            color: #007BFF;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>編輯分類</h2>
    
    <!-- 分類資料檢查與顯示 -->
    <%
        DeviceCategory category = (DeviceCategory) request.getAttribute("category");
        if (category == null) {
    %>
        <p class="error">⚠ 找不到分類資料，請從清單頁重新進入。</p>
        <a href="<%= request.getContextPath() %>/CategoryServlet">← 回分類清單</a>
    <%
            return;
        }
    %>

	<!--  編輯表單內容 -->
    <form method="get" action="<%= request.getContextPath() %>/CategoryEditServlet">
        <input type="hidden" name="id" value="<%= category.getId() %>">

        <label for="name">分類名稱：</label>
        <input type="text" id="name" name="name" value="<%= category.getName() %>" required>

        <label for="categoryId">排序位置：</label>
		<input type="number" id="categoryId" name="categoryId" value="<%= category.getCategoryId() %>" required>

        <button type="submit" class="btn">更新</button>
    </form>
    <!--  返回連結 -->
    <a href="<%= request.getContextPath() %>/CategoryServlet">← 回分類清單</a>
</div>
</body>
</html>
