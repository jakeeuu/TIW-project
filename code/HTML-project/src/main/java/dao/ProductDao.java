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
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, user.getMail());
			pstatement.setString(2, user.getMail());
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return null;           ///////////////////////TOLGOOOOOOOOOOOOOOO
				else {
					while(result.next()) {
						Product product = new Product();
						product.setCode(result.getString("ProdCode"));
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
	
	public ArrayList<Product> produtcsFromSearch(String keyWord) throws SQLException{
		ArrayList<Product> products = new ArrayList<Product>();
		/////////////////////////TODO : SVILUPPO LA SEGUENTE QUERY
		String query = 	  "A seguito dell’invio compare una pagina RISULTATI con prodotti che contengono "
				+ "la chiave di ricerca nel nome o nella descrizione. L’elenco mostra solo il codice, "
				+ "il nome del prodotto e il prezzo minimo di vendita del prodotto da parte dei fornitori "
				+ "che lo vendono (lo stesso prodotto può essere  venduto da diversi fornitori a prezzi diversi e "
				+ "l’elenco mostra il minimo valore di tali prezzi). L’elenco è ordinato in modo "
				+ "crescente in base al prezzo minimo di vendita del prodotto da parte dei fornitori che lo offrono";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, keyWord);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return null;           ///////////////////////SE LA QUERY NON PESCA NULLA DAL DB COSA FACCIO ???
				else {
					while(result.next()) {
						Product product = new Product();
						product.setCode(result.getString("code"));
						product.setName(result.getString("name"));
						product.setMinimumPrice(Integer.parseInt(result.getString("minimumPrice")));
					}
					return products;
				}
			}
		}
	}
}
