package nursingHome.dao.model;

import java.util.List;
import java.util.UUID;

import java.sql.Timestamp;

public class ViewingAppointment {
    private int id;
    private int memberId;
    private int roomId;
    private Timestamp appointmentDate; // 對應 SQL Server 的 DATETIME
    private String status;
    private String notes;

    public ViewingAppointment() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getRoomId() {
        return roomId;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Timestamp getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(Timestamp appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
}