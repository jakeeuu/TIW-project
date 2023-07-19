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

import beans.Supplier;
import beans.User;
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
		ArrayList<Supplier> suppliers = (ArrayList<Supplier>) session.getAttribute("cart");
		
		int quantity = 0;
		String supplierCode = null;
		String productCode = null;
		try {
			quantity = Integer.parseInt(request.getParameter("quantity"));
			supplierCode = StringEscapeUtils.escapeJava(request.getParameter("supplier_code"));
			productCode = StringEscapeUtils.escapeJava(request.getParameter("product_code"));
			if(quantity <= 0 || supplierCode.isEmpty() || productCode.isEmpty()) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
				return;
			}
		}catch(NumberFormatException | NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}
		
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
