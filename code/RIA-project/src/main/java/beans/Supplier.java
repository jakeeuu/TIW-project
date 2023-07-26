package beans;

import java.util.ArrayList;

public class Supplier {

	private int code;
	private String name;
	private float unitaryPrice;
	private float totalProductsPrice;
	private float freeShipping;
	private ArrayList<SpendingRanges> spendingRanges;
	private int score;
	private int totalNumber;
	
	public int getCode(){
		return this.code;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setSpendingRanges(ArrayList<SpendingRanges> spendingranges){
		this.spendingRanges = spendingranges;
	}
	
	public ArrayList<SpendingRanges> getSpendingRanges(){
		return this.spendingRanges;
	}
    
	public float getUnitaryPrice(){
		return this.unitaryPrice;
	}
	
	public void setUnitaryPrice(float unitaryPrice){
		this.unitaryPrice = unitaryPrice;
	}
	
	public float getTotalProductsPrice(){
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
	
	
	public void setTotalProductsPrice(float totalProductsPrice) {
		this.totalProductsPrice = totalProductsPrice;
	}
	

	public void setScore(int score) {
		this.score=score;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public int getTotalNumber() {
		return this.totalNumber;
	}
	
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}
}