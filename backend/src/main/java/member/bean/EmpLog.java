package member.bean;

import java.sql.Timestamp;

public class EmpLog {
    private Long id;
    private int userId;
    private String action;
    private Integer targetId;
    private Timestamp createdAt;
    
    
	public EmpLog() {
		
	}

	public EmpLog(int userId, String action, Integer targetId) {
		this.userId = userId;
		this.action = action;
		this.targetId = targetId;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Integer getTargetId() {
		return targetId;
	}
	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}  
}