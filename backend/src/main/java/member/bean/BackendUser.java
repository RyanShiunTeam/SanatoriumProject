package member.bean;

import java.io.Serializable;
import java.time.LocalDate;

public class BackendUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userId;
    private String userName;
    private String passWord;
    private String email;
    private String role;
    private boolean isActive;
    private LocalDate createdAt;
    private LocalDate updatedAt;


    public BackendUser() {
    }

    public BackendUser(int userId, String userName, String passWord, String email,
                       String role, boolean isActive, LocalDate createdAt, LocalDate updatedAt) {
        this.userId = userId;
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "BackendUser{" +
               "userId=" + userId +
               ", userName='" + userName + '\'' +
               ", passWord='" + passWord + '\'' +
               ", email='" + email + '\'' +
               ", role='" + role + '\'' +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               '}';
    }
}
