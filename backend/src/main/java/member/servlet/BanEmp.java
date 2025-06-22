package member.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.bean.BackendUser;
import member.service.BackendUserService;


@WebServlet("/BanEmp")
public class BanEmp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private BackendUserService userService = new BackendUserService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


	}
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        BackendUser currentUser = (session != null) ? (BackendUser) session.getAttribute("user") : null;
        response.setContentType("application/json;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            if (currentUser == null || !"Admin".equals(currentUser.getRole())) {
                out.write("{\"success\":false,\"error\":\"權限不足\"}");
                return;
            }
            String idStr = request.getParameter("userID");
            int targetId = Integer.parseInt(idStr.trim());
            
            boolean ok = userService.disableUser(targetId);
            if (ok) out.write("{\"success\":true}");
            else   out.write("{\"success\":false,\"error\":\"停權失敗\"}");
        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"error\":\"系統異常\"}");
        }
    }

}
