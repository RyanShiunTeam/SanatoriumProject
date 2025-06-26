<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, activity.bean.Activity" %>
<html>
<head>
    <title>查詢結果</title>
</head>
<body>
<h2>查詢結果</h2>
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
        if (list != null && !list.isEmpty()) {
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
    </tr>
    <%
            }
        } else {
    %>
    <tr><td colspan="10">查無資料</td></tr>
    <%
        }
    %>
</table>
<div style="text-align: center;">
    <a href="${pageContext.request.contextPath}/ActivityPage/findActivity.jsp">返回查詢</a>
</div>
</body>
</html>
