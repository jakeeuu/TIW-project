package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import beans.Order;
import beans.Product;
import beans.User;
import dao.OrderDao;
import dao.ProductDao;
import dao.SupplierDao;
import utils.ConnectionHandler;

/**
 * Servlet implementation class GoToOrder
 */
@WebServlet("/GoToOrder")
public class GoToOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
	private ServletContext servletContext;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToOrder() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
    	connection = ConnectionHandler.getConnection(getServletContext());
		servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String path = "/WEB-INF/Orderpage.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String mail = user.getMail();
		String error = null;
		
		OrderDao orderDao = new OrderDao(connection);
		ProductDao productDao = new ProductDao(connection);
		ArrayList<Order> orders = new ArrayList<Order>();
		
		try {
			orders = orderDao.printOrders(mail);
		} catch (SQLException e) {
			error = "db error";
			ctx.setVariable("error", error);
			templateEngine.process(path, ctx, response.getWriter());
			return;
		}
		
		for(Order order : orders) {
			try {
				order.setProducts(productDao.productInOrders(order.getCode()));
			} catch (SQLException e) {
				error = "db error";
				ctx.setVariable("error", error);
				templateEngine.process(path, ctx, response.getWriter());
				return;
			}
		}
		
		ctx.setVariable("orders", orders);
		templateEngine.process(path, ctx, response.getWriter());
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
