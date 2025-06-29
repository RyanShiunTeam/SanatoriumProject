package bus.dao;

public class BusSeatDTO {
	private int minSeats;
	private int maxSeats;
	private int minWheels;
	private int maxWheels;
	
	public BusSeatDTO() {
		// Gson 需要無參數建構子，否則反序列化會失敗
	}

	public int getMinSeats() {
		return minSeats;
	}

	public void setMinSeats(int minSeats) {
		this.minSeats = minSeats;
	}

	public int getMaxSeats() {
		return maxSeats;
	}

	public void setMaxSeats(int maxSeats) {
		this.maxSeats = maxSeats;
	}

	public int getMinWheels() {
		return minWheels;
	}

	public void setMinWheels(int minWheels) {
		this.minWheels = minWheels;
	}

	public int getMaxWheels() {
		return maxWheels;
	}

	public void setMaxWheels(int maxWheels) {
		this.maxWheels = maxWheels;
	}
	
	
}
