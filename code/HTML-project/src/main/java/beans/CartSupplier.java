package beans;

import java.util.ArrayList;
import java.util.HashMap;

public class CartSupplier {

	private int code;
	private String name;
	private ArrayList<String> prodName;
	private ArrayList<Integer> prodCode;
	private HashMap<Integer, Integer> prodCounter;
	private float totalPrice;
	private float shippingPrice;
	
	public CartSupplier() {
		totalPrice = 0;
		this.prodCounter = new HashMap<Integer, Integer>();
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
	
	public ArrayList<Integer> getCodeProducts(){
		return this.prodCode;
	}
	
	public Integer getProdCounter(int prod) {
		return this.prodCounter.get(prod);
	}
	
	public HashMap<Integer,Integer> getProdCounter(){
		return this.prodCounter;
	}
	
	public void setProdCounter(int prod, int quantity) {
		this.prodCounter.put(prod,quantity);
	}
	
	public void setProdCounter(HashMap<Integer,Integer> prodCounter){
		this.prodCounter = prodCounter;
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
