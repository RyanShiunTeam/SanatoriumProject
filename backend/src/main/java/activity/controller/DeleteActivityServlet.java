package activity.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import activity.bean.Activity;

import activity.service.ActivityService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.EmpService;

@WebServlet("/DeleteActivityServlet")
public class DeleteActivityServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ActivityService activityService = new ActivityService();
	private Gson gson = new Gson();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");

		Activity deleteActivity = gson.fromJson(request.getReader(), Activity.class);

		Map<String, Object> result = new HashMap<>();
		boolean success = activityService.deleteActivity(deleteActivity.getId());
		if (success) {
			result.put("success", true);
			result.put("message", "活動刪除成功");
		} else {
			result.put("success", false);
			result.put("message", "活動刪除失敗，請稍後再試");
		}
		
		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
		if (success && loginUserId != null) {
			new EmpService().record(loginUserId, "刪除活動", deleteActivity.getId());
		}
		
		gson.toJson(result, response.getWriter());
	}
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	doGet(request, response);
    }
}
