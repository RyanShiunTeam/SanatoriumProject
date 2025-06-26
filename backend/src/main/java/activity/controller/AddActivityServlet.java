package activity.controller;

import activity.bean.Activity;
import activity.dao.ActivityDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/AddActivityServlet")
public class AddActivityServlet extends HttpServlet {
    private final ActivityDao dao = new ActivityDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 接收表單資料
        Activity act = new Activity();
        act.setName(request.getParameter("name"));
        act.setCategory(request.getParameter("category"));
        act.setLimit(Integer.parseInt(request.getParameter("limit")));
        act.setDate(request.getParameter("date"));  // 你目前是用 String 儲存
        act.setTime(request.getParameter("time"));
        act.setLocation(request.getParameter("location"));
        act.setInstructor(request.getParameter("instructor"));
        act.setStatus(Boolean.parseBoolean(request.getParameter("status")));
        act.setDescription(request.getParameter("description"));

        int id = dao.insert(act);  // 回傳成功的ID

        response.setContentType("text/html;charset=UTF-8");
        if (id > 0) {
            response.getWriter().println(
                "<h2>新增成功！ID: " + id + "</h2>" +
                "<a href='ActivityPage/addActivity.jsp'>再新增</a><br>" +
                "<a href='" + request.getContextPath() + "/ListActivityServlet'>查看全部活動</a>"
            );
        } else {
            response.getWriter().println(
                "<h2>新增失敗</h2>" +
                "<a href='ActivityPage/addActivity.jsp'>回上一頁</a>"
            );
        }
       
    }
}
