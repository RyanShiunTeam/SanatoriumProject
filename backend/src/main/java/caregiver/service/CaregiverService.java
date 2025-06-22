package caregiver.service;

import java.util.List;

import caregiver.bean.CaregiverBean;
import caregiver.dao.CaregiverDAO;

public class CaregiverService {
    private CaregiverDAO dao = new CaregiverDAO();

    //新增照服員
    public void addCaregiver(CaregiverBean bean) {
        try {
            dao.insertCaregiver(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //修改照服員
    public void modifyCaregiver(CaregiverBean bean) {
        try {
            dao.updateCaregiver(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查詢單筆
    public CaregiverBean getCaregiver(int id) {
        try {
            return dao.getCaregiverById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //查詢全部
    public List<CaregiverBean> getAllCaregivers() {
        try {
            return dao.getAllCaregivers();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
