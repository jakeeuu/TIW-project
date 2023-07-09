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
		String query = 	  "SELECT TOP 5"
						+ "DISTINCT code\r\n"
						+ "name,description,category,photo\r\n"  ////SISTEMO QUESTA QUERY PERCHé SICURAMENTE SBAGLIATE
						+ "FROM Visualizza JOIN Product\r\n"
						+ "WHERE mail = ?\r\n"
						+ "ORDER BY data ASC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, user.getMail());
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return null;           ///////////////////////TOLGOOOOOOOOOOOOOOO
				else {
					while(result.next()) {
						Product product = new Product();
						product.setCode(result.getString("code"));
						product.setName(result.getString("name"));
						product.setDescription(result.getString("description"));
						product.setCategory(result.getString("category"));
						product.setPhoto(result.getString("photo"));
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
