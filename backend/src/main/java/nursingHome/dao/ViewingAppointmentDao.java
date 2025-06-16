package nursingHome.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import device.dao.model.Device;
import device.dao.model.DeviceCategory;
import nursingHome.dao.model.ViewingAppointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewingAppointmentDao {
    private Connection conn;

    public ViewingAppointmentDao(Connection conn) {
        this.conn = conn;
    }

    // 新增預約
    public void insertAppointment(ViewingAppointment appointment) throws SQLException {
        String sql = "INSERT INTO ViewingAppointment (member_id, room_id, appointment_date, status, notes) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointment.getMemberId());
            ps.setInt(2, appointment.getRoomId());
            ps.setTimestamp(3, appointment.getAppointmentDate());
            ps.setString(4, appointment.getStatus());
            ps.setString(5, appointment.getNotes());
            ps.executeUpdate();
        }
    }

    // 查詢全部預約
    public List<ViewingAppointment> getAllAppointments() throws SQLException {
        List<ViewingAppointment> list = new ArrayList<>();
        String sql = "SELECT * FROM ViewingAppointment ORDER BY appointment_date";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ViewingAppointment a = new ViewingAppointment();
                a.setId(rs.getInt("id"));
                a.setMemberId(rs.getInt("member_id"));
                a.setRoomId(rs.getInt("room_id"));
                a.setAppointmentDate(rs.getTimestamp("appointment_date"));
                a.setStatus(rs.getString("status"));
                a.setNotes(rs.getString("notes"));
                list.add(a);
            }
        }
        return list;
    }

    // 根據 ID 查詢單筆預約
    public ViewingAppointment getAppointmentById(int id) throws SQLException {
        String sql = "SELECT * FROM ViewingAppointment WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ViewingAppointment a = new ViewingAppointment();
                    a.setId(rs.getInt("id"));
                    a.setMemberId(rs.getInt("member_id"));
                    a.setRoomId(rs.getInt("room_id"));
                    a.setAppointmentDate(rs.getTimestamp("appointment_date"));
                    a.setStatus(rs.getString("status"));
                    a.setNotes(rs.getString("notes"));
                    return a;
                }
            }
        }
        return null;
    }

    // 更新預約資料
    public void updateAppointment(ViewingAppointment appointment) throws SQLException {
        String sql = "UPDATE ViewingAppointment SET member_id = ?, room_id = ?, appointment_date = ?, status = ?, notes = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointment.getMemberId());
            ps.setInt(2, appointment.getRoomId());
            ps.setTimestamp(3, appointment.getAppointmentDate());
            ps.setString(4, appointment.getStatus());
            ps.setString(5, appointment.getNotes());
            ps.setInt(6, appointment.getId());
            ps.executeUpdate();
        }
    }

    // 刪除預約
    public void deleteAppointment(int id) throws SQLException {
        String sql = "DELETE FROM ViewingAppointment WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
