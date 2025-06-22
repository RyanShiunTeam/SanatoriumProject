package member.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.bean.BackendUser;
import member.service.BackendUserService;


@WebServlet("/AddEmp")
public class AddEmp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BackendUserService userService = new BackendUserService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
	
		// 前端接收員工資料
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String email = request.getParameter("email");

		
		BackendUser newEmp = new BackendUser();
		newEmp.setUserName(userName);
		newEmp.setPassWord(password);
		newEmp.setEmail(email);
		boolean succeess = userService.addBackendUser(newEmp);
		request.setAttribute("success", succeess);
		request.getRequestDispatcher("/MemberPage/addPage.jsp").forward(request, response);

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
