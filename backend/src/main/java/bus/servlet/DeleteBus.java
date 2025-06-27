package bus.servlet;

import java.io.IOException;


import java.sql.SQLException;


import bus.dao.BusDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//DAO(負責跟資料庫取資料)
@WebServlet("/DeleteBus")
public class DeleteBus extends HttpServlet {
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

	// GET & POST 共用同一套邏輯
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
        System.out.println("成功");
		String busIdStr = request.getParameter("busId");

		// 如果沒有填 empno，視為「初次進入表單頁面」→ 不執行新增
		if (busIdStr == null || busIdStr.trim().isEmpty()) {
			request.getRequestDispatcher("/BusPage/getBus.jsp").forward(request, response);
			
			return;
		}

		// 轉型成 int（可加 try/catch 做錯誤處理）
		int busId;
		try {
            busId = Integer.parseInt(busIdStr.trim());
		} catch (NumberFormatException e) {
			request.setAttribute("error", "車輛編號必須為數字");
			request.getRequestDispatcher("/BusPage/getBus.jsp").forward(request, response);
			return;
		}

		
		// 呼叫DAO
		try {
			boolean deleted = dao.deleteBus(busId);
			if (deleted) {
				response.sendRedirect(request.getContextPath() + "/GetAllBus");
				return;
				//加上return，結束doGet
			
			} else {
				response.sendRedirect(request.getContextPath() + "/BusPage/getBus.jsp");
			}

		} catch (SQLException se) {
			log("資料庫操作失敗", se);
			request.setAttribute("error", "資料庫操作失敗");

		} catch (Exception e) {
			log("系統異常", e);
			request.setAttribute("error", "系統發生未知的錯誤");
		}

		// 帶訊息回同一張刪除表單
		request.getRequestDispatcher("/BusPage/getBus.jsp").forward(request, response);
	}

	// 讓 POST 也走 doGet
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
