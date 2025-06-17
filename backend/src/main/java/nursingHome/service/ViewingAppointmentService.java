package nursingHome.service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import nursingHome.dao.ViewingAppointmentDao;
import nursingHome.dao.model.ViewingAppointment;

public class ViewingAppointmentService {
    private ViewingAppointmentDao dao;

    public ViewingAppointmentService(Connection conn) {
        this.dao = new ViewingAppointmentDao(conn);
    }

    // 新增預約
    public void addAppointment(ViewingAppointment appointment) throws SQLException {
        dao.insertAppointment(appointment);
    }

    // 查詢全部預約
    public List<ViewingAppointment> getAllAppointments() throws SQLException {
        return dao.getAllAppointments();
    }

    // 查詢單筆預約
    public ViewingAppointment getAppointmentById(int id) throws SQLException {
        return dao.getAppointmentById(id);
    }

    // 修改預約
    public void updateAppointment(ViewingAppointment appointment) throws SQLException {
        dao.updateAppointment(appointment);
    }

    // 刪除預約
    public void deleteAppointment(int id) throws SQLException {
        dao.deleteAppointment(id);
    }
    
    public void importAppointmentsFromCSV(String csvPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
            String line;
            reader.readLine(); // 跳過標題列

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) continue; // 欄位不足

                ViewingAppointment appt = new ViewingAppointment();
                appt.setMemberId(Integer.parseInt(parts[0].trim()));
                appt.setRoomId(Integer.parseInt(parts[1].trim()));
                appt.setAppointmentDate(Timestamp.valueOf(parts[2].trim()));
                appt.setNotes(parts[3].trim());

                dao.insertAppointment(appt);
            }

            System.out.println(" 預約資料已成功匯入！");
        } catch (IOException | SQLException e) {
            System.err.println("匯入預約資料失敗：" + e.getMessage());
        }
    }
    
    
}
