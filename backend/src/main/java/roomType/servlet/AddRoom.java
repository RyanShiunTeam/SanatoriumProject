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
import utils.EmpService;


@WebServlet("/AddRoom")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024)
public class AddRoom extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIR = "img";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // 除錯：印出所有欄位
            System.out.println("=== Debug 輸入欄位 ===");
            System.out.println("name: " + request.getParameter("name"));
            System.out.println("price: " + request.getParameter("price"));
            System.out.println("description: " + request.getParameter("description"));
            System.out.println("capacity: " + request.getParameter("capacity"));

            // 防呆：避免 null
            String capacityStr = request.getParameter("capacity");
            if (capacityStr == null || capacityStr.trim().isEmpty()) {
                throw new IllegalArgumentException("capacity 是空的");
            }

            // 取得欄位
            String name = request.getParameter("name");
            int price = Integer.parseInt(request.getParameter("price"));
            String description = request.getParameter("description");
 
            String specialFeatures = request.getParameter("specialFeatures");
            
            int capacity = Integer.parseInt(capacityStr);

            // 圖片上傳處理
            Part imagePart = request.getPart("imageFile");
            String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();

            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String filePath = uploadPath + File.separator + fileName;
            imagePart.write(filePath);
            String imagePath = UPLOAD_DIR + "/" + fileName;

            // 組合資料
            RoomType room = new RoomType();
            room.setName(name);
            room.setPrice(price);
            room.setDescription(description);
            room.setCapacity(capacity);
            room.setSpecialFeatures(specialFeatures);
            room.setImageUrl(imagePath);

            // 新增
            RoomTypeService service = new RoomTypeService();
            service.addRoomType(room);
            
            // 記錄操作
    		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
    		if (loginUserId != null) {
    			new EmpService().record(loginUserId, "新增設施", null);
    		}

            response.sendRedirect(request.getContextPath() + "/GetAllRoom");

        } catch (Exception e) {
            e.printStackTrace();  // 印到 console
            response.getWriter().println("發生錯誤：" + e.getMessage());
        }
    }
}