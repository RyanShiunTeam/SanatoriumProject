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
import member.bean.EmpLog;
import utils.EmpService;


@WebServlet("/GetLog")
public class GetLog extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EmpService empService = new EmpService();
	private Gson gson = new Gson();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		
		List<EmpLog> empLogs = empService.getAllLog();
		
		Map<String, Object> result = new HashMap<>();
		if (empLogs != null && !empLogs.isEmpty()) {
			result.put("find", true);
			result.put("empList", empLogs);
		} else {
			result.put("find", false);
			result.put("message", "尚無紀錄");
		}
		
		gson.toJson(result, response.getWriter());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
