package bus.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import bus.bean.RehaBus;
import bus.service.BusService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.EmpService;

@WebServlet("/DeleteBus")
public class DeleteBus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	


	private BusService busService = new BusService();
	private Gson gson = new Gson();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");

		try {
			RehaBus deleteBus = gson.fromJson(request.getReader(), RehaBus.class);
			Map<String, Object> result = new HashMap<>();
			boolean success = busService.deleteBus(deleteBus.getBusId());
			if (success) {
				result.put("success", true);
				result.put("message", "復康巴士刪除成功");
			} else {
				result.put("success", false);
				result.put("message", "復康巴士刪除失敗，請稍後再試");
			}
			
			Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
			if (success && loginUserId != null) {
				new EmpService().record(loginUserId, "刪除巴士", deleteBus.getBusId());
			}
			
			gson.toJson(result, response.getWriter());
		} catch (Exception e) {
			Map<String, Object> errorResult = new HashMap<>();
			errorResult.put("success", false);
			errorResult.put("message", "發生錯誤，請稍後再試");
			gson.toJson(errorResult, response.getWriter());
		}
	}
}
