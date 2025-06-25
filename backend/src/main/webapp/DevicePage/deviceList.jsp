<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="device.bean.Device" %>
<%@ page import="device.bean.DeviceCategory" %>

<!--  登入檢查區塊 -->
<%
    String user = (String) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>輔具清單</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        img.device-image {
            width: 80px;
            height: auto;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .table-container {
            box-shadow: 0 0 5px rgba(0,0,0,0.1);
            background-color: white;
            padding: 20px;
            border-radius: 8px;
        }
    </style>
</head>
<body class="bg-light">
<div class="container my-4">
    <h2 class="mb-4">輔具清單</h2>

<!--  上方提示訊息區:分類 -->
    <% Integer categoryId = (Integer) request.getAttribute("categoryId");
       if (categoryId != null) {
           try {
               DeviceCategory currentCategory = device.service.DeviceCategoryService.getInstance().getCategoryById(categoryId);
               if (currentCategory != null) {
    %>
    <div class="alert alert-info">📂 目前分類：<%= currentCategory.getName() %></div>
    <% } } catch (Exception e) { %>
    <div class="alert alert-warning">⚠️ 載入分類名稱失敗：<%= e.getMessage() %></div>
    <% } } %>

<!--  上方提示訊息區:訊息提示 -->
    <% String status = request.getParameter("status");
       if ("deleted".equals(status)) {
    %><div class="alert alert-success">✅ 刪除成功！</div><%
       } else if ("updated".equals(status)) {
    %><div class="alert alert-success">✅ 修改成功！</div><%
       } else if ("created".equals(status)) {
    %><div class="alert alert-success">✅ 新增成功！</div><%
       } else if ("batchUpdated".equals(status)) {
    %><div class="alert alert-success">✅ 批次修改成功！</div><%
       } %>

    <form method="get" action="<%= request.getContextPath() %>/DeviceServlet" class="row g-3 mb-4">
        <input type="hidden" name="action" value="searchByName">
        <div class="col-md-4">
            <input type="text" id="name" name="name" class="form-control" placeholder="輸入輔具名稱" required>
        </div>
        <div class="col-md-auto">
            <button type="submit" class="btn btn-primary">🔍 查詢</button>
        </div>
    </form>

<!--  查詢與操作按鈕列-->
    <div class="mb-3 d-flex flex-wrap gap-2">
        <a href="<%= request.getContextPath() %>/DeviceServlet?action=toAdd" class="btn btn-success">➕ 新增輔具</a>
        <a href="<%= request.getContextPath() %>/DeviceServlet" class="btn btn-secondary">📋 查詢全部輔具</a>
        <a href="<%= request.getContextPath() %>/CategoryServlet" class="btn btn-secondary">📂 查詢全部分類</a>
        <a href="<%= request.getContextPath() %>/DeviceExportServlet" class="btn btn-info">⬇️ 匯出 CSV</a>
        <a href="<%= request.getContextPath() %>/DevicePage/importDevice.jsp" class="btn btn-warning">⬆️ 匯入 CSV</a>
        <a href="<%= request.getContextPath() %>/LogoutServlet" class="btn btn-danger">🚪 登出</a>
    </div>

    <form method="get" action="<%= request.getContextPath() %>/DeviceServlet">
        <input type="hidden" name="action" value="deleteBatch">

        <div class="table-container">
            <table class="table table-bordered align-middle">
                <thead class="table-light">
                <tr>
                    <th><input type="checkbox" onclick="toggleSelectAll(this)"> 全選</th>
                    <th>ID</th>
                    <th>名稱</th>
                    <th>SKU</th>
                    <th>分類</th>
                    <th>價格</th>
                    <th>圖片</th>
                    <th>庫存</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <% List<Device> deviceList = (List<Device>) request.getAttribute("deviceList");
                   if (deviceList != null && !deviceList.isEmpty()) {
                       for (Device device : deviceList) { %>
                <tr>
                    <td><input type="checkbox" name="id" value="<%= device.getId() %>"></td>
                    <td><%= device.getId() %></td>
                    <td><%= device.getName() %></td>
                    <td><%= device.getSku() %></td>
                    <td><%= device.getCategory() != null ? device.getCategory().getName() : "未分類" %></td>
                    <td><%= device.getUnitPrice() %></td>
                    <td>
                        <% if (device.getImage() != null && !device.getImage().isEmpty()) { %>
                        <img src="<%= request.getContextPath() + device.getImage() %>" class="device-image" alt="圖片">
                        <% } else { %>無<% } %>
                    </td>
                    <td><%= device.getInventory() %></td>
                    <td>
                        <a href="<%= request.getContextPath() %>/DeviceServlet?action=edit&id=<%= device.getId() %>" class="btn btn-sm btn-outline-success">編輯</a>
                        <a href="<%= request.getContextPath() %>/DeviceServlet?action=deleteSingle&id=<%= device.getId() %>" onclick="return confirm('確定刪除？')" class="btn btn-sm btn-outline-danger">刪除</a>
                    </td>
                </tr>
                <% } } else { %>
                <tr><td colspan="9">目前沒有資料</td></tr>
                <% } %>
                </tbody>
            </table>
        </div>
		 <!--  分頁導覽區 -->
        <% Integer currentPage = (Integer) request.getAttribute("currentPage");
           Integer totalPages = (Integer) request.getAttribute("totalPages");
           if (currentPage != null && totalPages != null && totalPages > 1) { %>
        <nav class="mt-3">
            <ul class="pagination">
                <% for (int i = 1; i <= totalPages; i++) { %>
                    <li class="page-item <%= (i == currentPage) ? "active" : "" %>">
                        <a class="page-link" href="<%= request.getContextPath() %>/DeviceServlet?page=<%= i %>"><%= i %></a>
                    </li>
                <% } %>
            </ul>
        </nav>
        <% } %>

		<!--  批次操作區 -->
        <div class="mt-3 d-flex gap-2">
            <input type="submit" class="btn btn-danger" value="🗑️ 批次刪除" onclick="return confirm('確定要刪除選取項目嗎？')">
            <button type="button" class="btn btn-warning" onclick="submitBatchEdit()">✏️ 批次修改</button>
        </div>
    </form>

    <a class="btn btn-link mt-4" href="<%= request.getContextPath() %>/DevicePage/deviceSearch.html">← 回查詢頁</a>
</div>


<!--  JS功能 toggleSelectAll()：勾選／取消全選。  -->
<!--  JS功能 submitBatchEdit()：送出勾選的項目 ID 給 JSP 做修改。  -->
    function toggleSelectAll(checkbox) {
        const checkboxes = document.querySelectorAll('input[name="id"]');
        checkboxes.forEach(cb => cb.checked = checkbox.checked);
    }
    function submitBatchEdit() {
        const checkedBoxes = document.querySelectorAll('input[name="id"]:checked');
        if (checkedBoxes.length === 0) {
            alert("請至少選取一筆資料進行批次修改！");
            return false;
        }
        let form = document.createElement("form");
        form.method = "post";
        form.action = "<%= request.getContextPath() %>/jsp/batchEdit.jsp";
        checkedBoxes.forEach(box => {
            let hidden = document.createElement("input");
            hidden.type = "hidden";
            hidden.name = "id";
            hidden.value = box.value;
            form.appendChild(hidden);
        });
        document.body.appendChild(form);
        form.submit();
    }
</script>
</body>
</html>
