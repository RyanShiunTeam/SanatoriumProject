<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="device.bean.DeviceCategory" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>新增輔具</title>
    <!-- Bootstrap 引入 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="card shadow-sm">
        <div class="card-header bg-primary text-white">
            <h4 class="mb-0">➕ 新增輔具</h4>
        </div>
        <div class="card-body">
        <!-- 表單送出到 DeviceAddServlet -->
            <form method="post" action="<%= request.getContextPath() %>/DeviceAddServlet?action" enctype="multipart/form-data">
                <div class="mb-3">
                    <label for="name" class="form-label">名稱</label>
                    <input type="text" class="form-control" id="name" name="name" required>
                </div>

                <div class="mb-3">
                    <label for="sku" class="form-label">SKU</label>
                    <input type="text" class="form-control" id="sku" name="sku" required>
                </div>

                <div class="mb-3">
                    <label for="unitPrice" class="form-label">單價</label>
                    <input type="number" class="form-control" id="unitPrice" name="unitPrice" step="0.01" required>
                </div>

                <div class="mb-3">
                    <label for="inventory" class="form-label">庫存</label>
                    <input type="number" class="form-control" id="inventory" name="inventory" required>
                </div>

                <div class="mb-3">
                    <label for="description" class="form-label">描述</label>
                    <input type="text" class="form-control" id="description" name="description">
                </div>

                <div class="mb-3">
                    <label for="imageFile" class="form-label">圖片上傳</label>
                    <input class="form-control" type="file" id="imageFile" name="imageFile" accept="image/*">
                    <img id="preview" class="img-thumbnail mt-2" style="display:none; max-width: 200px;" alt="預覽圖片">
                </div>
				
				 <!-- 是否上架 -->
                <div class="mb-3">
                    <label for="isOnline" class="form-label">是否上架</label>
                    <select class="form-select" name="isOnline" id="isOnline">
                        <option value="true">是</option>
                        <option value="false">否</option>
                    </select>
                </div>
				
				 <!-- 分類下拉選單（從後端取出 categoryList） -->
                <div class="mb-3">
                    <label for="categoryId" class="form-label">分類</label>
                    <select class="form-select" name="categoryId" id="categoryId" required>
                        <%
                            List<DeviceCategory> categoryList = (List<DeviceCategory>) request.getAttribute("categoryList");
                            if (categoryList != null && !categoryList.isEmpty()) {
                                for (DeviceCategory cat : categoryList) {
                        %>
                        <option value="<%= cat.getId() %>"><%= cat.getName() %></option>
                        <%
                                }
                            } else {
                        %>
                        <option disabled>無分類資料</option>
                        <% } %>
                    </select>
                </div>

				<!-- 按鈕區塊 -->
                <div class="d-flex justify-content-between">
                    <a href="<%= request.getContextPath() %>/DevicePage/deviceSearch.html" class="btn btn-outline-secondary">🔙 回查詢頁</a>
                    <button type="submit" class="btn btn-success">✅ 新增</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- 圖片預覽 JavaScript -->
<script>
    document.getElementById("imageFile").addEventListener("change", function (e) {
        const reader = new FileReader();
        reader.onload = function (event) {
            const img = document.getElementById("preview");
            img.src = event.target.result;
            img.style.display = "block";
        };
        if (e.target.files.length > 0) {
            reader.readAsDataURL(e.target.files[0]);
        }
    });
</script>

</body>
</html>
