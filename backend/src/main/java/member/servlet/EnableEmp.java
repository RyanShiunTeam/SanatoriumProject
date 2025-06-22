package member.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.service.BackendUserService;


@WebServlet("/EnableEmp")
public class EnableEmp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private BackendUserService userService = new BackendUserService();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("userID");
        int userId = Integer.parseInt(idStr.trim());

        boolean success;
        try {
            success = userService.enableUser(userId);
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }

        response.setContentType("application/json;charset=UTF-8");
        if (success) {
            response.getWriter().write("{\"success\":true}");
        } else {
            response.getWriter().write("{\"success\":false,\"error\":\"恢復權限失敗\"}");
        }
    }
}
