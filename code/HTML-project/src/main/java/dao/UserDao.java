package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.User;

public class UserDao {
	private Connection connection;

	public UserDao(Connection connection) {
		this.connection = connection;
	}
	
	public User checkCredentials(String mail, String password) throws SQLException{
		String query = "SELECT mail, name, surname, address FROM User  WHERE username = ? AND password = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, mail);
			pstatement.setString(2, password);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return null;
				else {
					result.next();
					User user = new User();
					user.setMail(result.getString("mail"));
					user.setName(result.getString("name"));
					user.setSurname(result.getString("surname"));
					user.setAddress(result.getString("address"));
					return user;
				}
			}
		}
	}
}
