package activity.controller;

import activity.bean.Activity;
import activity.dao.ActivityDao;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/QueryActivityServlet")
public class QueryActivityServlet extends HttpServlet {
    private final ActivityDao dao = new ActivityDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
// 模糊查詢~~~
        List<Activity> list = dao.queryByName(name);  
        request.setAttribute("activityList", list);

        request.getRequestDispatcher("/ActivityPage/activityResult.jsp").forward(request, response);
    }
}
