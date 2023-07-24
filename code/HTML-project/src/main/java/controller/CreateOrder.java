package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.CartSupplier;
import beans.Product;
import beans.User;
import dao.OrderDao;
import utils.ConnectionHandler;

/**
 * Servlet implementation class CreateOrder
 */
@WebServlet("/CreateOrder")
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		ArrayList<CartSupplier> cart = (ArrayList<CartSupplier>) session.getAttribute("cart");
		Integer supplierCode = null;
		CartSupplier cartSupplier = null;
		boolean badRequest = false;
		
		try {
			supplierCode = Integer.parseInt(request.getParameter("supplier_code"));
			for(CartSupplier sp : cart) {
				if(sp.getCode() == supplierCode) {
					cartSupplier = sp;
				}
			}
			if(cartSupplier == null || supplierCode < 0) {
				badRequest = true;
			}
		}catch(NumberFormatException | NullPointerException e) {
			badRequest = true;
			e.printStackTrace();
		}
		if(badRequest == true) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error in supplier code");
			return;
		}
		
		OrderDao orderDao = new OrderDao(connection);
		Date date = new Date(System.currentTimeMillis());
		float total = cartSupplier.getTotalPrice() + cartSupplier.getShippingPrice();
		
		HashMap<Integer,Integer> counter = new HashMap<Integer,Integer>();
		for(Product p : cartSupplier.getProducts()) {
			counter.put(p.getCode(),p.getQuantity());
		}
		
		try {
			orderDao.generalOrderUpdate(user.getMail(), cartSupplier.getName(), total, date, user.getAddress(), counter);
			cart.remove(cartSupplier);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToOrder";
		response.sendRedirect(path);
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
