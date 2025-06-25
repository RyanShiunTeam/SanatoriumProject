package roomType.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import member.bean.BackendUser;
import member.service.BackendUserService;
import roomType.model.RoomType;
import roomType.service.RoomTypeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomType.model.RoomType;
import roomType.service.RoomTypeService;

import java.io.IOException;

@WebServlet("/SearchByPrice")
public class SearchByPrice extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String range = request.getParameter("range"); // ex: "15000-20000"

			int min = 0;
			int max = Integer.MAX_VALUE;

			if (range != null && range.contains("-")) {
				String[] parts = range.split("-");
				if (parts.length == 2) {
					min = Integer.parseInt(parts[0]);
					max = Integer.parseInt(parts[1]);
				}
			}

			RoomTypeService service = new RoomTypeService();
			List<RoomType> roomList = service.getRoomTypesByPriceRange(min, max);

			request.setAttribute("roomList", roomList);
			request.getRequestDispatcher("/RoomTypePage/getAllRoomClient.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("GetAllRoom?error=查詢價格失敗：" + e.getMessage());
		}
	}
}