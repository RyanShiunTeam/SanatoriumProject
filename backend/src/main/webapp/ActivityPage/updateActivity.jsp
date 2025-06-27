<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="activity.bean.Activity" %>
<%
    Activity act = (Activity) request.getAttribute("activity");
%>
<html>
<head>
    <title>修改活動</title>
</head>
<body>
<h2>修改活動</h2>
<form method="post" action="${pageContext.request.contextPath}/UpdateActivityServlet">

    <input type="hidden" name="id" value="<%= act.getId() %>"/><br/>
    名稱: <input type="text" name="name" value="<%= act.getName() %>" required/><br/>
    分類: <input type="text" name="category" value="<%= act.getCategory() %>" required/><br/>
    上限: <input type="number" name="limit" value="<%= act.getLimit() %>" required/><br/>
    日期: <input type="date" name="date" value="<%= act.getDate() %>" required/><br/>
    時間: <input type="text" name="time" value="<%= act.getTime() %>" required/><br/>
    地點: <input type="text" name="location" value="<%= act.getLocation() %>" required/><br/>
    講師: <input type="text" name="instructor" value="<%= act.getInstructor() %>" required/><br/>
    狀態: 
    <select name="status">
        <option value="true" <%= act.isStatus() ? "selected" : "" %>>可報名</option>
        <option value="false" <%= !act.isStatus() ? "selected" : "" %>>已額滿</option>
    </select><br/>
    描述: <textarea name="description" required><%= act.getDescription() %></textarea><br/><br/>
    <input type="submit" value="確定修改"/>
</form>
</body>
</html>
