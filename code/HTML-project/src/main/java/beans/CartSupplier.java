package beans;

import java.util.ArrayList;

public class CartSupplier {

	private String code;
	private String name;
	private ArrayList<Product> products;
	private double totalProductsPrice;
	private double shippingPrice;
	
	public String getCode(){
		return this.code;
	}
	
	public String getName(){
		return this.name;
	}
	
	public ArrayList<Product> getProducts(){
		return this.products;
	}
	
	public double getTotalProductsPrice(){
		return this.totalProductsPrice;
	}
	
	public double getShippingPrice(){
		return this.shippingPrice;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}
	
	public void setTotalProductsPrice(double totalProductsPrice) {
		this.totalProductsPrice = totalProductsPrice;
	}
	
	public void setShippingPrice(double shippingPrice) {
		this.shippingPrice = shippingPrice;
	}
}
