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


@WebServlet("/CheckRole")
public class CheckRole extends HttpServlet {
	private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        BackendUser currentUser = (session != null)? (BackendUser) session.getAttribute("user") : null;

        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            if (currentUser != null && "Admin".equals(currentUser.getRole())) {
                out.write("{\"allowed\":true}");
            } else {
                out.write("{\"allowed\":false,\"error\":\"權限不足\"}");
            }
        }
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
