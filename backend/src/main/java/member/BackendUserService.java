package member;

import java.sql.SQLException;

public class BackendUserService {
	private BackendUserDAO backendUserDAO = new BackendUserDAO();
	
	// 增加後臺使用者
	public void addBackendUser(BackendUser user)  {
		try {
			backendUserDAO.addBackendUser(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 依照名稱停權後台使用者
	public void deleteBackendUser(String userName)  {
		try {
			backendUserDAO.deleteBackendUser(userName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 依照名稱更新後台使用者密碼
	public void updateBackendUser(String userName, String newPassword) {
		try {
			backendUserDAO.updateBackendUser(userName, newPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 依照名稱查詢後台使用者
	public BackendUser getBackendUserByName(String userName) {
		try {
			return backendUserDAO.getBackendUserByName(userName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
