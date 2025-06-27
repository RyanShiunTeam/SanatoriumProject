package activity.controller;

import activity.dao.ActivityDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/DeleteActivityServlet")
public class DeleteActivityServlet extends HttpServlet {
    private final ActivityDao dao = new ActivityDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String did = request.getParameter("id");
        System.out.print(did);
        if (did != null) {
            int id = Integer.parseInt(did);
            boolean success = dao.delete(id);
            if (success) {
                System.out.println("刪除成功！");
            } else {
                System.out.println("刪除失敗！");
            }
        }

        // 刪除，回到列表頁
        response.sendRedirect("ListActivityServlet");
    }
}
