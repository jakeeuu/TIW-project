package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Date;

import beans.Product;
import beans.User;

public class ProductDao {
	private Connection connection;

	public ProductDao(Connection connection) {
		this.connection = connection;
	}
	
	public ArrayList<Product> memoryProducts(User user) throws SQLException{
		ArrayList<Product> products = new ArrayList<Product>();
		String query = 	"SELECT ProdCode, Name, Description,Category,Photo\r\n"
				 		+ "FROM ProductVisualized PR\r\n"
				 		+"WHERE Mail = ? and 4>= (select count(*) from Productvisualized where Date > PR.Date and Mail = ?)\r\n"
				        +"Order by Date DESC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, user.getMail());
			pstatement.setString(2, user.getMail());
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) {
					return null;	        
				}
				else {
					while(result.next()) {
						Product product = new Product();
						product.setCode(Integer.parseInt(result.getString("ProdCode")));
						product.setName(result.getString("Name"));
						product.setDescription(result.getString("Description"));
						product.setCategory(result.getString("Category"));
						product.setPhoto(result.getString("Photo"));
						products.add(product);
					}
					return products;
				}
			}
		}
	}
	
	public ArrayList<Product> defaultProducts(int num) throws SQLException{
		ArrayList<Product> products = new ArrayList<Product>();
		String query = "SELECT Code, Name, Description,Category,Photo \r\n"
						+"FROM product P \r\n"
						+"WHERE ? > (select count(*) from product where Code < P.Code)";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, String.valueOf(num));
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) {
					return null;	        
				}
				else {
					while(result.next()) {
						Product product = new Product();
						product.setCode(Integer.parseInt(result.getString("Code")));
						product.setName(result.getString("Name"));
						product.setDescription(result.getString("Description"));
						product.setCategory(result.getString("Category"));
						product.setPhoto(result.getString("Photo"));
						products.add(product);
					}
					return products;
				}
			}
		}
	}
	
	public ArrayList<Product> produtcsToVisualize(User user) throws SQLException{
		ArrayList<Product> products1 = new ArrayList<Product>();
		ArrayList<Product> products2 = new ArrayList<Product>();
		
		int lenght;
		products1 = memoryProducts(user);
		if(products1 == null) {
			lenght = 5;
			products2 = defaultProducts(lenght);
		}
		else if(products1.size() < 5) {
			lenght = 5-products1.size();
			products2 = defaultProducts(lenght);
			products1.addAll(products2);
		}
		
		if(products1 != null)
			return products1;
		else return products2;
	}
		
	
	public ArrayList<Product> produtcsFromSearch(String keyWord) throws SQLException{
		ArrayList<Product> products = new ArrayList<Product>();
		String query = "SELECT P.Code, P.Name, Price, P.Description, P.Category, P.Photo \r\n"
						+"FROM product P join sold_by S on Code=ProdCode \r\n"
						+"WHERE Name LIKE ? or Description like ? and Price = (select min(Price) from product join sold_by on Code=ProdCode where Code=P.Code ) \r\n"
				        +"Order by price";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, "%" + keyWord + "%");
			pstatement.setString(2, "%" + keyWord + "%");
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return null;           ///////////////////////SE LA QUERY NON PESCA NULLA DAL DB COSA FACCIO ???
				else {
					while(result.next()) {
						Product product = new Product();
						product.setCode(Integer.parseInt(result.getString("Code")));
						product.setName(result.getString("Name"));
						product.setMinimumPrice(Float.parseFloat(result.getString("Price")));
						product.setDescription(result.getString("Description"));
						product.setCategory(result.getString("Category"));
						product.setPhoto(result.getString("Photo"));
						products.add(product);
						
					}
					return products;
				}
			}
		}
	}
	
	public ArrayList<Product> productInOrders(int orderCode)throws SQLException{
		ArrayList<Product> products = new ArrayList<Product>();
		String query = "select P.Code, P.Name, P.Description, P.Category, P.Photo \r\n"
						+"from (product P join composed C on P.Code=ProductCode) join orders O on O.Code=OrderCode \r\n"
						+"where OrderCode = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, String.valueOf(orderCode) );
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return null;           
				else {
					while(result.next()) {
						Product product = new Product();
						product.setCode(Integer.parseInt(result.getString("Code")));
						product.setName(result.getString("Name"));
						product.setDescription(result.getString("Description"));
						product.setCategory(result.getString("Category"));
						product.setPhoto(result.getString("Photo"));
						products.add(product);
						
					}
					return products;
				}
			}
		}
	}
	
	public void insertInto(String mailUser, int prodCode,Date date) throws SQLException {
		String query1= "select count(*)\r\n"
						+ "from visualize\r\n"
						+ "where MailUser=? and ProdCode=?";
		String query2 = "update visualize \r\n"
						+ "set Date= ? \r\n"
						+ "where ProdCode = ? and MailUser=?";
		String query3 = "insert into visualize (MailUser, ProdCode, Date) values(?,?,?)";
		try(PreparedStatement pstatement = connection.prepareStatement(query1);){
			pstatement.setString(1, mailUser);
			pstatement.setString(2, String.valueOf(prodCode));
			try (ResultSet result = pstatement.executeQuery();) {
				if(result.getInt("count")>=1) {
					try(PreparedStatement pstatement2 = connection.prepareStatement(query2);){
						pstatement.setDate(1, date);
						pstatement.setString(2, String.valueOf(prodCode));
						pstatement.setString(3, mailUser);
						pstatement.executeUpdate();
					}
				}else {
					try(PreparedStatement pstatemen3 = connection.prepareStatement(query3);){
						pstatement.setString(1, mailUser);
						pstatement.setString(2, String.valueOf(prodCode));
						pstatement.setDate(3, date);
						pstatement.executeUpdate();
					}
				}
			}
		}
	}
	
	public boolean isValidCode(int productCode) throws SQLException{ 
		String query= "select count(*) from product where Code = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, String.valueOf(productCode) );
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return false;           
				else {
					if (result.getInt("count") == 1) {
						return true;
					}else {
						return false;
					}
				}
			}
		}
	}	
}
