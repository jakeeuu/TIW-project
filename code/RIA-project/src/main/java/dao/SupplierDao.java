package dao;

import java.sql.Connection;
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
		String query = "select Supcode,S.Name, Score, Price, FreeShipping \r\n"
						+"from (product P join sold_by on P.Code=ProdCode) join supplier S on S.Code=Supcode \r\n"
						+"where ProdCode = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, String.valueOf(code));
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return null;
				else {
					while(result.next()) {
						Supplier supplier = new Supplier();
						supplier.setCode(Integer.parseInt(result.getString("Supcode")));
						supplier.setName(result.getString("Name"));
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
	
	
	
	public Float supplierFreeShipping(int supCode) throws SQLException{ 
		String query= "select FreeShipping from supplier where Code = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, String.valueOf(supCode) );
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return null;           
				else {
					result.next();
					return Float.parseFloat(result.getString("FreeShipping"));
				}
			}
		}
	}	
	
	
	public boolean areValid(int supCode, String supName) throws SQLException{ 
		String query= "select count(*) from supplier where Code = ? and Name = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, String.valueOf(supCode) );
			pstatement.setString(2, supName );
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return false;           
				else {
					result.next();
					if (result.getInt("count(*)") == 1) {
						return true;
					}else {
						return false;
					}
				}
			}
		}
	}

}
