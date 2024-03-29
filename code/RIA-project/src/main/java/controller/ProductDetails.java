package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import beans.Supplier;
import beans.User;
import dao.ProductDao;
import dao.SpendingRangesDao;
import dao.SupplierDao;
import utils.ConnectionHandler;

/**
 * Servlet implementation class ProductDetails
 */
@WebServlet("/ProductDetails")
@MultipartConfig
public class ProductDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductDetails() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer productCode = null;
		ProductDao productDao = new ProductDao(connection);
		
		try {
			productCode = Integer.parseInt(request.getParameter("product_code"));
			if(productCode < 0 || !productDao.isValidCode(productCode)) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
				response.getWriter().println("this product code is invalid, click again");
				return;
			}
		}catch(NullPointerException | NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
			response.getWriter().println("this product code is invalid, click again");
			return;
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
			response.getWriter().println("db error, click again");
			return;
		}
		
		SupplierDao supplierDao = new SupplierDao(connection);
		SpendingRangesDao spendigRangesDao = new SpendingRangesDao(connection);
		ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
		
		try {
			suppliers = supplierDao.findAllSuppliers(productCode);
			for(Supplier s : suppliers) {
				s.setSpendingRanges(spendigRangesDao.findSpendingRanges(s.getCode()));
			}
		}catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
			response.getWriter().println("db error, click again");
			return;
		} 
		
		if(suppliers == null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
			response.getWriter().println("no supplier match for the code");
			return;
		}
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String mail = user.getMail();
		
		Date date = new Date(System.currentTimeMillis());
		Time time = new Time(date.getTime());
		try {
			productDao.insertInto(mail,productCode,date, time);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
			response.getWriter().println("db error, click again");
			return;
		}
		
		String json = new Gson().toJson(suppliers);
		response.setStatus(HttpServletResponse.SC_OK);//200
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
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
