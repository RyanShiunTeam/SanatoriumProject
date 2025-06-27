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


@WebServlet("/BanEmp")
public class BanEmp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private BackendUserService userService = new BackendUserService();
    private Gson gson = new Gson();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		
		BackendUser banEmp = gson.fromJson(request.getReader(), BackendUser.class);
		
		int userID = banEmp.getuserID();
		
		Map<String, Object> result = new HashMap<>();
		if (userService.disableUser(userID)) {
			result.put("success", true);
		} else {
			result.put("success", false);
			result.put("message", "停權失敗，請聯絡管理員 !");
		}
		
        gson.toJson(result, response.getWriter());
	}
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	doGet(request, response);
    }

}
