package caregiver.bean;

import java.io.Serializable;

public class Caregiver implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int caregiverId;
	private String chineseName; // 中文姓名
	private boolean gender; // 性別
	private String phone; // 聯絡電話
	private String email; // 電子信箱
	private int experienceYears; // 工作年資
	private String photo; // 照片

	// 無參建構子
	public Caregiver() {
	}

	// 全參建構子
	public Caregiver(int caregiverId, String chineseName, boolean gender, String phone, String email,
			int experienceYears, String photo) {
		this.caregiverId = caregiverId;
		this.chineseName = chineseName;
		this.gender = gender;
		this.phone = phone;
		this.email = email;
		this.experienceYears = experienceYears;
		this.photo = photo;
	}

	// Getter / Setter
	public int getCaregiverId() {
		return caregiverId;
	}

	public void setCaregiverId(int caregiverId) {
		this.caregiverId = caregiverId;
	}

	public String getChineseName() {
		return this.chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public boolean getGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getExperienceYears() {
		return experienceYears;
	}

	public void setExperienceYears(int experienceYears) {
		this.experienceYears = experienceYears;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "Caregiver{" + "caregiverId=" + caregiverId + ", chineseName='" + chineseName + '\'' + ", gender='"
				+ gender + '\'' + ", phone='" + phone + '\'' + ", email='" + email + '\'' + ", experienceYears='"
				+ experienceYears + '\'' + ", photo='" + photo + '\'' + '}';

	}

}