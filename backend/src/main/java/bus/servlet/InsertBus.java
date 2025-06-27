package bus.servlet;

import java.io.IOException;

import java.sql.SQLException;

import bus.bean.RehaBus;
import bus.dao.BusDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//DAO(負責跟資料庫取資料)
@WebServlet("/InsertBus")
public class InsertBus extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
request.getRequestDispatcher("/BusPage/insertBus.jsp").forward(request, response);

	}

		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			doGet(request, response);
		
		// 封裝成Bean，再丟給DAO
		RehaBus rehabus = new RehaBus();
		rehabus.setCarDealership(request.getParameter("carDealership"));
		rehabus.setBusBrand(request.getParameter("busBrand"));
		rehabus.setBusModel(request.getParameter("busModel"));
		rehabus.setSeatCapacity(Integer.parseInt(request.getParameter("seatCapacity")));
		rehabus.setWheelchairCapacity(Integer.parseInt(request.getParameter("wheelchairCapacity")));
		rehabus.setLicensePlate(request.getParameter("licensePlate"));
		request.setAttribute("bus", rehabus);
		
		// 呼叫DAO
		try {
			BusDAO dao = new BusDAO();

			if (dao.insertBus(rehabus)) {
				response.sendRedirect(request.getContextPath() + "/GetAllBus");
				return;
			
			} else {
				request.setAttribute("error", "新增失敗，請檢查輸入資料。");
			}

		} catch (SQLException se) {
			request.setAttribute("error","資料庫錯誤:" + se.getMessage());
		
		} catch (Exception e) {
			request.setAttribute("error", "系統發生錯誤：" + e.getMessage());
		}
		
		request.getRequestDispatcher("/BusPage/insertBus.jsp").forward(request, response);

	}
    
}

