package roomType.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import member.bean.BackendUser;
import member.service.BackendUserService;
import roomType.model.RoomType;
import roomType.service.RoomTypeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomType.model.RoomType;
import roomType.service.RoomTypeService;

import java.io.IOException;

@WebServlet("/SearchByKeyword")
public class SearchByKeyword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String keyword = request.getParameter("keyword");

            RoomTypeService service = new RoomTypeService();
            List<RoomType> roomList = service.getRoomTypesByDescriptionKeyword(keyword);

            request.setAttribute("roomList", roomList);
            request.getRequestDispatcher("/RoomTypePage/getAllRoomClient.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("GetAllRoom?error=查詢關鍵字失敗：" + e.getMessage());
        }
    }
}