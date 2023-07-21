package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import beans.CartSupplier;
import beans.Product;
import dao.SupplierDao;
import utils.ConnectionHandler;

/**
 * Servlet implementation class CheckQuantity
 */
@WebServlet("/CheckQuantity")
public class CheckQuantity extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckQuantity() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
    	connection = ConnectionHandler.getConnection(getServletContext());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		ArrayList<CartSupplier> cart = (ArrayList<CartSupplier>) session.getAttribute("cart");
		
		int quantity = 0;
		int supplierCode = 0;
		int productCode = 0;
		try {
			quantity = Integer.parseInt(request.getParameter("quantity"));
			supplierCode = Integer.parseInt(request.getParameter("supplier_code"));
			productCode = Integer.parseInt(request.getParameter("product_code"));
			if(quantity <= 0 || productCode < 0 || supplierCode < 0) {//controllo anche che il codice prodoto e quello supplier siano validi
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
				return;
			}
		}catch(NumberFormatException | NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}
		
		CartSupplier cartSupplier = null;
		for(CartSupplier c : cart) {
			if(c.getCode() == supplierCode) {
				cartSupplier = c;
			}
		}
		
		if(cartSupplier == null) {
			cartSupplier = new CartSupplier();
			//CONTINUOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
			cartSupplier.setCode(supplierCode);
		}
		
		Integer code = null;
		for(Integer c : cartSupplier.getCodeProducts()) {
			if(c == productCode) {
				code = c;
			}
		}
	
		/*
		if(code == null) {
			product = new Product();
			//CONTINUOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
			product.setCode(productCode);
		}
		*/
		
		
		SupplierDao supplierDao = new SupplierDao(connection);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	@Override
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
