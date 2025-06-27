<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>查詢單一復康巴士資料</title>
</head>
<body>
	<h2>查詢單一復康巴士資料</h2>

	<!-- 顯示操作結果訊息 -->

	<c:if test="${not empty error}">
		<div style="color: #FF0000">${error}</div>
	</c:if>


	<c:if test="${not empty bus}">

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

			<tr>
				<td>${bus.busId}</td>
				<td>${bus.carDealership}</td>
				<td>${bus.busBrand}</td>
				<td>${bus.busModel}</td>
				<td>${bus.seatCapacity}</td>
				<td>${bus.wheelchairCapacity}</td>
				<td>${bus.licensePlate}</td>

			</tr>
		</table>
	</c:if>
	

	<!-- 返回查詢按鈕 -->
	
	<br />
	<a href="${pageContext.request.contextPath}/BusPage/querybus.html">
		<button>回首頁</button>
	</a>
</body>
</html>
