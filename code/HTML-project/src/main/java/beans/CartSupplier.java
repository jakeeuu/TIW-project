package beans;

import java.util.ArrayList;

public class CartSupplier {

	private int code;
	private String name;
	private ArrayList<String> prodName;
	private ArrayList<Integer> prodCode;
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
	
	public ArrayList<String> getNameProducts(){
		return this.prodName;
	}
	
	public float getTotalPrice() {
		return this.totalPrice;
	}
	
	public float getShippingPrice() {
		return this.shippingPrice;
	}
	
	public ArrayList<String> getProducts(){
		return this.prodName;
	}
	
	public ArrayList<Integer> getCodeProducts(){
		return this.prodCode;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setNameProducts(ArrayList<String> prodName) {
		this.prodName = prodName;
	}
	
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public void setShippingPrice(float shippingPrice) {
		this.shippingPrice = shippingPrice;
	}
	
	public void setCodeProducts(ArrayList<Integer> prodCode){
		this.prodCode = prodCode;
	}
}
