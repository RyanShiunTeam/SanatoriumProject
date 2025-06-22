package activity.bean;

public class Activity {
    private String Name;
    private String category;
    private int Limit;
    private int participant;
    private String Date;
    private String Time;
    private String location;
    private String instructor;
    private String status;
    private String registrationStart;
    private String registrationEnd;
    private String description;
    
    public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getLimit() {
		return Limit;
	}
	public void setLimit(int limit) {
		Limit = limit;
	}
	public int getParticipant() {
		return participant;
	}
	public void setParticipant(int participant) {
		this.participant = participant;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getInstructor() {
		return instructor;
	}
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRegistrationStart() {
		return registrationStart;
	}
	public void setRegistrationStart(String registrationStart) {
		this.registrationStart = registrationStart;
	}
	public String getRegistrationEnd() {
		return registrationEnd;
	}
	public void setRegistrationEnd(String registrationEnd) {
		this.registrationEnd = registrationEnd;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
