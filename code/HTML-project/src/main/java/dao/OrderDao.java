package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.CartSupplier;
import beans.Order;
import beans.User;

public class OrderDao {
	private Connection connection;

	public OrderDao(Connection connection) {
		this.connection = connection;
	}
	
	public void insertOrder(String mailUser, String supName,float total, Date date, String address) throws SQLException {
		String query = "insert into orders (Code, MailUser, Supplier, Total, Date, Address) values(?,?,?,?,?,?)";
		try(PreparedStatement pstatement = connection.prepareStatement(query);){
			pstatement.setString(1, null); //prova per l'autoincremento
			pstatement.setString(2, mailUser);
			pstatement.setString(3, supName);
			pstatement.setString(4, String.valueOf(total));
			pstatement.setDate(5, new java.sql.Date(date.getTime()));
			pstatement.setString(6,address);
		}
	}
	
	public ArrayList<Order> printOrders (String mailUser)throws SQLException {
		ArrayList<Order> orders = new ArrayList<Order>();
		String query = "select O.Code, Supplier,Total, Date, U.Address \r\n"
						+"from orders O join user U on MailUser = Mail \r\n"
						+"where Mail = ? \r\n"
						+"order by Date \r\n";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, mailUser);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return null;
				else {
					while(result.next()) {
						Order order = new Order();
						order.setCode(Integer.parseInt(result.getString("Code")));
						order.setSupplierName(result.getString("Supplier"));
						order.setTotalPrice(Float.parseFloat(result.getString("Total")));
						order.setDate(result.getDate("Date"));
						order.setAddress(result.getString("Address"));
						orders.add(order);
						
					}
					return orders;
				}
			}
		}
	}
}
