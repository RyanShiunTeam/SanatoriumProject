<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>錯誤頁面</title>
</head>
<body>
    <h2 style="color: red;">⚠ 發生錯誤</h2>
    <p><%= request.getAttribute("errorMessage") %></p>
    <a href="<%= request.getContextPath() %>/CategoryServlet">← 回分類清單</a>
</body>
</html>
