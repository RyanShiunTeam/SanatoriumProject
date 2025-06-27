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


@WebServlet("/UpdateEmp")
public class UpdateEmp extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private BackendUserService userService = new BackendUserService();
    private Gson gson = new Gson();
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		
		BackendUser updateEmp = gson.fromJson(request.getReader(), BackendUser.class);
		
		int userID = updateEmp.getuserID();
		String userName = updateEmp.getUserName();
		String emai = updateEmp.getEmail();
		String role = updateEmp.getRole();
		System.out.println("準備修改大名: " + userName);
		Boolean success = userService.updateBackendUser(userID, userName, emai, role);
		String result = success? "修改成功" : "修改失敗";
		
        
        gson.toJson(result, response.getWriter());
		

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
