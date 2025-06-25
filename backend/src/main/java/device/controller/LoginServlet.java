package device.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 取得帳號與密碼參數
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 預設帳號密碼：TINA / 1234（可改成資料庫查詢）
        if ("TINA".equalsIgnoreCase(username) && "1234".equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", username);

         // 導向設備主頁（首頁）
            response.sendRedirect(request.getContextPath() + "/DeviceServlet");
        } else {
            response.sendRedirect(request.getContextPath() + "/DevicePage/login.jsp?error=1");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
