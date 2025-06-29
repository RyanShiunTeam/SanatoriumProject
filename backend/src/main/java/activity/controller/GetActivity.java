package activity.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import activity.bean.Activity;
import activity.service.ActivityService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GetActivity")
public class GetActivity extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ActivityService activityService = new ActivityService();
    private Gson gson = new Gson();

   
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		
		Activity updateActivity = gson.fromJson(request.getReader(), Activity.class);

		List<Activity> activity = activityService.getActivityByName(updateActivity.getName());

		
		Map<String, Object> result = new HashMap<>();
		if (activity != null) {
			System.out.println("找到活動資料: " + activity.size());
			result.put("find", true);
			result.put("activity", activity);
		} else {
			result.put("find", false);
			result.put("message", "沒有找到活動資料");
		}
		gson.toJson(result, response.getWriter());
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
