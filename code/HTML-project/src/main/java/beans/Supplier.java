package beans;

import java.util.ArrayList;

public class Supplier {

	private String code;
	private String name;
	private float unitaryPrice;
	private double totalProductsPrice;
	private float freeShipping;
	//private double shippingPrice; deve diventare un array list
	private int score;
	
	public String getCode(){
		return this.code;
	}
	
	public String getName(){
		return this.name;
	}
    
	public float getUnitaryPrice(){
		return this.unitaryPrice;
	}
	
	public void setUnitaryPrice(float unitaryPrice){
		this.unitaryPrice = unitaryPrice;
	}
	
	public double getTotalProductsPrice(){
		return this.totalProductsPrice;
	}
	
	
	public void setFreeShipping(float freeshipping) {
		this.freeShipping = freeshipping;
	}
	
	public float getFreeShipping() {
		return this.freeShipping;
	}
	
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public void setTotalProductsPrice(double totalProductsPrice) {
		this.totalProductsPrice = totalProductsPrice;
	}
	

	public void setScore(int score) {
		this.score=score;
	}
	
	public int getScore(int score) {
		return this.score;
	}
}
