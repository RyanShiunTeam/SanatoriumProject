package bus.servlet;

import java.io.IOException;


import com.google.gson.Gson;

import bus.bean.RehaBus;

import bus.service.BusService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.EmpService;

//DAO(負責跟資料庫取資料)
@WebServlet("/UpdateBus")
public class UpdateBus extends HttpServlet {
	private static final long serialVersionUID = 1L;


	private BusService busService = new BusService();
	private Gson gson = new Gson();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		
		RehaBus updateBus = gson.fromJson(request.getReader(), RehaBus.class);
		
		int busId = updateBus.getBusId();
		int seatCapacity = updateBus.getSeatCapacity();
		int wheelchairCapacity = updateBus.getWheelchairCapacity();
		
		Boolean success = busService.updateBus(busId, seatCapacity, wheelchairCapacity);
		
		String result = success ? "更新成功" : "更新失敗，請稍後再試";
		
		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
		if (success && loginUserId != null) {
			new EmpService().record(loginUserId, "修改巴士", busId);
		}
		
		gson.toJson(result, response.getWriter());
	}

	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
