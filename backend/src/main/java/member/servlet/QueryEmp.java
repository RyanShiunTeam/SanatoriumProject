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


@WebServlet("/QueryEmp")
public class QueryEmp extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    private BackendUserService userService = new BackendUserService();
    private Gson gson = new Gson();
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		
		BackendUser queryName = gson.fromJson(request.getReader(), BackendUser.class);
		
		String userName = null;
		if (queryName != null) {
			// 讀取使者輸入 
		    userName = queryName.getUserName();  
		}

		
		List<BackendUser> empList;
		// 如果沒有回傳 userName，就查詢全部
		if (userName == null) {
			empList = userService.getAllBackendUsers();
		} else {			
			empList = userService.getBackendUserByName(userName);
		}
	
		// 設定回傳資料
		Map<String, Object> result = new HashMap<>();
		if (empList != null && !empList.isEmpty()) {
			result.put("find", true);
			result.put("empList", empList);
		} else {
			result.put("find", false);
			result.put("message", "啥木都沒有");
		}
		
		
		gson.toJson(result, response.getWriter());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		doGet(request, response);
	}

}
