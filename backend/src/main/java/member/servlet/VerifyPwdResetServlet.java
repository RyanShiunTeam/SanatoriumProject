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
import member.bean.PwdReset;
import member.dao.PwdResetDTO;
import member.service.BackendUserService;
import member.service.PwdResetService;




@WebServlet("/VerifyPwdReset")
public class VerifyPwdResetServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BackendUserService userService = new BackendUserService();
    private final Gson gson = new Gson();
    private final PwdResetService pwdResetService = new PwdResetService();


    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        
        Map<String, Object>  result = new HashMap<>();

        try {
        	// 特別做一個 DTO 用來接收前端資料
        	PwdResetDTO reserInfo = gson.fromJson(request.getReader(), PwdResetDTO.class);
            
            int userID = reserInfo.getUserID();
            String code = reserInfo.getCode();
            String newPwd = reserInfo.getNewPwd();


            
            PwdReset reset = pwdResetService.validateCode(String.valueOf(userID), code);
            if (reset == null) {
            	result.put("success", false);
            	result.put("message", "驗證碼錯誤或已過期");
            } else {
            	// 修改密碼
            	userService.updatePwd(userID, newPwd);
            	pwdResetService.markCodeAsUsed(reset.getId());
            	result.put("success", true);
            	result.put("message", "密碼已成功更新");
            }
        
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "伺服器錯誤: ");
        }

        gson.toJson(result, response.getWriter());
    }
}
