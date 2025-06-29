package member.servlet;

import java.io.IOException;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.bean.BackendUser;
import member.service.BackendUserService;
import utils.EmpService;


@WebServlet("/AddEmp")
public class AddEmp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BackendUserService userService = new BackendUserService();
	private Gson gson = new Gson();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
	
		BackendUser newEmp = gson.fromJson(request.getReader(), BackendUser.class);
		
		String userName = newEmp.getUserName();
		String password = newEmp.getPassWord();
		String email = newEmp.getEmail();

		Boolean success = userService.addBackendUser(userName, password, email);
		String result = success? "新增成功" : "新增失敗";
		
		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
		if (success && loginUserId != null) {
			new EmpService().record(loginUserId, "新增員工", null);
		}
		
        gson.toJson(result, response.getWriter());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
