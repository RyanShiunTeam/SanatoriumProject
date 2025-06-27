package roomType.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

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

@WebServlet("/Room")
public class Room extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("edit".equals(action)) {
            // 處理點「修改」按鈕
            String roomIdStr = request.getParameter("roomId");
            try {
                int id = Integer.parseInt(roomIdStr);
                RoomTypeService service = new RoomTypeService();
                RoomType room = service.getRoomTypeById(id);
                if (room != null) {
                    request.setAttribute("room", room);
                    request.getRequestDispatcher("/RoomTypePage/editRoom.jsp").forward(request, response);
                } else {
                    response.sendRedirect("GetAllRoom?error=查無房型");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("GetAllRoom?error=參數錯誤：" + e.getMessage());
            }
        } else {
            // 你可以未來處理其他 action，例如 add、delete 等
            response.sendRedirect("GetAllRoom");
        }
    }
}