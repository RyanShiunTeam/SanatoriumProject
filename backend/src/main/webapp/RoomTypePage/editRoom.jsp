<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="roomType.model.RoomType" %>
<%
    RoomType room = (RoomType) request.getAttribute("room");
    if (room == null) {
%>
    <div class="alert alert-danger mt-5 container">找不到房型資料，請從列表點選進入。</div>
    <a href="${pageContext.request.contextPath}/GetAllRoom" class="btn btn-secondary container mt-2">返回列表</a>
<%
        return;
    }
%>
<html>
<head>
    <title>修改房型</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .form-section {
            background-color: #f8f9fa;
            padding: 2rem;
            border-radius: 16px;
            box-shadow: 0 0 10px rgba(0,0,0,0.05);
        }
        .form-label {
            font-weight: bold;
        }
        .form-control {
            border-radius: 12px;
        }
    </style>
</head>
<body class="container mt-5">

    <div class="form-section">
        <h3 class="mb-4 text-center text-primary">修改房型資料</h3>

        <form action="${pageContext.request.contextPath}/EditRoom" method="post" enctype="multipart/form-data">
            <input type="hidden" name="id" value="<%= room.getId() %>">

            <div class="mb-3">
                <label class="form-label">房型名稱</label>
                <input type="text" name="name" class="form-control" value="<%= room.getName() %>" required>
            </div>

            <div class="mb-3">
                <label class="form-label">價格</label>
                <input type="number" name="price" class="form-control" value="<%= room.getPrice() %>" required>
            </div>

            <div class="mb-3">
                <label class="form-label">人數</label>
                <input type="number" name="capacity" class="form-control" value="<%= room.getCapacity() %>" required>
            </div>

            <div class="mb-3">
                <label class="form-label">描述</label>
                <textarea name="description" class="form-control" rows="3"><%= room.getDescription() %></textarea>
            </div>

            <div class="mb-3">
                <label class="form-label">特色設施</label>
                <input type="text" name="specialFeatures" class="form-control" value="<%= room.getSpecialFeatures() %>">
            </div>

            <div class="mb-3">
                <label class="form-label">目前圖片</label><br>
                <img src="<%= room.getImageUrl() %>" width="250" height="160"
                     class="border rounded"
                     onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/img/default.png';">
            </div>

            <div class="mb-3">
                <label class="form-label">上傳新圖片（若不選則保留原圖）</label>
                <input type="file" name="imageFile" class="form-control">
            </div>

            <div class="d-flex justify-content-between">
                <button type="submit" class="btn btn-primary px-4">確認修改</button>
                <a href="${pageContext.request.contextPath}/GetAllRoom" class="btn btn-secondary">取消</a>
            </div>
        </form>
    </div>

</body>
</html>

