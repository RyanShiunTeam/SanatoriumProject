<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <title>登入系統</title>
    <style>
        body {
            font-family: "Microsoft JhengHei", sans-serif;
            background-color: #f5f6fa;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }
        .login-container {
            background: #ffffff;
            padding: 30px 40px;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            width: 350px;
        }
        h2 {
            text-align: center;
            color: #333;
        }
        label {
            font-weight: bold;
            display: block;
            margin-top: 15px;
        }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .btn {
            margin-top: 20px;
            width: 100%;
            padding: 10px;
            background-color: #007BFF;
            color: white;
            font-weight: bold;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .btn:hover {
            background-color: #0056b3;
        }
        .error {
            margin-top: 15px;
            color: red;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="login-container">
    <h2>登入系統</h2>
    <form action="<%= request.getContextPath() %>/LoginServlet" method="post">
        <label for="username">帳號：</label>
        <input type="text" id="username" name="username" required>

        <label for="password">密碼：</label>
        <input type="password" id="password" name="password" required>

        <button type="submit" class="btn">登入</button>

        <%
    String error = request.getParameter("error");
    if (error != null) {
	%>
    <div class="error" style="color: red; margin-bottom: 10px;">❌ 登入失敗，請檢查帳號或密碼</div>
	<%
    }
		%>
    </form>
</div>
</body>
</html>
