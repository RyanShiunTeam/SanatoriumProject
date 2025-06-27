<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, roomType.model.RoomType"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
    <title>新增房型</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="card shadow">
        <div class="card-header bg-primary text-white">
            <h4 class="mb-0">新增房型</h4>
        </div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/AddRoom" method="post" enctype="multipart/form-data">
                <div class="mb-3">
                    <label class="form-label">房型名稱</label>
                    <input type="text" name="name" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">價格</label>
                    <input type="number" name="price" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">描述</label>
                    <textarea name="description" class="form-control" rows="3" required></textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label">特色設施</label>
                    <input type="text" name="specialFeatures" class="form-control" placeholder="例如：冷氣、電視、簡化家具" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">容納人數</label>
                    <input type="number" name="capacity" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">上傳圖片</label>
                    <input type="file" name="imageFile" class="form-control" accept="image/*" required>
                </div>

                <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                    <button type="submit" class="btn btn-success">送出新增</button>
                    <a href="${pageContext.request.contextPath}/GetAllRoom" class="btn btn-secondary">回房型列表</a>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>