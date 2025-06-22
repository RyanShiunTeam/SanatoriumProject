package member.servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.bean.BackendUser;
import member.service.BackendUserService;


@WebServlet("/GetEmpByID")
public class GetEmpByID extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BackendUserService userService = new BackendUserService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		       
    	// 取得目標員工資料並 forward
        int targetId = Integer.parseInt(request.getParameter("userID").trim());
        
        // 用員工 ID 撈出該員工物件
        BackendUser emp = userService.getBackendUserById(targetId);
        
        // 下拉式選單 ( 職等選擇 )
        List<String> roles = userService.getAllRoles();

        request.setAttribute("employee", emp);
        request.setAttribute("roles", roles);
        request.getRequestDispatcher("/MemberPage/updatePage.jsp")
           .forward(request, response);

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
