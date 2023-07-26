package beans;

import java.util.ArrayList;

public class Product {
	
	private int code;
	private String name;
	private String description;
	private String category;
	private String photo;
	private float minimumPrice;
	private ArrayList<Supplier> suppliers;
	private int quantity;
	private float price;
	
	public int getCode() {
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
	
	public ArrayList<Supplier> getSuppliers(){
		return this.suppliers;
	}
	
	public int getQuantity() {
		return this.quantity;
	}
	
	public float getPrice() {
		return this.price;
	}
	
	public void setCode(int code) {
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
	
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public void setMinimumPrice(float minimumPrice) {
		this.minimumPrice = minimumPrice;
	}
	
	public void setSuppliers(ArrayList<Supplier> arrayList){
		this.suppliers = arrayList;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
}
