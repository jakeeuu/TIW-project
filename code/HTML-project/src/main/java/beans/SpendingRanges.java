package beans;

public class SpendingRanges {
	private int SupCode;
	private float Price;
	private int MaximumN;
	private int MinimumN;
	
	public void setSupCode(int code) {
		this.SupCode = code;
	}
	
	public int getSupCode() {
		return this.SupCode;
	}
	
	public void setPrice(float price) {
		this.Price = price;
	}
	
	public float getPrice() {
		return this.Price;
	}
	
	public void setMaximumN(int num) {
		this.MaximumN = num;
	}
	
	public int getMaximumN() {
		return this.MaximumN;
	}
	
	public void setMinimumN(int num) {
		this.MinimumN = num;
	}
	
	public int getMinimummN() {
		return this.MinimumN;
	}
}
