<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>查詢活動</title>
</head>
<body>
<h2>查詢活動</h2>
<form method="get" action="../QueryActivityServlet">
    請輸入活動名稱：<input type="text" name="name"/>
    <input type="submit" value="查詢"/>
</form>
<br><br>
<div style="text-align: center;">
    <a href="${pageContext.request.contextPath}/ListActivityServlet">查詢全部</a>
    <a href="../backHome.html">返回首頁</a>
</div>
</body>
</html>
