package member.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

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
    private Gson gson = new Gson();
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		
		List<BackendUser> banList = userService.getBanList();
		Map<String ,Object> result = new HashMap<>();
		if (banList != null && !banList.isEmpty()) {
			result.put("find", true);
			result.put("banList", banList);
		} else {
			result.put("find", false);
			result.put("message", "目前沒有人被停權喔 !");
		}
		
		gson.toJson(result, response.getWriter());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
