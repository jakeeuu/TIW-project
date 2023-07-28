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

import beans.Order;
import beans.User;
import dao.OrderDao;
import dao.ProductDao;
import utils.ConnectionHandler;

/**
 * Servlet implementation class GoToOrder
 */
@WebServlet("/GoToOrder")
public class GoToOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToOrder() {
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
		User user = (User) session.getAttribute("user");
		String mail = user.getMail();
		
		OrderDao orderDao = new OrderDao(connection);
		ProductDao productDao = new ProductDao(connection);
		ArrayList<Order> orders = new ArrayList<Order>();
		
		try {
			orders = orderDao.printOrders(mail);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
			response.getWriter().println("db error, click again");
			return;
		}
		
		if(orders != null) {
			for(Order order : orders) {
				try {
					order.setProducts(productDao.productInOrders(order.getCode()));
				} catch (SQLException e) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
					response.getWriter().println("db error, click again");
					return;
				}
			}
		}
		
		String json = new Gson().toJson(orders);
		response.setStatus(HttpServletResponse.SC_OK);//200
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
