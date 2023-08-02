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
		this.products = new ArrayList<Product>();
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getName() {
		return this.name;
	}
	
	public float getTotalPrice() {
		return this.totalPrice;
	}
	
	public float getShippingPrice() {
		return this.shippingPrice;
	}
	
	public ArrayList<Product> getProducts(){
		return this.products;
	}
	
	public Product getProduct(int code) {
		for(Product p : this.products) {
			if(p.getCode() == code)
				return p;
		}
		return null;
	}
	
	public int getTotalNumber() {
		int total = 0;
		for(Product p : products) {
			total = total + p.getQuantity();
		}
		return total;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public void setShippingPrice(float shippingPrice) {
		this.shippingPrice = shippingPrice;
	}
	
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}
	
	public void setProduct(Product product) {
		this.products.add(product);
	}

}
