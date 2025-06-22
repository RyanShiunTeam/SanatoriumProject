package member.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.bean.BackendUser;
import member.service.BackendUserService;


@WebServlet("/UpdateEmp")
public class UpdateEmp extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private BackendUserService userService = new BackendUserService();
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
	
		// 前端接收員工資料
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String passWord = request.getParameter("passWord");
		String email = request.getParameter("email");
		String role = request.getParameter("role");

		int id = Integer.parseInt(userId);
		BackendUser userinfo = new BackendUser();
		userinfo.setUserId(id);
		userinfo.setUserName(userName);
		userinfo.setPassWord(passWord);
		userinfo.setEmail(email);
		userinfo.setRole(role);
		boolean succeess = userService.updateBackendUser(userinfo);
		

		request.setAttribute("success", succeess);
		request.getRequestDispatcher("/MemberPage/updatePage.jsp").forward(request, response);

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
