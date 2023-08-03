package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import beans.CartSupplier;
import beans.Product;
import beans.SpendingRanges;
import beans.User;
import dao.OrderDao;
import dao.ProductDao;
import dao.SpendingRangesDao;
import dao.SupplierDao;
import utils.ConnectionHandler;

/**
 * Servlet implementation class CreateOrder
 */
@WebServlet("/CreateOrder")
@MultipartConfig
public class CreateOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateOrder() {
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
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
		StringBuilder jsonBody = new StringBuilder();
        String line;
        CartSupplier cartSupplier = null;
        while ((line = reader.readLine()) != null) {
            jsonBody.append(line);
        }
        reader.close();
        
        String json = jsonBody.toString();
        Gson gson = new Gson();
        
        try {
        	cartSupplier = gson.fromJson(json, CartSupplier.class);
            
            SupplierDao supplierDao = new SupplierDao(connection);
            ProductDao productDao = new ProductDao(connection);
            
            float total = 0;
            int totalNumber = 0;
            	
        	try {
    			if(cartSupplier.getCode() >= 0 || supplierDao.areValid(cartSupplier.getCode(), cartSupplier.getName())) {
    				for(Product product : cartSupplier.getProducts()) {
    					if(product.getCode() >= 0 || productDao.areValid(product.getCode(), product.getName())) {
    						if(product.getQuantity() > 0) {
    							if(productDao.matching(product, cartSupplier)) {
    								total = total + product.getPrice() * product.getQuantity();
    								totalNumber = totalNumber + product.getQuantity();
    							}else {
    								response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
    								response.getWriter().println(product.getName() + " is not sold by " + cartSupplier.getName());
    								return;
    							}
    						}else {
    							response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
    							response.getWriter().println("incorrect quantity");
    							return;
    						}
    					}else {
    						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
    						response.getWriter().println("incorrect product code");
    						return;
    					}
    				}
    			}else {
    				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
    				response.getWriter().println("incorrect supplier code");
    				return;
    			}
    		} catch (SQLException e) {
    			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
    			response.getWriter().println("db error, click again");
    			return;
    		}
            
            if(total != cartSupplier.getTotalPrice()) {
            	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
    			response.getWriter().println("incorrect total price");
    			return;
            }
            
            SpendingRangesDao spendingRangesDao = new SpendingRangesDao(connection);
    		ArrayList<SpendingRanges> spendingRanges = null;
    		boolean isCartValid = false;
    		try {
    			spendingRanges = spendingRangesDao.findSpendingRanges(cartSupplier.getCode());
    		} catch (SQLException e) {
    			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
    			response.getWriter().println("db error, click again");
    			return;
    		}
    	
    		
    		for(SpendingRanges sp : spendingRanges) {
    			if(totalNumber >= sp.getMinimumN() && (totalNumber <= sp.getMaximumN() || sp.getMaximumN() == sp.getMinimumN()) && cartSupplier.getShippingPrice() == sp.getPrice()) {
    				isCartValid = true;
    			}
    		}
    		
    		if(cartSupplier.getShippingPrice() == 0 && !isCartValid) {
    			Float freeShipping = null;
    			try {
    				freeShipping = supplierDao.supplierFreeShipping(cartSupplier.getCode());
    				if(cartSupplier.getTotalPrice() < freeShipping) {
    					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
    					response.getWriter().println("incorrect shipping price");
    					return;
    				}
    			} catch (SQLException e) {
    				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
    				response.getWriter().println("db error, click again");
    				return;
    			}
    		}else if(!isCartValid) {
    			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
    			response.getWriter().println("incorrect shipping price");
    			return;
    		}
            
    		HttpSession session = request.getSession();
    		User user = (User) session.getAttribute("user");
    		OrderDao orderDao = new OrderDao(connection);
    		Date date = new Date(System.currentTimeMillis());
    		float orderTotal = cartSupplier.getTotalPrice() + cartSupplier.getShippingPrice();
    		
    		HashMap<Integer,Integer> counter = new HashMap<Integer,Integer>();
    		for(Product p : cartSupplier.getProducts()) {
    			counter.put(p.getCode(),p.getQuantity());
    		}
    		
    		try {
    			orderDao.generalOrderUpdate(user.getMail(), cartSupplier.getName(), orderTotal, date, user.getAddress(), cartSupplier.getCode() ,counter);
    		} catch (SQLException e) {
    			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
    			response.getWriter().println("db error, click again");
    			return;
    		}
    		
    		String path = "/GoToOrder";
    		RequestDispatcher dispatcher = request.getRequestDispatcher(path);
    		dispatcher.forward(request, response);
    		
        } catch (JsonSyntaxException e) {
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
			response.getWriter().println("incorrect json");
			return;
        }
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
