package beans;

import java.util.ArrayList;

public class CartSupplier {

	private int code;
	private String name;
	private ArrayList<Product> products;
	private int totalPrice;
	private int shippingPrice;
	
	public int getCode() {
		return this.code;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ArrayList<Product> getProducts(){
		return this.products;
	}
	
	public int getTotalPrice() {
		return this.totalPrice;
	}
	
	public int getShippingPrice() {
		return this.shippingPrice;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}
	
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public void setShippingPrice(int shippingPrice) {
		this.shippingPrice = shippingPrice;
	}
}
