package member.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.bean.BackendUser;
import member.service.BackendUserService;

import java.io.IOException;


@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private BackendUserService userService = new BackendUserService();


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		
		// 空值檢查
		if (userId == null 
		    || userId.isBlank()     
		    || password    == null 
		    || password.isBlank()) {
			  // 跳回登入頁或顯示錯誤訊息
			  request.setAttribute("error", "帳號或密碼不得為空白 !");
			  request.getRequestDispatcher("/loginPage.jsp").forward(request, response);
			  return;
		}
		
		// 將 userId 轉換為 int
		int id;
		try {
		  id = Integer.parseInt(userId.trim());
		} catch (NumberFormatException e) {
		  // userId 不是純數字，發出警告
		  request.setAttribute("error", "請輸入員工編號 !");
		  request.getRequestDispatcher("/loginPage.jsp").forward(request, response);
		  return;
		}
		
		// 發給 service 做密碼驗證
		BackendUser loginResult =  userService.login(id, password);
		if (loginResult != null && loginResult.isActive()) {
			request.getSession().setAttribute("user", loginResult);
			System.out.println("目前使用者: " + loginResult.getUserName() + " 權限為: " + loginResult.getRole());
			response.sendRedirect(request.getContextPath() + "/backHome.html");
		} else if (loginResult != null && !loginResult.isActive()) {
			  request.setAttribute("error", "帳號已停權，請聯絡管理員 !");
			  request.getRequestDispatcher("/loginPage.jsp").forward(request, response);
		} 
		else {
			  request.setAttribute("error", "帳號或密碼錯誤 !");
			  request.getRequestDispatcher("/loginPage.jsp").forward(request, response);
		}

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
