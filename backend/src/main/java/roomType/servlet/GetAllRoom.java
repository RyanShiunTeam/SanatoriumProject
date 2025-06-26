package roomType.servlet;

import java.io.IOException;
import java.util.List;


import member.bean.BackendUser;
import member.service.BackendUserService;
import roomType.model.RoomType;
import roomType.service.RoomTypeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GetAllRoom")
public class GetAllRoom extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
	
		RoomTypeService service = new RoomTypeService();
		try {
			List<RoomType> roomList = service.getAllRoomTypes();
			request.setAttribute("roomList", roomList);
			request.getRequestDispatcher("/RoomTypePage/getAllRoom.jsp").forward(request, response);		
			System.out.println("測試");
			}catch (Exception e) {
			e.printStackTrace();
		    request.setAttribute("error", "查詢失敗：" + e.getMessage());
		    request.getRequestDispatcher("error.jsp").forward(request, response);
		}
		}




	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
