package bus.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import bus.bean.RehaBus;
import bus.service.BusService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/GetAllBus")
public class GetAllBus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private BusService busService = new BusService();
	private Gson gson = new Gson();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		
		
		// 取得所有巴士資料		
		List<RehaBus> busList = busService.findAllBus();
		
		
		Map<String, Object> result = new HashMap<>();
		if (busList != null && !busList.isEmpty()) {
			result.put("find", true);
			result.put("busList", busList);
		} else {
			result.put("find", false);
			result.put("message", "沒有找到任何巴士資料");
		}
		
		gson.toJson(result, response.getWriter());
	}

			
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
