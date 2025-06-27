package member.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.bean.BackendUser;
import member.service.BackendUserService;
import member.service.PwdResetService;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import com.google.gson.Gson;


@WebServlet("/RequestPwdReset")
public class RequestPwdResetServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private BackendUserService userService = new BackendUserService();
    private final Gson gson = new Gson();
    private final PwdResetService service = new PwdResetService();

    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object>  result = new HashMap<>();

        try {
            // 解析前端 JSON
        	BackendUser userInfo = gson.fromJson(request.getReader(), BackendUser.class);
            int userID = userInfo.getuserID();
            
            // 查詢使用者 email
            String email =  userService.getEmailById(userID);
            
            if (email == null) {
            	result.put("success", false);
            	result.put("message", "此 ID 不存在");
            } else {
                // 產生並儲存驗證碼
                String code = service.generateCode();
                service.createResetCode(userID, code);
                // 寄送驗證碼
                sendEmail(email, code);
            	result.put("success", true);
            	result.put("message", "驗證碼已寄出");
            }
        } catch (Exception e) {
            e.printStackTrace();
        	result.put("success", false);
        	result.put("message", "伺服器錯誤，無法寄出驗證信");

        }
        gson.toJson(result, response.getWriter());

    }

    private void sendEmail(String recipient, String code) throws MessagingException {
        final String smtpHost = "smtp.gmail.com";
        final String smtpPort = "587";
        final String from = "a0976828118@gmail.com";
        final String password = "dsowlrvxgmwnkqhm";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(from, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject("重設密碼驗證碼");
        message.setText("您的驗證碼為：" + code + "，請在 10 分鐘內使用。");

        Transport.send(message);
    }
}
