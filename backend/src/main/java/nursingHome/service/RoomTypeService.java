package nursingHome.service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import nursingHome.dao.RoomTypeDao;
import nursingHome.dao.model.RoomType;

public class RoomTypeService {
    private RoomTypeDao dao;

    public RoomTypeService(Connection conn) {
        this.dao = new RoomTypeDao(conn);
    }

    public void addRoomType(RoomType room) throws SQLException {
        dao.insertRoomType(room);
    }

    public List<RoomType> getAllRoomTypes() throws SQLException {
        return dao.getAllRoomTypes();
    }

    public RoomType getRoomTypeById(int id) throws SQLException {
        return dao.getRoomTypeById(id);
    }

    public void updateRoomType(RoomType room) throws SQLException {
        dao.updateRoomType(room);
    }

    public void deleteRoomType(int id) throws SQLException {
        dao.deleteRoomType(id);
    }

    // 查價格區間
    public List<RoomType> getRoomTypesByPriceRange(int min, int max) throws SQLException {
        return dao.getRoomTypesByPriceRange(min, max);
    }

    // 查描述模糊關鍵字
    public List<RoomType> getRoomTypesByDescriptionKeyword(String keyword) throws SQLException {
        return dao.getRoomTypesByDescriptionKeyword(keyword);
    }
    
    public void importRoomTypesFromCSV(String csvPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
            String line;
            reader.readLine(); // 跳過標題列

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) continue; // 欄位不足就跳過

                RoomType room = new RoomType();
                room.setName(parts[0].trim());
                room.setPrice(Integer.parseInt(parts[1].trim()));
                room.setArea(Double.parseDouble(parts[2].trim()));
                room.setDescription(parts[3].trim());
                room.setSpecialFeatures(parts[4].trim());
                room.setImageUrl(parts[5].trim());

                dao.insertRoomType(room);
            }
            System.out.println("房型資料已成功匯入！");
        } catch (IOException | SQLException e) {
            System.err.println("匯入房型失敗：" + e.getMessage());
        }
    }

}
