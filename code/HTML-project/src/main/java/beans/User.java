package beans;

public class User {
	
	private String mail;
	private String name;
	private String surname;
	private String address;
	
	public String getMail() {
		return this.mail;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getSurname() {
		return this.surname;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
}
