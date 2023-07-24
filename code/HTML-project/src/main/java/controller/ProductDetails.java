package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.sql.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import beans.CartSupplier;
import beans.Product;
import beans.Supplier;
import beans.User;
import dao.ProductDao;
import dao.SupplierDao;
import utils.ConnectionHandler;

/**
 * Servlet implementation class ProductDetails
 */
@WebServlet("/ProductDetails")
public class ProductDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;     
	
	public void init() throws ServletException {
    	connection = ConnectionHandler.getConnection(getServletContext());
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		boolean badRequest = false;
		String keyWord = null;
		Integer productCode = null;
		
		ProductDao productDao = new ProductDao(connection);
		
		try {
			keyWord = StringEscapeUtils.escapeJava(request.getParameter("key_word"));
			productCode = Integer.parseInt(request.getParameter("product_code"));
			if(productCode < 0 || !productDao.isValidCode(productCode))
				badRequest = true;
		}catch(NullPointerException | NumberFormatException e) {
			badRequest = true;
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(badRequest) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You have to write something into the search box");
			return;
		}
		
		
		ArrayList<Product> products = new ArrayList<Product>();
		
		try {
			products = productDao.produtcsFromSearch(keyWord);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to create mission");
			return;
		} catch (NullPointerException e) {
			//se mi ritorna null significa che non ha trovato nulla che corrisponda al key word
			// come lo gestisco?? tonro alla home ?? in più mando un errore???
		}
		
		Product product = null;
		for(Product p : products) {
			if(p.getCode() == productCode)
				product = p;
		}
		
		SupplierDao supplierDao = new SupplierDao(connection);
		try {
			product.setSuppliers(supplierDao.findAllSuppliers(productCode));
		}catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to create mission");
			return;
		} catch (NullPointerException e) {
			//se mi ritorna null significa che non ha trovato nulla che corrisponda al key word
			// come lo gestisco?? tonro alla home ?? in più mando un errore???
		}
		
		
		
		HttpSession session = request.getSession();
		ArrayList<CartSupplier> cart = (ArrayList<CartSupplier>) session.getAttribute("cart");
		User user = (User) session.getAttribute("user");
		String mail = user.getMail();
		
		Date date = new Date(System.currentTimeMillis());
		Time time = new Time(date.getTime());
		try {
			productDao.insertInto(mail,productCode,date, time);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Supplier s : product.getSuppliers()) {
			for(CartSupplier c : cart) {
				if(c.getCode() == s.getCode()) {
					float tmp = s.getTotalProductsPrice();
					s.setTotalProductsPrice(tmp + c.getTotalPrice());
				}
			}
		}
		
		
		
		String path = "/WEB-INF/Resultspage.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("products", products);
		ctx.setVariable("keyWord", keyWord);
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
