package member.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.bean.BackendUser;
import member.service.BackendUserService;


@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private BackendUserService userService = new BackendUserService();
    private Gson gson = new Gson();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		
		// 讀取 JSON (JS讀取的資料名稱一定要跟 Java Bean 名稱大小寫一樣，LocalDate 沒辦法直接轉換)
		BackendUser userInfo = gson.fromJson(request.getReader(), BackendUser.class);
		
		// 讀取帳號密碼
		int userID = userInfo.getuserID();
		String password = userInfo.getPassWord();		
		

		BackendUser user = userService.login(userID, password);
		
		Map<String, Object>  result = new HashMap<>();
		if (user != null) {
			// 檢查是否被停權
			if (user.isActive()) {
				// 後端也存使用者資訊
				HttpSession session = request.getSession();
				session.setAttribute("loginUserId", user.getuserID());
				session.setAttribute("loginUserName", user.getUserName());
				System.out.println("目前登入者 ID: " + user.getuserID());
				// 將結果放入準備回傳
				result.put("success", true);
				result.put("role", user.getRole());
				// 將目前登入者資訊放在前端
				result.put("user", user);
			} else {
				// 停權被拒
				result.put("success", false);
				result.put("message", "已被停權 ! 請聯絡管理者");
			}
		} else {
			// 帳密錯誤
			result.put("success", false);
			result.put("message", "帳號或密碼錯誤");
		}
		

        gson.toJson(result, response.getWriter());

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
