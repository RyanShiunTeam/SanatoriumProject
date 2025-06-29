package utils;

import java.sql.SQLException;
import java.util.List;

import member.bean.EmpLog;
import member.dao.EmpLogDAO;

public class EmpService {
	private EmpLogDAO logDao = new EmpLogDAO();
	
    public void record(int userId, String action, Integer targetId) {
        EmpLog log = new EmpLog(userId, action, targetId);
        try {
            logDao.insert(log);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<EmpLog> getAllLog() {
    	try {
			return logDao.getAllEmpLogs();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
