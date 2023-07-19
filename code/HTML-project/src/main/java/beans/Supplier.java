package beans;

import java.util.ArrayList;

public class Supplier {

	private int code;
	private String name;
	private float unitaryPrice;
	private double totalProductsPrice;
	private float freeShipping;
	private ArrayList<SpendingRanges> spendingranges;
	private int score;
	
	public int getCode(){
		return this.code;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setSpendingRanges(ArrayList<SpendingRanges> spendingranges){
		this.spendingranges = spendingranges;
	}
	
	public ArrayList<SpendingRanges> getSpendingRnages(){
		return this.spendingranges;
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
	
	
	public void setCode(int code) {
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
