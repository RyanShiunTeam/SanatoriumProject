package caregiver.service;

import caregiver.bean.Caregiver;
import caregiver.dao.CaregiverDAO;

import java.sql.SQLException;
import java.util.List;

public class CaregiverService {

    private CaregiverDAO caregiverDAO;

    public CaregiverService() {
        caregiverDAO = new CaregiverDAO();
    }

    // 查詢全部照服員
    public List<Caregiver> getAllCaregivers() {
        try {
            return caregiverDAO.getAllCaregivers();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // 模糊名字查詢
    public List<Caregiver> getCaregiversByName(String chineseName) {
        try {
            return caregiverDAO.getCaregiversByName(chineseName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // 依 ID 查詢照服員
    public Caregiver getCaregiverById(int id) {
        try {
            return caregiverDAO.getCaregiverById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 新增照服員
    public boolean addCaregiver(Caregiver caregiver) {
        try {
            return caregiverDAO.addCaregiver(caregiver);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新照服員
    public boolean updateCaregiver(Caregiver caregiver) {
        try {
            return caregiverDAO.updateCaregiver(caregiver);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 刪除照服員
    public boolean deleteCaregiverById(int id) {
        try {
            return caregiverDAO.deleteCaregiverById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
