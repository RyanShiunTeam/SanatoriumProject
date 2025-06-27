package bus.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import bus.bean.RehaBus;
import bus.dao.BusDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//DAO(負責跟資料庫取資料)
@WebServlet("/FilterBus")
public class FilterBus extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Integer minSeats = parseIntOrNull(request.getParameter("minSeats"));
		Integer maxSeats = parseIntOrNull(request.getParameter("maxSeats"));
		Integer minWheels = parseIntOrNull(request.getParameter("minWheels"));
		Integer maxWheels = parseIntOrNull(request.getParameter("maxWheels"));
		Integer busId = parseIntOrNull(request.getParameter("busId"));

		try {

			BusDAO dao = new BusDAO();
			List<RehaBus> list = dao.findByFilter(minSeats, maxSeats, minWheels, maxWheels, busId);

			 request.setAttribute("bus", list);
	            request.getRequestDispatcher("/BusPage/getAllBus.jsp")
	                   .forward(request, response);

	        } catch (SQLException e) {
	            e.printStackTrace();
	            response.sendError(
	                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
	                "執行模糊查詢失敗"
	            );
	        }
	    }

	

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	 private Integer parseIntOrNull(String num) {
	        if (num == null || num.isBlank()) 
	        	return null;
	        try {
	            return Integer.valueOf(num.trim());
	        } catch (NumberFormatException e) {
	            return null;
	        }
	    }
	}
