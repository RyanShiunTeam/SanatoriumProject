<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, roomType.model.RoomType"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<title>房型清單</title>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<style>
.word-wrap {
	max-width: 250px;
	word-wrap: break-word;
	white-space: normal;
}

img {
	object-fit: cover;
}

.modal {
	display: none;
	position: fixed;
	z-index: 999;
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
	background-color: rgba(0, 0, 0, 0.6);
	padding-top: 60px;
}

.modal-content {
	position: relative;
	background-color: #fff;
	margin: auto;
	padding: 20px 20px 20px 20px; 
	border-radius: 8px;
	width: fit-content;
	max-width: 90%;
	text-align: center;
}

.modal .close {
  position: absolute;
  top: 10px;
  right: 12px;
  color: #aaa;
  font-size: 26px;
  font-weight: bold;
  cursor: pointer;
  z-index: 10;
  padding: 4px 10px; 
  background-color: #fff; 
  border-radius: 50%;
}

.modal .close:hover {
  color: #e76f51;
  background-color: #f2f2f2;
}
</style>
</head>
<body class="bg-light">

	<div class="container mt-5">
		<h2 class="mb-4">所有房型列表</h2>


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
					<td><img class="preview-img" src="<%= room.getImageUrl() %>"
						alt="房型圖片" width="200" height="150"
						data-full="<%= room.getImageUrl() %>"
						onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/img/default.png';">
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

		<a href="/Sanatorium-backend/RoomTypePage/roomType.html"
			class="btn btn-secondary mt-4">回查詢首頁</a> 
	</div>
	<!-- 圖片放大 Modal -->
	<div id="imgModal" class="modal" style="display: none;">
		<div class="modal-content">
			<span class="close">&times;</span> <img id="modalPreviewImg" src=""
				alt="放大圖片" style="max-width: 100%; max-height: 80vh;">
		</div>
	</div>
	<script>
		// 圖片點擊 → 顯示 modal
		$(document).ready(function() {
			$('.preview-img').on('click', function() {
				const fullSrc = $(this).data('full');
				$('#modalPreviewImg').attr('src', fullSrc);
				$('#imgModal').fadeIn();
			});

			$('.modal .close').on('click', function() {
				$('#imgModal').fadeOut();
			});

			$(window).on('click', function(e) {
				if (e.target.id === 'imgModal') {
					$('#imgModal').fadeOut();
				}
			});
		});
	</script>

</body>
</html>