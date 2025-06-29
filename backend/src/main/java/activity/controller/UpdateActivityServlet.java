package activity.controller;

import activity.bean.Activity;
import activity.service.ActivityService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import utils.EmpService;

import java.io.IOException;

import com.google.gson.Gson;

@WebServlet("/UpdateActivityServlet")
public class UpdateActivityServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ActivityService activityService = new ActivityService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		
		Activity updateActivity = gson.fromJson(request.getReader(), Activity.class);
		Boolean success = activityService.updateActivity(updateActivity);
		String result = success ? "更新成功" : "更新失敗，請稍後再試";
		
		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
		if (success && loginUserId != null) {
			new EmpService().record(loginUserId, "修改活動", updateActivity.getId());
		}
		
		gson.toJson(result, response.getWriter());
    }

    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	doGet(request, response);
    }
}
