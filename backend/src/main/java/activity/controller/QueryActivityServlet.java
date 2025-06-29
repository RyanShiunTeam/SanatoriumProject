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

@WebServlet("/QueryActivityServlet")
public class QueryActivityServlet extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
    private ActivityService activityService = new ActivityService();
    private Gson gson = new Gson();

    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");    	
		
		List<Activity> activityList = activityService.getAllActivities();
		
		Map<String, Object> result = new HashMap<>();
		if (activityList != null && !activityList.isEmpty()) {
			result.put("find", true);
			result.put("activityList", activityList);
		} else {
			result.put("find", false);
			result.put("message", "沒有找到任何活動資料");
		}

		gson.toJson(result, response.getWriter());
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	doGet(request, response);
    }
}
