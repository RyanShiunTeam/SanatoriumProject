package bus.servlet;

import java.io.IOException;

import bus.bean.RehaBus;
import bus.dao.BusDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//DAO(負責跟資料庫取資料)
@WebServlet("/GetBus")
public class GetBus extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String busIdStr = request.getParameter("busId");
		RehaBus bus = null;
		String error = null;

		if (busIdStr == null || busIdStr.isBlank()) {
			error = "請輸入要查詢的巴士編號";

		} else {

			try {
				int busId = Integer.parseInt(busIdStr.trim());
				BusDAO dao = new BusDAO();
				bus = dao.findById(busId);

				if (bus == null) {
					error = "查無編號為 " + busId + " 的復康巴士資料";
				}
			} catch (NumberFormatException e) {
				error = "巴士編號必須是整數";

			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "查詢巴士資料失敗");
				return;
			}
		}

		// 傳資料給jsp
		request.setAttribute("bus", bus);
		request.setAttribute("error", error);

		request.getRequestDispatcher("/BusPage/getBus.jsp").forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
