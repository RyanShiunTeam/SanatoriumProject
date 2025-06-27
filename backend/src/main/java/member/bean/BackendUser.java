package member.bean;

import java.io.Serializable;
import java.sql.Date;


public class BackendUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userID;
    private String userName;
    private String password;
    private String email;
    private String role;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;


    public BackendUser() {
    }

    public BackendUser(int userID, String userName, String password, String email,
                       String role, boolean isActive, Date createdAt, Date updatedAt) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getuserID() {
        return userID;
    }

    public void setuserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return password;
    }

    public void setPassWord(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "BackendUser{" +
               "userID=" + userID +
               ", userName='" + userName + '\'' +
               ", password='" + password + '\'' +
               ", email='" + email + '\'' +
               ", role='" + role + '\'' +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               '}';
    }
}
