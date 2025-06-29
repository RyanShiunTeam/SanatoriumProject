package member.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.bean.BackendUser;
import member.service.BackendUserService;
import utils.EmpService;


@WebServlet("/EnableEmp")
public class EnableEmp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private BackendUserService userService = new BackendUserService();
	private Gson gson = new Gson();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		
		BackendUser enableEmp = gson.fromJson(request.getReader(), BackendUser.class);
		
		int userID = enableEmp.getuserID();
		
		Map<String, Object> result = new HashMap<>();
		
		boolean success = userService.enableUser(userID);
		if (success) {
			result.put("success", true);
		} else {
			result.put("success", false);
			result.put("message", "恢復失敗，請聯絡管理員 !");
		}
		
		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
		if (success && loginUserId != null) {
			new EmpService().record(loginUserId, "恢復員工權限", userID);
		}
		
        gson.toJson(result, response.getWriter());
	}

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	doGet(request, response);
    }
}
