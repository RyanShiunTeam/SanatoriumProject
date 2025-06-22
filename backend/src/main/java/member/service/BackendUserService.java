package member.service;

import java.sql.SQLException;
import java.util.List;

import member.bean.BackendUser;
import member.dao.BackendUserDAO;

public class BackendUserService {
	private BackendUserDAO backendUserDAO = new BackendUserDAO();
	
	// 查詢員工資料 by userId
	public BackendUser getBackendUserById(int userId) {
		try {
			return backendUserDAO.getBackendUserById(userId);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 員工登入驗證
	public BackendUser login (int userId, String password) {
		try {
			// 取得該員工 ID 所有資料
			BackendUser user = backendUserDAO.getBackendUserById(userId);
			// 比較密碼是否正確
			if (user != null && password != null && password.equals(user.getPassWord())) {
				return user;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 增加後臺使用者
	public boolean addBackendUser(BackendUser user)  {
		try {
			return backendUserDAO.addBackendUser(user);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// 停權後台員工，管理權限為 Admin 才可執行
	public boolean disableUser(int userId) { 
		try {
			backendUserDAO.disableUser(userId);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// 啟用後台員工，管理權限為 Admin 才可執行
	public boolean enableUser(int userId) {
		try {
			backendUserDAO.enableUser(userId);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// 更新員工資料，管理權限為 Admin 才可執行
	public boolean updateBackendUser(BackendUser user) {
		try {
			backendUserDAO.updateBackendUser(user);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// 依名稱模糊查詢後台員工
	public List<BackendUser> getBackendUserByName(String userName) {
		try {
			return backendUserDAO.getBackendUserByName(userName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 取得所有職等
	public List<String> getAllRoles() {
		try {
			return backendUserDAO.getAllRoles();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 取得停權名單
	public List<BackendUser> getBanList() {
		try {
			return backendUserDAO.getDisabledUsers();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 檢查權限是否為 Admin
	public boolean isAdmin(int userId) {
		try {
			BackendUser user = backendUserDAO.getBackendUserById(userId);
			return user != null && "Admin".equals(user.getRole());
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
