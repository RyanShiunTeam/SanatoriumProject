<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>新增活動</title></head>
<body>
<h2>新增活動</h2>
<form method="post" action="../AddActivityServlet">
    名稱：<input type="text" name="name" required/><br/>
    分類：<input type="text" name="category" required/><br/>
    名額：<input type="number" name="limit" required/><br/>
    日期：<input type="date" name="date" required/><br/>
    時間：<input type="text" name="time" required/><br/>
    地點：<input type="text" name="location" required/><br/>
    講師：<input type="text" name="instructor" required/><br/>
    狀態：
    <select name="status">
        <option value="true">可報名</option>
        <option value="false">已額滿</option>
    </select><br/>
    說明：<br/>
    <textarea name="description" rows="4" cols="40" required></textarea><br/><br/>
    <input type="submit" value="送出"/>
    <br><br>
<div style="text-align: center;">
    <a href="${pageContext.request.contextPath}/ActivityPage/findActivity.jsp">返回查詢</a>
    
    <a href="${pageContext.request.contextPath}/ListActivityServlet">查詢全部</a>
</div>
</form>
</body>
</html>
