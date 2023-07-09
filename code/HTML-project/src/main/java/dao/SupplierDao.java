package dao;

import java.sql.Connection;

import beans.CartSupplier;

public class SupplierDao {

	private Connection connection;

	public SupplierDao(Connection connection) {
		this.connection = connection;
	}
	
	public CartSupplier addProductToCart(String supplierCode, String productCode) {
		
	}
}
