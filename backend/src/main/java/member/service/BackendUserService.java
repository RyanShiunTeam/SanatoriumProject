package member.service;

import java.sql.SQLException;
import java.util.List;

import member.bean.BackendUser;
import member.dao.BackendUserDAO;

public class BackendUserService {
	private BackendUserDAO backendUserDAO = new BackendUserDAO();
	
	// 查詢員工資料 by userID
	public BackendUser getBackendUserById(int userID) {
		try {
			return backendUserDAO.getBackendUserById(userID);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 查詢 Email by userID
	public String getEmailById(int userID) {
		try {
			BackendUser userEmail = backendUserDAO.getEmailById(userID);
			return userEmail.getEmail();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	// 員工登入驗證
	public BackendUser login (int userID, String password) {
		try {
			// 取得該員工 ID 所有資料
			BackendUser user = backendUserDAO.getBackendUserById(userID);
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
	public boolean addBackendUser(String userName, String password, String email)  {
		try {
			return backendUserDAO.addBackendUser(userName, password, email);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// 修改密碼 by ID
	public boolean updatePwd(int userID, String newPwd) {
		try {
			return backendUserDAO.updatePwd(userID, newPwd);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// 停權後台員工，管理權限為 Admin 才可執行
	public boolean disableUser(int userID) { 
		try {
			backendUserDAO.disableUser(userID);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// 啟用後台員工，管理權限為 Admin 才可執行
	public boolean enableUser(int userID) {
		try {
			backendUserDAO.enableUser(userID);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// 更新員工資料，管理權限為 Admin 才可執行
	public boolean updateBackendUser(int userID, String userName, String email, String role) {
		try {
			backendUserDAO.updateBackendUser(userID, userName, email, role);
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
	
	// 查詢所有員工
	public List<BackendUser> getAllBackendUsers() {
		try {
			return backendUserDAO.getAllBackendUser();
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
	public boolean isAdmin(int userID) {
		try {
			BackendUser user = backendUserDAO.getBackendUserById(userID);
			return user != null && "Admin".equals(user.getRole());
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
