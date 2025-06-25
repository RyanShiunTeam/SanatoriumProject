package device.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	// 取得當前 session（設定 false 表示不建立新 session）
        HttpSession session = request.getSession(false); // 不建立新 session
        if (session != null) {
            session.invalidate(); // 清除 session
        }
     // 登出後導向登入頁
        response.sendRedirect(request.getContextPath() + "/DevicePage/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
