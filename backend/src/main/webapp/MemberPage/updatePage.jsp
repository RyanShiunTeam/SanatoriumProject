<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>修改員工資料</title>
</head>
<body>
    <h2>修改員工資料</h2>
	
    <!-- 顯示操作結果訊息 param.success 會取出 URL 或表單中名為 success 的參數值（第一個）-->
  <c:if test="${success == true}">
    <div style="color:green">修改成功！</div>
  </c:if>
  <c:if test="${success == false}">
    <div style="color:red">修改失敗！</div>
  </c:if>

    <form action="${pageContext.request.contextPath}/UpdateEmp" method="post">
        <table>
            <tr>
                <td>員工編號:</td>
                <td>
                	<!-- 直接顯示員工編號 -->
                 	${employee.userId}  
                    <input type="hidden" name="userId" value="${employee.userId}">
                </td>
            </tr>
            <tr>
                <td>到職日:</td>
                <td>
                	${employee.createdAt} 
                	<input type="hidden" name="createdAt" value="${employee.createdAt}">
                </td>
            </tr>
            <tr>
                <td>姓名:</td>
                <td><input type="text" name="userName" value="${employee.userName}" required></td>
            </tr>
            <tr>
                <td>密碼:</td>
                <td><input type="password" name="passWord" value="${employee.passWord}"></td>
            </tr>
            <tr>
                <td>信箱:</td>
                <td><input type="text" name="email" value="${employee.email}" required></td>
            </tr>
            <tr>
            	<td>職等</td>
				  <td>
				    <select name="role">
				      <c:forEach var="t" items="${roles}">
				        <option value="${t}"
				          <c:if test="${t == employee.role}">selected</c:if>>
				          ${t}
				        </option>
				      </c:forEach>
				    </select>
				  </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="修改員工資料">
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
