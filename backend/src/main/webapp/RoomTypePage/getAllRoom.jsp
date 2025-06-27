<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, roomType.model.RoomType"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<title>房型清單</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

<style>
.word-wrap {
	max-width: 200px;
	word-wrap: break-word;
	white-space: normal;
}

img {
	object-fit: cover;
}

#backToTopBtn {
	display: none;
	position: fixed;
	bottom: 30px;
	right: 30px;
	z-index: 999;
	width: 48px;
	height: 48px;
	border: none;
	outline: none;
	background-color: #e0e0e0;
	color: white;
	cursor: pointer;
	border-radius: 50%; /* 完美圓形 */
	font-size: 20px;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
	transition: background-color 0.3s;
	text-align: center;
	line-height: 48px; /* 垂直置中 */
	padding: 0;
}

#backToTopBtn:hover {
	background-color: #999; /* 滑過變深灰 */
}

.btn btn-secondary mt-4 {
	margin-top: 40px; /* ↑ 增加上方間距，讓按鈕往上 */
	margin-bottom: 30px; /* 保持與底部距離 */
}
</style>
</head>
<body class="bg-light">

	<div class="container mt-5">
		<div
			class="header-bar d-flex justify-content-between align-items-center mb-3">
			<h2 class="mb-0">所有房型列表</h2>
			<a href="${pageContext.request.contextPath}/RoomTypePage/addRoom.jsp"
				class="btn btn-primary"> <i class="fa-solid fa-square-plus"></i>
				新增房型
			</a>
		</div>
		<%
		List<RoomType> roomList = (List<RoomType>) request.getAttribute("roomList");
		%>

		<table class="table table-bordered table-striped">
			<thead class="table-warning text-center">
				<tr>
					<th>ID</th>
					<th>名稱</th>
					<th>費用</th>
					<th>容納人數</th>
					<th>描述</th>
					<th>特色設施</th>
					<th>圖片</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody class="text-center align-middle">
				<%
				if (roomList != null && !roomList.isEmpty()) {
					for (RoomType room : roomList) {
				%>
				<tr>
					<td><%=room.getId()%></td>
					<td><%=room.getName()%></td>
					<td>$<%=room.getPrice()%></td>
					<td><%=room.getCapacity()%> 人</td>
					<td class="word-wrap"><%=room.getDescription()%></td>
					<td class="word-wrap"><%=room.getSpecialFeatures()%></td>
					<td><img src="<%= room.getImageUrl() %>" alt="房型圖片"
						width="200" height="150"
						onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/img/default.png';">
					</td>
					<td>
						<div class="d-flex flex-column gap-2">
							<!-- 修改按鈕 -->
							<form action="${pageContext.request.contextPath}/Room"
								method="get">
								<input type="hidden" name="action" value="edit"> <input
									type="hidden" name="roomId" value="<%=room.getId()%>">
								<button type="submit" class="btn btn-success btn-sm w-100">
									<i class="fa-solid fa-pencil"></i> 修改
								</button>
							</form>

							<!-- 刪除按鈕 -->
							<form action="${pageContext.request.contextPath}/DeleteRoom"
								method="get" onsubmit="return confirm('確定要刪除這筆資料嗎？');">
								<input type="hidden" name="id" value="<%=room.getId()%>">
								<button type="submit" class="btn btn-danger btn-sm w-100">
									<i class="fa-solid fa-trash"></i> 刪除
								</button>
							</form>
						</div>
					</td>
				</tr>
				<%
				}
				} else {
				%>
				<tr>
					<td colspan="8" class="text-muted">目前尚無任何房型資料。</td>
				</tr>
				<%
				}
				%>
			</tbody>
		</table>
<div class="text-left my-5">

		<a href="RoomTypePage/roomType.html"
			class="btn btn-secondary mt-4">回查詢首頁</a> <br>
			</div>
		<button onclick="scrollToTop()" id="backToTopBtn" title="回到最上面">
			<i class="fa-solid fa-up-long"></i>
		</button>
	</div>
	<script>
		// 滾動時顯示按鈕
		window.onscroll = function() {
			const btn = document.getElementById("backToTopBtn");
			if (document.body.scrollTop > 200
					|| document.documentElement.scrollTop > 200) {
				btn.style.display = "block";
			} else {
				btn.style.display = "none";
			}
		};

		// 點擊回頂部
		function scrollToTop() {
			window.scrollTo({
				top : 0,
				behavior : 'smooth'
			});
		}
	</script>

</body>
</html>