package activity.controller;

import activity.bean.Activity;
import activity.dao.ActivityDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/ListActivityServlet")
public class ListActivityServlet extends HttpServlet {
    private final ActivityDao dao = new ActivityDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Activity> activityList = dao.findAll();
        request.setAttribute("activityList", activityList);
        request.getRequestDispatcher("/ActivityPage/listActivity.jsp").forward(request, response);
    }
}
