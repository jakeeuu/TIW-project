package beans;

public class SpendingRanges {
	private int supCode;
	private float price;
	private int maximumN;
	private int minimumN;
	
	public void setSupCode(int code) {
		this.supCode = code;
	}
	
	public int getSupCode() {
		return this.supCode;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
	
	public float getPrice() {
		return this.price;
	}
	
	public void setMaximumN(int num) {
		this.maximumN = num;
	}
	
	public int getMaximumN() {
		return this.maximumN;
	}
	
	public void setMinimumN(int num) {
		this.minimumN = num;
	}
	
	public int getMinimumN() {
		return this.minimumN;
	}
}
