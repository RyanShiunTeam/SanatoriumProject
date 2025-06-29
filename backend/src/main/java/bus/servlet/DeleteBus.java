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
			if (busService.deleteBus(deleteBus.getBusId())) {
				result.put("success", true);
				result.put("message", "復康巴士刪除成功");
			} else {
				result.put("success", false);
				result.put("message", "復康巴士刪除失敗，請稍後再試");
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
