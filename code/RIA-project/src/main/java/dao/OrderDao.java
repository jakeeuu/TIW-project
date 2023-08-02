package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import beans.Order;

public class OrderDao {
	private Connection connection;

	public OrderDao(Connection connection) {
		this.connection = connection;
	}
	
	public void insertOrder(String mailUser, String supName,float total, Date date, String address,int supCode) throws SQLException {
		String query = "insert into orders (Code, MailUser, Supplier, Total, Date, Address, SupCode) values(?,?,?,?,?,?,?)";
		try(PreparedStatement pstatement = connection.prepareStatement(query);){
			pstatement.setString(1, null);
			pstatement.setString(2, mailUser);
			pstatement.setString(3, supName);
			pstatement.setString(4, String.valueOf(total));
			pstatement.setDate(5, new java.sql.Date(date.getTime()));
			pstatement.setString(6,address);
			pstatement.setString(7,String.valueOf(supCode));
			pstatement.executeUpdate();
		}
	}
	
	public int findOrderCode(String mailUser) throws SQLException {
		int num;
		String query= "SELECT Code FROM orders  O WHERE MailUser = ? and Code>= ALL(Select Code from orders where MailUser=O.MailUser)";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, mailUser);
			try (ResultSet result = pstatement.executeQuery();) {
				result.next();
				num = Integer.parseInt(result.getString("Code"));
				return num;
			}
		}
	}
	
	public void insertInComposed(int orderCode, int productCode, int quantity) throws SQLException {
		String query = "insert into composed (OrderCode, ProductCode, Quantity) values(?,?,?)";
		try(PreparedStatement pstatement = connection.prepareStatement(query);){
			pstatement.setString(1, String.valueOf(orderCode));
			pstatement.setString(2, String.valueOf(productCode));
			pstatement.setString(3, String.valueOf(quantity));
			pstatement.executeUpdate();
		}
	}
	
	public void generalOrderUpdate(String mailUser, String supName,float total,Date date, String address,int supCode,HashMap<Integer, Integer> pq) throws SQLException{
		try {
			connection.setAutoCommit(false);
			insertOrder(mailUser , supName , total , date, address, supCode);
			int num = findOrderCode(mailUser);
			for (HashMap.Entry<Integer, Integer> entry : pq.entrySet()) {
	            int key = entry.getKey();
	            int value = entry.getValue();
	            insertInComposed(num,key, value);
	        }
			connection.commit();
		}catch(SQLException e){
			connection.rollback();
			throw e;
		}finally {
			connection.setAutoCommit(true);
		}
		
	}
	
	
	
	public ArrayList<Order> printOrders (String mailUser)throws SQLException {
		ArrayList<Order> orders = new ArrayList<Order>();
		String query = "select O.Code, Supplier,Total, Date, U.Address \r\n"
						+"from orders O join user U on MailUser = Mail \r\n"
						+"where Mail = ? \r\n"
						+"order by Date";
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
