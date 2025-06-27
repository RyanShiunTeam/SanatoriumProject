package roomType.servlet;

import java.io.IOException;
import java.sql.SQLException;
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

@WebServlet("/DeleteRoom")
public class DeleteRoom extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
	
		response.setContentType("text/html;charset=UTF-8");
		String idParam = request.getParameter("id");
		
		 if (idParam != null && !idParam.trim().isEmpty()) {
			 try {
				    int id = Integer.parseInt(idParam);
				    RoomTypeService service = new RoomTypeService();
				    boolean success = service.deleteRoomType(id);
				    if (success) {
				        response.sendRedirect(request.getContextPath() + "/GetAllRoom");
				    } else {
				        response.sendRedirect(request.getContextPath() + "/GetAllRoom?error=刪除失敗，找不到該房型");
				    }
				} catch (NumberFormatException e) {
				    response.sendRedirect(request.getContextPath() + "/GetAllRoom?error=ID格式錯誤：" + idParam);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        } else {
	            response.sendRedirect(request.getContextPath() + "/GetAllRoom?error=未提供有效的ID參數");
	        }
	    }

		
		



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
