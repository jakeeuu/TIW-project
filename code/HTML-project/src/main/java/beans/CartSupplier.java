package beans;

import java.util.ArrayList;

public class CartSupplier {

	private int code;
	private String name;
	private ArrayList<Product> products;
	private float totalPrice;
	private float shippingPrice;
	
	public CartSupplier() {
		totalPrice = 0;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ArrayList<Product> getProducts(){
		return this.products;
	}
	
	public float getTotalPrice() {
		return this.totalPrice;
	}
	
	public float getShippingPrice() {
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
	
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public void setShippingPrice(float shippingPrice) {
		this.shippingPrice = shippingPrice;
	}
}
