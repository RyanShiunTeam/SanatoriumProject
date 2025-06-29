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


@WebServlet("/EditRoom")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024)
public class EditRoom extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIR = "img";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // 1. 取得並驗證欄位
            String idStr = request.getParameter("id");
            String name = request.getParameter("name");
            String priceStr = request.getParameter("price");
            String description = request.getParameter("description");
            String specialFeatures = request.getParameter("specialFeatures");
            String capacityStr = request.getParameter("capacity");

            if (idStr == null || priceStr == null || capacityStr == null ||
                idStr.trim().isEmpty() || priceStr.trim().isEmpty() || capacityStr.trim().isEmpty()) {
                throw new IllegalArgumentException("必要欄位不能為空");
            }

            int id = Integer.parseInt(idStr.trim());
            int price = Integer.parseInt(priceStr.trim());
            int capacity = Integer.parseInt(capacityStr.trim());

            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("名稱不能為空");
            }

            // 2. 查舊資料
            RoomTypeService service = new RoomTypeService();
            RoomType existing = service.getRoomTypeById(id);
            if (existing == null) {
                throw new Exception("找不到要修改的房型資料");
            }

            // 3. 處理圖片上傳（選擇性）
            Part imagePart = request.getPart("imageFile");
            String fileName = imagePart != null ? Paths.get(imagePart.getSubmittedFileName()).getFileName().toString() : "";
            String imagePath;

            if (fileName != null && !fileName.isEmpty()) {
                String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                String filePath = uploadPath + File.separator + fileName;
                imagePart.write(filePath);
                imagePath = UPLOAD_DIR + "/" + fileName;
            } else {
                imagePath = existing.getImageUrl(); // 保留原圖
            }

            // 4. 組合資料
            RoomType room = new RoomType();
            room.setId(id);
            room.setName(name.trim());
            room.setPrice(price);
            room.setCapacity(capacity);
            room.setDescription(description != null ? description.trim() : "");
            room.setSpecialFeatures(specialFeatures != null ? specialFeatures.trim() : "");
            room.setImageUrl(imagePath);

            // 5. 更新
            boolean success = service.updateRoomType(room);
            // 記錄操作
    		Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
    		if (loginUserId != null) {
    			new EmpService().record(loginUserId, "更新設施", room.getId());
    		}
    		
            if (success) {
                response.sendRedirect(request.getContextPath() + "/GetAllRoom");
            } else {
                response.sendRedirect(request.getContextPath() + "/GetAllRoom?error=err");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/GetAllRoom?error=" + e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/GetAllRoom?error=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/GetAllRoom?error=" + e.getMessage());
        }
    }
}