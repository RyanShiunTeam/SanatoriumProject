<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, activity.bean.Activity" %>
<html>
<head>
    <title>全部活動列表</title>
</head>
<body>
<h2>全部活動</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>名稱</th>
        <th>分類</th>
        <th>人數上限</th>
        <th>日期</th>
        <th>時間</th>
        <th>地點</th>
        <th>講師</th>
        <th>狀態</th>
        <th>描述</th>
    </tr>
    <%
        List<Activity> list = (List<Activity>) request.getAttribute("activityList");
        if (list != null) {
            for (Activity act : list) {
    %>
    <tr>
        <td><%= act.getId() %></td>
        <td><%= act.getName() %></td>
        <td><%= act.getCategory() %></td>
        <td><%= act.getLimit() %></td>
        <td><%= act.getDate() %></td>
        <td><%= act.getTime() %></td>
        <td><%= act.getLocation() %></td>
        <td><%= act.getInstructor() %></td>
        <td><%= act.isStatus() ? "可報名" : "已額滿" %></td>
        <td><%= act.getDescription() %></td>
        <td>
			<a href="${pageContext.request.contextPath}/UpdateActivityServlet?id=<%= act.getId() %>">修改</a>

			<a href="${pageContext.request.contextPath}/DeleteActivityServlet?id=<%= act.getId() %>" onclick="return confirm('確定要刪除這筆活動嗎？')">刪除</a>			
		</td>
			
    </tr>
    <%
            }
        }
    %>
</table>



<br><br>
<div style="text-align: center;">
    <a href="${pageContext.request.contextPath}/ActivityPage/findActivity.jsp">返回查詢</a>
    
    <a href="${pageContext.request.contextPath}/ActivityPage/addActivity.jsp">新增活動</a>
</div>

</body>
</html>

</body>
</html>
