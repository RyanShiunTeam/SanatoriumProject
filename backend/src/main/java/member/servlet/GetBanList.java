package member.servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.bean.BackendUser;
import member.service.BackendUserService;


@WebServlet("/GetBanList")
public class GetBanList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private BackendUserService userService = new BackendUserService();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		List<BackendUser> banList = userService.getBanList();
		request.setAttribute("banList", banList);
		request.getRequestDispatcher("/MemberPage/empBanPage.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
