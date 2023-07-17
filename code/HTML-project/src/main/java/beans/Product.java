package beans;

public class Product {
	
	private String code;
	private String name;
	private String description;
	private String category;
	private String photo;
	private float minimumPrice;
	
	public String getCode() {
		return this.code;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	public String getPhoto() {
		return this.photo;
	}
	
	public float getMinimumPrice() {
		return this.minimumPrice;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public void setPhoto(String photo) { ////////////////////////////TODO : in questo setter metto il path per la directory delle immagini già impostato
		this.photo = photo;
	}
	
	public void setMinimumPrice(float minimumPrice) {
		this.minimumPrice = minimumPrice;
	}
}
