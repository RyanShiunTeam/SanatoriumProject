<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>新增復康巴士資料</title>
</head>
<body>
    <h2>新增復康巴士資料</h2>
	
    <!-- 顯示操作結果訊息 param.success 會取出 URL 或表單中名為 success 的參數值（第一個）-->
  <c:if test="${not empty error}">
    <div style="color:#00EC00">${error}</div>
  </c:if>
  <c:if test="${not empty message}">
    <div style="color:#FF0000">${message}</div>
  </c:if>

    <!-- 表單：送到 InsertBus Servlet ${pageContext.request.contextPath} 會展開成你的應用根路徑-->
    <form action="${pageContext.request.contextPath}/InsertBus" method="post">
        <table>
           
            <tr>
                <td>車行</td>
                <td><input type="text" name="carDealership" required></td>
            </tr>
            <tr>
                <td>汽車廠牌</td>
                <td><input type="text" name="busBrand" required></td>
            </tr>
             <tr>
                <td>型號</td>
                <td><input type="text" name="busModel" required></td>
            </tr>
             <tr>
                <td>一般座位</td>
                <td><input type="text" name="seatCapacity" required></td>
            </tr>
             <tr>
                <td>輪椅座位</td>
                <td><input type="text" name="wheelchairCapacity" required></td>
            </tr>
            <tr>
                <td>車牌號碼</td>
                <td><input type="text" name="licensePlate" required></td>
            </tr>
            
            
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="新增巴士">
                </td>
            </tr>
        </table>
    </form>

    <!-- 返回查詢按鈕 -->
    <a href="${pageContext.request.contextPath}/BusPage/querybus.html">
      <button type="button">返回查詢</button>
    </a>
</body>
</html>
