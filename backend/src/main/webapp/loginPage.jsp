<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head><title>員工登入</title></head>
<body>
  <h2>員工登入</h2>
  <form method="post" action="${pageContext.request.contextPath}/Login">
    員工編號：<input type="text" name="userId"/><br/>
    密碼：<input type="password" name="password"/><br/>
    <!-- 用來顯示錯誤訊息 -->
    <c:if test="${not empty error}">
      <p style="color:red">${error}</p>
    </c:if>
    <input type="submit" value="登入" />
  </form>
</body>
</html>