package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.Product;
import beans.User;

public class ProductDao {
	private Connection connection;

	public ProductDao(Connection connection) {
		this.connection = connection;
	}
	
	public ArrayList<Product> produtcsToVisualize(User user) throws SQLException{
		ArrayList<Product> products = new ArrayList<Product>();
		String query = 	"SELECT ProdCode, Name, Description,Category,Photo\r\n"
				 		+ "FROM ProductVisualized PR\r\n"
				 		+"WHERE Mail = ? and 4>= (select count(*) from Productvisualized where Date > PR.Date and Mail = ?)\r\n"
				        +"Order by Date DESC";
		String altquery = "SELECT Code, Name, Description,Category,Photo \r\n"
						+"FROM product P \r\n"
						+"WHERE ? > (select count(*) from product where Code < P.Code)\r\n";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, user.getMail());
			pstatement.setString(2, user.getMail());
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return null;           // TOLGOOOOO
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
					if(products.size() < 5) {
						int lenght = 5-products.size();
						try (PreparedStatement altpstatement = connection.prepareStatement(altquery);) {
							altpstatement.setString(1,String.valueOf(lenght));
							try (ResultSet altresult = altpstatement.executeQuery();) {
								if (!altresult.isBeforeFirst()) {
									System.out.println("male male");
									return null;  //TODO : TOLGOOOO
								}
								else {
									while(altresult.next()) {
										Product product = new Product();
										product.setCode(Integer.parseInt(altresult.getString("ProdCode")));
										product.setName(altresult.getString("Name"));
										product.setDescription(altresult.getString("Description"));
										product.setCategory(altresult.getString("Category"));
										product.setPhoto(altresult.getString("Photo"));
										products.add(product);
									}
								}
							}
						}
					}
					return products;
				}
			}
		}
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
}
