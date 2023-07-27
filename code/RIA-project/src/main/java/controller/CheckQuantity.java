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

import com.google.gson.Gson;

import beans.CartSupplier;
import beans.Product;
import beans.SpendingRanges;
import dao.ProductDao;
import dao.SpendingRangesDao;
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
		HttpSession session = request.getSession();
		ArrayList<CartSupplier> cart = (ArrayList<CartSupplier>) session.getAttribute("cart");
		
		Integer quantity = null;
		Integer supplierCode = null;
		Integer productCode = null;
		
		ProductDao productDao = new ProductDao(connection);
		SupplierDao supplierDao = new SupplierDao(connection);
		
		try {
			quantity = Integer.parseInt(request.getParameter("quantity"));
			supplierCode = Integer.parseInt(request.getParameter("supplier_code"));
			productCode = Integer.parseInt(request.getParameter("product_code"));
			if(quantity <= 0 || productCode < 0 || supplierCode < 0 || !productDao.isValidCode(productCode) || !supplierDao.isValidCode(supplierCode)) {
				response.setStatus(403);
				response.getWriter().println("Incorrect param values");
				return;
			}
		}catch(NumberFormatException | NullPointerException e) {
			response.setStatus(403);
			response.getWriter().println("Incorrect param values");
			return;
		} catch (SQLException e) {
			response.setStatus(403);
			response.getWriter().println("db error, click again");
			return;
		}
		
		CartSupplier cartSupplier = null;
		for(CartSupplier c : cart) {
			if(c.getCode() == supplierCode) {
				cartSupplier = c;
			}
		}
		
		CartSupplier newSupplier = null;
		try {
			newSupplier = supplierDao.infoCartSupplier(productCode, supplierCode);
			newSupplier.setShippingPrice(-1);
		} catch (SQLException e) {
			response.setStatus(403);
			response.getWriter().println("db error, click again");
			return;
		}
		
		if(cartSupplier == null) {
			cartSupplier = newSupplier;
			float tmp = cartSupplier.getTotalPrice();
			cartSupplier.setTotalPrice(quantity * tmp);
			cartSupplier.getProduct(productCode).setQuantity(quantity);
			cart.add(cartSupplier);
			
		}else {
			
			Product product = null;
			for(Product p : cartSupplier.getProducts()) {
				if(p.getCode() == productCode) {
					product = p;
				}
			}
			
			if(product == null) {
				cartSupplier.setProduct(newSupplier.getProduct(productCode));
				cartSupplier.getProduct(productCode).setQuantity(quantity);
			}else {
				int prevQuant = cartSupplier.getProduct(productCode).getQuantity();
				cartSupplier.getProduct(productCode).setQuantity(quantity + prevQuant);
			}
			
			float tmp = cartSupplier.getTotalPrice();
			cartSupplier.setTotalPrice(quantity * newSupplier.getTotalPrice() + tmp);
		}
		
		
		if(cartSupplier.getShippingPrice() != 0) {
			
			Float freeShipping = null;
			try {
				freeShipping = supplierDao.supplierFreeShipping(supplierCode);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(cartSupplier.getTotalPrice() >= freeShipping) {
				cartSupplier.setShippingPrice(0);
			}else {
				SpendingRangesDao spendingRangesDao = new SpendingRangesDao(connection);
				ArrayList<SpendingRanges> spendingRanges = null;
				try {
					spendingRanges = spendingRangesDao.findSpendingRanges(supplierCode);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				int total = 0;
				int i = 0;
				int size = cartSupplier.getProducts().size();
				while(i<size) {
					total = total + cartSupplier.getProducts().get(i).getQuantity();
					i++;
				}
				
				for(SpendingRanges sp : spendingRanges) {
					if(total >= sp.getMinimumN() && (total <= sp.getMaximumN() || sp.getMaximumN() == sp.getMinimumN())) {
						cartSupplier.setShippingPrice(sp.getPrice());
					}
				}
			}
		}
		
		String json = new Gson().toJson(cart);
		response.setStatus(200);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
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
