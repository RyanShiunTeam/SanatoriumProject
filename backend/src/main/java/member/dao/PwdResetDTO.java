package member.dao;

public class PwdResetDTO {

    private int userID;
    private String code;
    private String newPwd;

    // Gson 需要無參數建構子**，不然反序列化會炸
    public PwdResetDTO() {}

    public int getUserID() { return userID; }
    public String getCode() { return code; }
    public String getNewPwd() { return newPwd; }

}
