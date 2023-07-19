package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.Supplier;

public class SupplierDao {

	private Connection connection;

	public SupplierDao(Connection connection) {
		this.connection = connection;
	}
	
	public ArrayList<Supplier> findAllSuppliers(int code)throws SQLException{
		ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
		String query = "select SupCode,SupName, Score, Price, FreeShipping \r\n"
						+"from alldata \r\n"
						+"where ProdCode = ? \r\n";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, String.valueOf(code));
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return null;           ///////////////////////SE LA QUERY NON PESCA NULLA DAL DB COSA FACCIO ???
				else {
					while(result.next()) {
						Supplier supplier = new Supplier();
						supplier.setCode(Integer.parseInt(result.getString("SupCode")));
						supplier.setName(result.getString("SupName"));
						supplier.setScore(Integer.parseInt(result.getString("Score")));
						supplier.setUnitaryPrice(Float.parseFloat(result.getString("Price")));
						supplier.setFreeShipping(Float.parseFloat(result.getString("FreeShipping")));
						suppliers.add(supplier);
						
					}
					return suppliers;
				}
			}
		}
	}
	
	public void insertInto(String mailUser, int prodCode,Date date) throws SQLException {
		String query = "insert into visualize (MailUser, ProdCode, Date) values(?,?,?)";
		try(PreparedStatement pstatement = connection.prepareStatement(query);){
			pstatement.setString(1, mailUser);
			pstatement.setString(2, String.valueOf(prodCode));
			pstatement.setDate(1, new java.sql.Date(date.getTime()));
		}
	}

}
