package member.bean;

import java.sql.Timestamp;

public class PwdReset {
    private int id;
    private int userID;
    private String code;
    private Timestamp expiresAt;
    private boolean used;
    
    
    
	public PwdReset() {
	}

	public PwdReset(int id, int userID, String code, Timestamp expiresAt, boolean used) {
		this.id = id;
		this.userID = userID;
		this.code = code;
		this.expiresAt = expiresAt;
		this.used = used;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Timestamp getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Timestamp expiresAt) {
		this.expiresAt = expiresAt;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}
    
    
	
    
}
