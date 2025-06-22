<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>新增員工資料</title>
</head>
<body>
    <h2>新增員工資料</h2>
	
    <!-- 顯示操作結果訊息 param.success 會取出 URL 或表單中名為 success 的參數值（第一個）-->
  <c:if test="${success == true}">
    <div style="color:green">新增成功！</div>
  </c:if>
  <c:if test="${success == false}">
    <div style="color:red">新增失敗！</div>
  </c:if>

    <!-- 表單：送到 AddEmp Servlet ${pageContext.request.contextPath} 會展開成你的應用根路徑-->
    <form action="${pageContext.request.contextPath}/AddEmp" method="post">
        <table>
            <tr>
                <td>姓名:</td>
                <td><input type="text" name="userName" required></td>
            </tr>
            <tr>
                <td>密碼</td>
                <td><input type="password" name="password" required></td>
            </tr>
            <tr>
                <td>信箱</td>
                <td><input type="text" name="email" required></td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="新增員工">
                </td>
            </tr>
        </table>
    </form>

    <!-- 返回查詢按鈕 -->
    <a href="${pageContext.request.contextPath}/MemberPage/queryEmp.html">
      <button type="button">返回查詢</button>
    </a>
</body>
</html>
