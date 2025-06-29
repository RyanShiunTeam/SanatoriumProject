package bus.bean;

public class RehaBus {
	
	private int busId;                    //車輛流水號
	private String carDealership;         //車行
	private String busBrand;              //汽車廠牌
	private String busModel;              //型號
	private int seatCapacity;             //一般座位
	private int wheelchairCapacity;       //輪椅座位
	private String licensePlate;          //車牌號碼
	
	public RehaBus() {
		super();
	}

	public RehaBus(int busId, String carDealership, String busBrand, String busModel, int seatCapacity,
			int wheelchairCapacity, String licensePlate) {
		super();
		this.busId = busId;
		this.carDealership = carDealership;
		this.busBrand = busBrand;
		this.busModel = busModel;
		this.seatCapacity = seatCapacity;
		this.wheelchairCapacity = wheelchairCapacity;
		this.licensePlate = licensePlate;
	}

	public RehaBus(String carDealership, String busBrand, String busModel, int seatCapacity, int wheelchairCapacity,
			String licensePlate) {
		super();
		this.carDealership = carDealership;
		this.busBrand = busBrand;
		this.busModel = busModel;
		this.seatCapacity = seatCapacity;
		this.wheelchairCapacity = wheelchairCapacity;
		this.licensePlate = licensePlate;
	}

	public int getBusId() {
		return busId;
	}

	public void setBusId(int busId) {
		this.busId = busId;
	}

	public String getCarDealership() {
		return carDealership;
	}

	public void setCarDealership(String carDealership) {
		this.carDealership = carDealership;
	}

	public String getBusBrand() {
		return busBrand;
	}

	public void setBusBrand(String busBrand) {
		this.busBrand = busBrand;
	}

	public String getBusModel() {
		return busModel;
	}

	public void setBusModel(String busModel) {
		this.busModel = busModel;
	}

	public int getSeatCapacity() {
		return seatCapacity;
	}

	public void setSeatCapacity(int seatCapacity) {
		this.seatCapacity = seatCapacity;
	}

	public int getWheelchairCapacity() {
		return wheelchairCapacity;
	}

	public void setWheelchairCapacity(int wheelchairCapacity) {
		this.wheelchairCapacity = wheelchairCapacity;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	
}
