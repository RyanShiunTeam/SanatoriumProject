package member.service;



import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;

import member.bean.PwdReset;
import member.dao.PwdResetDAO;

public class PwdResetService {

    private final PwdResetDAO dao = new PwdResetDAO();

    public String generateCode() {
        return String.format("%06d", new SecureRandom().nextInt(1000000));
    }

    public void createResetCode(int userID, String code) throws Exception {
        Timestamp expireTime = Timestamp.from(Instant.now().plusSeconds(600));

        PwdReset reset = new PwdReset();
        reset.setUserID(userID);
        reset.setCode(code);
        reset.setExpiresAt(expireTime);
        reset.setUsed(false);

        dao.insert(reset);
    }

    public PwdReset validateCode(String userID, String code) throws Exception {
        return dao.getValidCode(userID, code);
    }

    public void markCodeAsUsed(int id) throws Exception {
        dao.markUsed(id);
    }
}
