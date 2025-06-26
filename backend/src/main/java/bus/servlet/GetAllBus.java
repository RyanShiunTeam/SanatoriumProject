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
@WebServlet("/GetAllBus")
public class GetAllBus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BusDAO dao;
	
	@Override
	public void init() throws ServletException {
		try {
			dao = new BusDAO();
		} catch (SQLException e) {
			throw new ServletException("無法初始化BusDAO", e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 呼叫DAO，查詢所有的員工
		try {
			BusDAO dao = new BusDAO();
			List<RehaBus> bus = dao.findAllBus();
			
			//傳資料給jsp
			request.setAttribute("bus", bus);
			request.getRequestDispatcher("/BusPage/getAllBus.jsp").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"查詢巴士資料失敗");
		}
	}

			
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
