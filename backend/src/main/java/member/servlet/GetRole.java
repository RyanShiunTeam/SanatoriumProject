package member.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.service.BackendUserService;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;


@WebServlet("/GetRole")
public class GetRole extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private BackendUserService userService = new BackendUserService();
	private Gson gson = new Gson();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");

		List<String> result = userService.getAllRoles();
		gson.toJson(result, response.getWriter());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
