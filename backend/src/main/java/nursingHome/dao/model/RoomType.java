package nursingHome.dao.model;


public class RoomType {
    private int id;//房子id
    private String name;//房名
    private int price;//價格
    private double area;//坪數
    private String description;//基本描述
    private String imageUrl;//房型圖片
    private  String specialFeatures;

    //無參數建構子 
	public RoomType() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getSpecialFeatures() {
		return specialFeatures;
	}

	public void setSpecialFeatures(String specialFeatures) {
		this.specialFeatures = specialFeatures;
	}

	@Override
	public String toString() {
		return "RoomType [id=" + id + ", name=" + name + ", price=" + price + ", area=" + area + ", description="
				+ description + ", imageUrl=" + imageUrl + ", specialFeatures=" + specialFeatures + "]";
	}
	
	
	
	
    
    
	
}