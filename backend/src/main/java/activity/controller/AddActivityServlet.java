package activity.controller;

import activity.bean.Activity;
import activity.service.ActivityService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.EmpService;

import java.io.IOException;

import com.google.gson.Gson;

@WebServlet("/AddActivityServlet")
public class AddActivityServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private ActivityService activityService = new ActivityService();
    private Gson gson = new Gson();

    
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		
		Activity newActivity = gson.fromJson(request.getReader(), Activity.class);
		
		String name = newActivity.getName();
		String category = newActivity.getCategory();
		int limit = newActivity.getLimit();
		String date = newActivity.getDate();
		String time = newActivity.getTime();
		String location = newActivity.getLocation();
		String instructor = newActivity.getInstructor();
		boolean status = true;
		String description = newActivity.getDescription();
		
		Activity activity = new Activity(name, category, limit, date, time, location, instructor, status, description);
		
		Boolean success = activityService.addActivity(activity);
		String result = success ? "新增成功" : "新增失敗，請稍後再試";
		
		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
		if (success && loginUserId != null) {
			new EmpService().record(loginUserId, "新增活動", null);
		}
		
		gson.toJson(result, response.getWriter());

	}
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	doGet(request, response);    
    }
}
