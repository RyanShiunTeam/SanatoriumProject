<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>匯入輔具資料（CSV）</title>
    <style>
        body { font-family: "Microsoft JhengHei", Arial, sans-serif; margin: 40px; background-color: #f9f9f9; }
        .container { background-color: white; padding: 25px 30px; max-width: 600px; border: 1px solid #ccc; }
        h2 { color: #333; }
        input[type="file"] { margin-top: 10px; }
        input[type="submit"] {
            margin-top: 20px; background-color: #007BFF; color: white;
            border: none; padding: 8px 14px; cursor: pointer;
        }
        input[type="submit"]:hover { background-color: #0056b3; }
        .note {
            font-size: 0.9em; color: #555;
            margin-top: 8px; line-height: 1.6;
        }
        .error {
            color: red;
            margin-top: 10px;
            font-weight: bold;
        }
        a {
            display: inline-block;
            margin-top: 20px;
            text-decoration: none;
            color: #007BFF;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>匯入輔具資料（CSV）</h2>

	<!--  錯誤訊息顯示（動態區塊） -->
    <% String errorMsg = (String) request.getAttribute("errorMessage"); %>
    <% if (errorMsg != null) { %>
        <div class="error">⚠ <%= errorMsg %></div>
    <% } %>

<!--  表單設定 -->
    <form method="post" action="<%= request.getContextPath() %>/DeviceImportServlet" enctype="multipart/form-data">
        <label for="csvFile">選擇 CSV 檔案：</label><br>
        
        <!--檔案上傳欄位-->
        <input type="file" id="csvFile" name="csvFile" accept=".csv" required>

		 <!--資料格式說明（使用者指引）-->
        <p class="note">
            ※ CSV 欄位順序：<br>
            <strong>name, sku, unitPrice, inventory, description, image, isOnline, categoryId</strong><br>
            - 若 name 重複，則更新原有資料，否則新增<br>
            - isOnline 為 true / false 或 1 / 0<br>
            - image 欄位為圖片檔名或完整路徑<br>
        </p>
		
		<!--提交按鈕-->
        <input type="submit" value="開始匯入">
    </form>
	
	<!--返回清單連結-->
    <a href="<%= request.getContextPath() %>/DevicePage/deviceList.jsp">← 回輔具清單</a>
</div>
</body>
</html>
