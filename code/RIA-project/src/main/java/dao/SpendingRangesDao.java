package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.SpendingRanges;

public class SpendingRangesDao {
	private Connection connection;

	public SpendingRangesDao(Connection connection) {
		this.connection = connection;
	}
	
	public ArrayList<SpendingRanges> findSpendingRanges(int SupCode) throws SQLException{
		ArrayList<SpendingRanges> spendingranges = new ArrayList<SpendingRanges>();
		String query = "select * from spending_ranges where SupCode= ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, String.valueOf(SupCode));
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return null;           ///////////////////////SE LA QUERY NON PESCA NULLA DAL DB COSA FACCIO ???
				else {
					while(result.next()) {
						SpendingRanges spendingrange = new SpendingRanges();
						spendingrange.setSupCode(Integer.parseInt(result.getString("SupCode")));
						spendingrange.setPrice(Float.parseFloat(result.getString("Price")));
						spendingrange.setMinimumN(Integer.parseInt(result.getString("MinimumN")));
						if(result.getString("MaximumN") != null) {
							spendingrange.setMaximumN(Integer.parseInt(result.getString("MaximumN")));
						}else {
							spendingrange.setMaximumN(Integer.parseInt(result.getString("MinimumN")));
						}	
						spendingranges.add(spendingrange);
						
					}
					return spendingranges;
				}
			}
		}
	}
}
