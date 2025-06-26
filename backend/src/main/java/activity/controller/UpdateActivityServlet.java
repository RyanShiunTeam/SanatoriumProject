package activity.controller;

import activity.bean.Activity;
import activity.dao.ActivityDao;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/UpdateActivityServlet")
public class UpdateActivityServlet extends HttpServlet {
    private final ActivityDao dao = new ActivityDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Activity act = dao.findById(id);

        request.setAttribute("activity", act);
        if (act == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "查無此活動資料");
            return;
        }
        request.getRequestDispatcher("/ActivityPage/updateActivity.jsp").forward(request, response);
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Activity act = new Activity();
        act.setId(Integer.parseInt(request.getParameter("id")));
        act.setName(request.getParameter("name"));
        act.setCategory(request.getParameter("category"));
        act.setLimit(Integer.parseInt(request.getParameter("limit")));
        act.setDate(request.getParameter("date"));
        act.setTime(request.getParameter("time"));
        act.setLocation(request.getParameter("location"));
        act.setInstructor(request.getParameter("instructor"));
        act.setStatus(Boolean.parseBoolean(request.getParameter("status")));
        act.setDescription(request.getParameter("description"));

        boolean success = dao.update(act);
        if (success) {
            response.sendRedirect("ListActivityServlet");
        } else {
            response.getWriter().println("<h2>修改失敗</h2>");
        }
    }
}
