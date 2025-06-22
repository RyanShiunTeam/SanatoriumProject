<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>員工資料</title>
	<script>
    /**
     * 檢查 Admin 權限，若通過則執行對應動作；失敗顯示錯誤
     * @param {number} userId 目標員工 ID
     * @param {string} actionType 'edit' 或 'disable'
     */
    function checkPermission(userId, actionType) {
      fetch('${pageContext.request.contextPath}/CheckRole?userID=' + userId)
        .then(response => response.json())
        .then(data => {
          if (data.allowed) {
            if (actionType === 'edit') {
              // 跳轉到載入更新資料的 Servlet
              window.location.href = '${pageContext.request.contextPath}/GetEmpByID?userID=' + userId;
            } else if (actionType === 'disable') {
              // 確認是否真的要停權
           	  if (!confirm("確定要將他停權嗎 ？")) {
                   return;
              }
              // 呼叫停權 Servlet
              fetch('${pageContext.request.contextPath}/BanEmp?userID=' + userId, { method: 'POST' })
                .then(res => res.json())
                .then(result => {
                  if (result.success) {
                    alert('已成功停權員工 !');
                    location.reload(); // 保留查詢結果
                  } else {
                    alert(result.error);
                  }
                });
            }
          } else {
            alert(data.error);
          }
        })
        .catch(err => {
          console.error(err);
          alert('系統錯誤，請稍後再試');
        });
    }
  </script>
</head>
<body style="background-color:#fdf5e6">
    <!-- 顯示錯誤訊息 -->
	<c:if test="${error == '權限不足'}">
	  <script>alert('權限不足');</script>
	</c:if>

<div align="center">

	<h2>員工資料</h2>

	<table border="1">
        <tr style="background-color:#a8fefa">
            <th>員工 ID</th>
            <th>姓名</th>
            <th>電子郵件</th>
            <th>權限</th>
            <th>到職日</th>
            <th>修改</th>
            <th>停權</th>
        </tr>
		<c:forEach items="${emps}" var="e">
			<tr>
			    <td>${e.userId}</td>
                <td>${e.userName}</td>
                <td>${e.email}</td>
                <td>${e.role}</td>
                <td>${e.createdAt}</td>
		        <td>
		          	<!-- 以 button 觸發 Ajax -->
		          	<button type="button" onclick="checkPermission(${e.userId}, 'edit')">修改</button>
		        </td>

                <td>
                    <!-- 以 button 觸發停權 -->
                    <button type="button" onclick="checkPermission(${e.userId}, 'disable')">停權</button>
                </td>
			</tr>
        </c:forEach>
	</table>
	<h3>共${fn:length(emps)}筆員工資料</h3>
	<span>
		<a href="${pageContext.request.contextPath}/MemberPage/queryEmp.html">
      		<button type="button">返回查詢</button>
    	</a>
	</span>
	<span>
		<c:if test="${sessionScope.user.role == 'Admin'}">
		  <button type="button"
		          onclick="location.href='${pageContext.request.contextPath}/GetBanList'">查看停權名單
		  </button>
		</c:if>
	</span>

</div>

</body>
</html>