<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>新增分類</title>
    <!--  CSS 美化 -->
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f9f9f9; }
        .container { background-color: white; padding: 30px; max-width: 500px; border: 1px solid #ccc; }
        label { display: block; margin-top: 12px; font-weight: bold; }
        input { width: 100%; padding: 6px; margin-top: 5px; }
        .btn { margin-top: 20px; background-color: #007BFF; color: white; padding: 8px 16px; border: none; cursor: pointer; }
        .btn:hover { background-color: #0056b3; }
        a { display: inline-block; margin-top: 15px; text-decoration: none; color: #007BFF; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<div class="container">
    <h2>新增設備分類</h2>
    <!-- 表單送出到 CategoryAddServlet，透過 POST 傳送 -->
    <form method="post" action="<%= request.getContextPath() %>/CategoryAddServlet">
		
		<!-- 分類名稱 -->
        <label for="name">名稱：</label>
        <input type="text" name="name" id="name" required>

		<!-- 分類排序值（例如用來控制前端顯示順序） -->
        <label for="categoryId">排序值：</label> 
        <input type="number" name="categoryId" id="categoryId" required> 
		
		<!-- 送出按鈕 -->
        <button type="submit" class="btn">新增</button>
    </form>
    
    <!--  回上一頁（分類清單） -->
    <a href="<%= request.getContextPath() %>/CategoryServlet">← 回分類清單</a>
</div>
</body>
</html>
