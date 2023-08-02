package beans;

import java.util.ArrayList;

import java.util.Date;

public class Order {

	private int code;
	private String supplierName;
	private ArrayList<Product> products;
	private float totalPrice;
	private Date date;
	private String address;
	
	public Order() {
		this.products = new ArrayList<Product>();
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getSupplierName() {
		return this.supplierName;
	}
	
	public ArrayList<Product> getProducts(){
		return this.products;
	}
	
	public float getTotalPrice() {
		return this.totalPrice;
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}
	
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
}
