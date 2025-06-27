<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>復康巴士資料</title>
</head>
<body>
	<h2>復 康 巴 士 資 料</h2>

	<!-- 顯示操作結果訊息 -->
	<c:if test="${not empty message}">
		<div style="color: #28FF28">${message}</div>
	</c:if>
	<c:if test="${not empty error}">
		<div style="color: #FF0000">${error}</div>
	</c:if>


	<table border="1">
		<tr style="background-color: #ECECFF">

			<th>車輛編號</th>
			<th>車行</th>
			<th>汽車廠牌</th>
			<th>型號</th>
			<th>一般座位</th>
			<th>輪椅座位</th>
			<th>車牌號碼</th>
		</tr>

		<!-- 動態產生表格列:把從後端提取出來的資料，一筆筆渲染成html的<tr> -->
		<c:forEach var="bus" items="${bus}">
			<tr>
				<td>${bus.busId}</td>
				<td>${bus.carDealership}</td>
				<td>${bus.busBrand}</td>
				<td>${bus.busModel}</td>
				<td>${bus.seatCapacity}</td>
				<td>${bus.wheelchairCapacity}</td>
				<td>${bus.licensePlate}</td>

				<td><a
					href="${pageContext.request.contextPath}/UpdateBus?busId=${bus.busId}">修改</a>

					<form method="post"
						action="${pageContext.request.contextPath}/DeleteBus"
						style="display: inline;"
						onsubmit="return confirm('確定要刪除編號 ${bus.busId}嗎？');">
						<input type="hidden" name="busId" value="${bus.busId}" />
						<button type="submit">刪除</button>
					</form></td>
			</tr>
		</c:forEach>
	</table>
	<h3>共${bus.size()}筆巴士資料</h3>



	<!-- 返回查詢按鈕 -->
	<br>
	<br>
	<a href="${pageContext.request.contextPath}/BusPage/querybus.html">
		<button>回首頁</button>
	</a>
</body>
</html>
