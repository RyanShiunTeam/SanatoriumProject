<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>修改復康巴士資料</title>
</head>
<body>
	<h2>修 改 復 康 巴 士 資 料</h2>

	<!-- 用 not empty 判斷字串是否存在／非空 -->
	<c:if test="${not empty message}">
		<div style="color: #28FF28">${message}</div>
	</c:if>
	<c:if test="${not empty error}">
		<div style="color: #FF0000">${error}</div>
	</c:if>

	<form method="post"
		action="${pageContext.request.contextPath}/UpdateBus">
		<input type="hidden" name="busId" value="${bus.busId}" />
		<table>
			<tr>
				<td>一般座位</td>
				<td><input type="number" name="seatCapacity"
					value="${bus.seatCapacity}" required min="0" /></td>
			</tr>
			<tr>
				<td>輪椅座位</td>
				<td><input type="number" name="wheelchairCapacity"
					value="${bus.wheelchairCapacity}" required min="0" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit"
					value="修改巴士"></td>
			</tr>
		</table>
	</form>

	<!-- 返回查詢按鈕 -->
	<a href="${pageContext.request.contextPath}/BusPage/querybus.html">
		<button type="button">返回查詢</button>
	</a>
</body>
</html>
