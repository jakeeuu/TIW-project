package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.CartSupplier;
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
	
	public CartSupplier infoCartSupplier(int prodCode, int supCode) throws SQLException{
		String query = "Select Price,  P.Name as CName, S.Name as SName \r\n"
						+"from (product P join sold_by  ON P.Code=ProdCode) join supplier s on S.Code=SupCode \r\n"
						+"where P.Code = ? and S.Code=? \r\n";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, String.valueOf(prodCode));
			pstatement.setString(2, String.valueOf(supCode));
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return null;           
				else {
					CartSupplier cSup = new CartSupplier();
					cSup.setCode(supCode);
					cSup.setName(result.getString("SName"));
					ArrayList<String> pNames = new ArrayList<String>();
					pNames.add(result.getString("CName"));
					cSup.setNameProducts(pNames);
					ArrayList<Integer> pCodes= new ArrayList<Integer>();
					pCodes.add(prodCode);
					cSup.setCodeProducts(pCodes);
					cSup.setTotalPrice(Float.parseFloat(result.getString("Price")));
					cSup.setProdCounter(prodCode, 1);
					
					return cSup;
				}
			}
		}
	}
	
	public boolean isValidCode(int supCode) throws SQLException{ 
		String query= "select count(*) from supplier where Code = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, String.valueOf(supCode) );
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
	
	public Float supplierFreeShipping(int supCode) throws SQLException{ 
		String query= "select FreeShipping from supplier where Code = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, String.valueOf(supCode) );
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return null;           
				else {
					return Float.parseFloat(result.getString("FreeShipping"));
				}
			}
		}
	}	

}
