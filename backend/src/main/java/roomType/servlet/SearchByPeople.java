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

@SuppressWarnings("serial")
@WebServlet("/SearchByPeople")
public class SearchByPeople extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String capacityStr = request.getParameter("capacity");

            // 檢查是否有填寫
            if (capacityStr == null || capacityStr.isEmpty()) {
                response.sendRedirect("GetAllRoom?error=請輸入人數");
                return;
            }

            int capacity = Integer.parseInt(capacityStr);

            RoomTypeService service = new RoomTypeService();
            List<RoomType> roomList = service.getRoomTypesByCapacity(capacity);

            request.setAttribute("roomList", roomList);
            request.getRequestDispatcher("/RoomTypePage/getAllRoomClient.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("GetAllRoom?error=人數格式錯誤");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("GetAllRoom?error=查詢失敗：" + e.getMessage());
        }
    }
}