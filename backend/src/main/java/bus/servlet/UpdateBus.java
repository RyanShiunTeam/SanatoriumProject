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
@WebServlet("/UpdateBus")
public class UpdateBus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BusDAO dao;

	@Override
	public void init() throws ServletException {
		try {
			dao = new BusDAO();
		} catch (SQLException e) {
			throw new ServletException(e);

		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String busIdStr = request.getParameter("busId");
		if (busIdStr == null || busIdStr.trim().isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/GetAllBus");
			return;
		}

		int busId;

		try {
			busId = Integer.parseInt(busIdStr.trim());

		} catch (NumberFormatException e) {
			response.sendRedirect(request.getContextPath() + "/GetAllBus");
			return;
		}

		try {
			RehaBus bus = dao.findById(busId);

			// null檢查
			if (bus == null) {
				response.sendRedirect(request.getContextPath() + "/GetAllBus");
				return;
			}
			request.setAttribute("bus", bus);
			request.getRequestDispatcher("/BusPage/updateBus.jsp").forward(request, response);
		
		} catch (SQLException e) {
			throw new ServletException("資料庫讀取錯誤", e);
		}
	}

	// 真正處理更新
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String busIdStr = request.getParameter("busId");
		String seatStr = request.getParameter("seatCapacity");
		String wheelStr = request.getParameter("wheelchairCapacity");

		// 基本參數檢查
		if (busIdStr == null || seatStr == null || wheelStr == null || seatStr.trim().isEmpty()
				|| wheelStr.trim().isEmpty()) {
			request.setAttribute("error", "請填寫所有欄位");
			// 把使用者輸入傳回去
			request.setAttribute("bus", buildPartialBus(busIdStr, seatStr, wheelStr));
			request.getRequestDispatcher("/BusPage/updateBus.jsp").forward(request, response);
			return;

		}

		try {
			int busId = Integer.parseInt(busIdStr.trim());
			int seatCap = Integer.parseInt(seatStr.trim());
			int wheelCap = Integer.parseInt(wheelStr.trim());

			RehaBus bus = new RehaBus();
			bus.setBusId(busId);
			bus.setSeatCapacity(seatCap);
			bus.setWheelchairCapacity(wheelCap);

			boolean ok = dao.updateBus(bus);
			if (ok) {
				response.sendRedirect(request.getContextPath() + "/GetAllBus");

			} else {
				request.setAttribute("error", "更新失敗，請稍後再試");
				request.setAttribute("bus", bus);
				request.getRequestDispatcher("/BusPage/updateBus.jsp").forward(request, response);

			}
		} catch (NumberFormatException e) {
			request.setAttribute("error", "數字格式錯誤");
			request.setAttribute("bus", buildPartialBus(busIdStr, seatStr, wheelStr));
			request.getRequestDispatcher("/BusPage/updateBus.jsp").forward(request, response);

		} catch (SQLException e) {
			throw new ServletException("資料庫錯誤:" + e.getMessage(), e);

		}
	}

	private RehaBus buildPartialBus(String busIdStr, String seatStr, String wheelStr) {
		RehaBus bus = new RehaBus();
		try {
			bus.setBusId(Integer.parseInt(busIdStr));
		} catch (Exception e) {

		}

		try {
			bus.setSeatCapacity(Integer.parseInt(seatStr));
		} catch (Exception e) {

		}

		try {
			bus.setWheelchairCapacity(Integer.parseInt(wheelStr));
		} catch (Exception e) {

		}

		return bus;
	}
}
