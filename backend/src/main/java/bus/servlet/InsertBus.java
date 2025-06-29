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

//DAO(負責跟資料庫取資料)
@WebServlet("/InsertBus")
public class InsertBus extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BusService busService = new BusService();
	private Gson gson = new Gson();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		
		RehaBus addBus = gson.fromJson(request.getReader(), RehaBus.class);
		
		String carDealership = addBus.getCarDealership();
		String busBrand = addBus.getBusBrand();
		String busModel = addBus.getBusModel();
		int seatCapacity = addBus.getSeatCapacity();
		int wheelchairCapacity = addBus.getWheelchairCapacity();
		String licensePlate = addBus.getLicensePlate();
		RehaBus newBus = new RehaBus(carDealership, busBrand, busModel, seatCapacity, wheelchairCapacity, licensePlate);
		
		Boolean success = busService.createBus(newBus);
		String result = success ? "新增成功" : "新增失敗";
		
		gson.toJson(result, response.getWriter());

	}

		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			doGet(request, response);
	}
    
}

