package caregiver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import caregiver.bean.CaregiverBean;
import utils.HikariCputil;

public class CaregiverDAO {

    // 新增照服員
    public void insertCaregiver(CaregiverBean bean) throws Exception {
        String sql = "INSERT INTO caregivers (chineseName, gender, phone, email, experience_years, certifications, photo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = HikariCputil.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, bean.getChineseName());
            stmt.setString(2, bean.getGender());
            stmt.setString(3, bean.getPhone());
            stmt.setString(4, bean.getEmail());
            stmt.setInt(5, bean.getExperienceYears());
            stmt.setString(6, bean.getCertifications());
            stmt.setString(7, bean.getPhoto());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int caregiverId = rs.getInt(1);
                    insertCaregiverServices(caregiverId, bean.getServiceNames(), conn);
                }
            }
        }
    }

    // 修改照服員
    public void updateCaregiver(CaregiverBean bean) throws Exception {
        String sql = "UPDATE caregivers SET chineseName=?, gender=?, phone=?, email=?, experience_years=?, certifications=?, photo=? " +
                     "WHERE caregiver_id=?";

        try (Connection conn = HikariCputil.getDataSource().getConnection();
        		PreparedStatement stmt = conn.prepareStatement(sql)) {
        	
                stmt.setString(1, bean.getChineseName());
                stmt.setString(2, bean.getGender());
                stmt.setString(3, bean.getPhone());
                stmt.setString(4, bean.getEmail());
                stmt.setInt(5, bean.getExperienceYears());
                stmt.setString(6, bean.getCertifications());
                stmt.setString(7, bean.getPhoto());
                stmt.setInt(8, bean.getCaregiverId());
                stmt.executeUpdate();
            

            deleteCaregiverServices(bean.getCaregiverId(), conn);
            insertCaregiverServices(bean.getCaregiverId(), bean.getServiceNames(), conn);
        }
    }

    // 查詢單筆照服員
    public CaregiverBean getCaregiverById(int id) throws Exception {
        String sql = "SELECT * FROM caregivers WHERE caregiver_id = ?";
        CaregiverBean bean = null;

        try (Connection conn = HikariCputil.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    bean = extractCaregiver(rs, conn);
                }
            }
        }

        return bean;
    }

    // 查詢全部照服員
    public List<CaregiverBean> getAllCaregivers() throws Exception {
        String sql = "SELECT * FROM caregivers";
        List<CaregiverBean> list = new ArrayList<>();

        try (Connection conn = HikariCputil.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(extractCaregiver(rs, conn));
            }
        }

        return list;
    }

    // 抽取共用欄位
    private CaregiverBean extractCaregiver(ResultSet rs, Connection conn) throws SQLException {
        CaregiverBean bean = new CaregiverBean();
        int id = rs.getInt("caregiver_id");

        bean.setCaregiverId(id);
        bean.setChineseName(rs.getString("chineseName"));
        bean.setGender(rs.getString("gender"));
        bean.setPhone(rs.getString("phone"));
        bean.setEmail(rs.getString("email"));
        bean.setExperienceYears(rs.getInt("experience_years"));
        bean.setCertifications(rs.getString("certifications"));
        bean.setPhoto(rs.getString("photo"));
        bean.setServiceNames(getServiceNamesByCaregiverId(id, conn));

        return bean;
    }

    // 查詢服務名稱清單
    private List<String> getServiceNamesByCaregiverId(int caregiverId, Connection conn) throws SQLException {
        String sql = "SELECT s.serviceChineseName FROM caregiver_services cs " +
                     "JOIN serviceType s ON cs.service_id = s.service_id WHERE cs.caregiver_id = ?";
        List<String> services = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, caregiverId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    services.add(rs.getString("serviceChineseName"));
                }
            }
        }

        return services;
    }

    // 新增關聯服務（中文轉ID）
    private void insertCaregiverServices(int caregiverId, List<String> serviceNames, Connection conn) throws SQLException {
        if (serviceNames == null) return;
        String queryId = "SELECT service_id FROM serviceType WHERE serviceChineseName = ?";
        String insert = "INSERT INTO caregiver_services (caregiver_id, service_id) VALUES (?, ?)";

        try (PreparedStatement queryStmt = conn.prepareStatement(queryId);
             PreparedStatement insertStmt = conn.prepareStatement(insert)) {
            for (String name : serviceNames) {
                queryStmt.setString(1, name);
                try (ResultSet rs = queryStmt.executeQuery()) {
                    if (rs.next()) {
                        int serviceId = rs.getInt("service_id");
                        insertStmt.setInt(1, caregiverId);
                        insertStmt.setInt(2, serviceId);
                        insertStmt.addBatch();
                    }
                }
            }
            insertStmt.executeBatch();
        }
    }

    // 刪除舊關聯
    private void deleteCaregiverServices(int caregiverId, Connection conn) throws SQLException {
        String sql = "DELETE FROM caregiver_services WHERE caregiver_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, caregiverId);
            stmt.executeUpdate();
        }
    }
}
