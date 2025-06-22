<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>停權名單</title>
	  <script>
    /**
     * 以 Ajax 呼叫恢復權限 Servlet，成功後從表格移除該列
     * @param userId 目標員工 ID
     */
    function restoreUser(userId, btn) {
      fetch('${pageContext.request.contextPath}/EnableEmp?userID=' + userId, {
        method: 'POST'
      })
      .then(res => res.json())
      .then(data => {
        if (data.success) {
          alert('已恢復該員工權限 !');
          // 移除按鈕所在的表格列
          const row = btn.closest('tr');
          row.remove();
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

<div align="center">

	<h2>員工資料</h2>

	<table border="1">
        <tr style="background-color:#a8fefa">
            <th>員工 ID</th>
            <th>姓名</th>
            <th>電子郵件</th>
            <th>權限</th>
            <th>停權日期</th>
            <th>恢復權限</th>
        </tr>
		<c:forEach items="${banList}" var="e">
			<tr>
			    <td>${e.userId}</td>
                <td>${e.userName}</td>
                <td>${e.email}</td>
                <td>${e.role}</td>
                <td>${e.updatedAt}</td>
		        <td>
		          	<!-- 以 button 觸發 Ajax -->
		          	<button type="button" onclick="restoreUser(${e.userId}, this)">恢復權限</button>
		        </td>
			</tr>
        </c:forEach>
	</table>
	<span>
		<a href="${pageContext.request.contextPath}/MemberPage/queryEmp.html">
      		<button type="button">返回查詢</button>
    	</a>
	</span>
</div>

</body>
</html>