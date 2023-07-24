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
import dao.SpendingRangesDao;
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
	private ServletContext servletContext;
	
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
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		String path = servletContext.getContextPath() + "/GoToResults";
		String keyWord = null;
		Integer productCode = null;
		
		ProductDao productDao = new ProductDao(connection);
		String clickError = null;
		
		
		try {
			keyWord = StringEscapeUtils.escapeJava(request.getParameter("key_word"));
			productCode = Integer.parseInt(request.getParameter("product_code"));
			if(productCode < 0 || !productDao.isValidCode(productCode)) 
				clickError = "this product code is invalid, click again";
			if(keyWord.isEmpty())
				response.sendRedirect(path);
		}catch(NullPointerException | NumberFormatException e) {
			clickError = "this product code is invalid, click again";
		} catch (SQLException e) {
			clickError = "db error, click again";
		}
		if(clickError!=null) {
			path = path + "?key_word=" + keyWord + "&click_error=" + clickError;
			response.sendRedirect(path);
			return;
		}
		
		
		ArrayList<Product> products = new ArrayList<Product>();
		
		try {
			products = productDao.produtcsFromSearch(keyWord);
		} catch (SQLException e) {
			clickError = "db error, click again";
			path = path + "?key_word=" + keyWord + "&click_error=" + clickError;
			response.sendRedirect(path);
			return;
		} catch (NullPointerException e) {
			path = path + "?key_word=" + keyWord;
			response.sendRedirect(path);
		}
		
		Product product = null;
		for(Product p : products) {
			if(p.getCode() == productCode)
				product = p;
		}
		
		SupplierDao supplierDao = new SupplierDao(connection);
		SpendingRangesDao spendigRangesDao = new SpendingRangesDao(connection);
		try {
			product.setSuppliers(supplierDao.findAllSuppliers(productCode));
			for(Supplier s : product.getSuppliers()) {
				s.setSpendingRanges(spendigRangesDao.findSpendingRanges(s.getCode()));
			}
		}catch (SQLException e) {
			clickError = "db error, click again";
		} catch (NullPointerException e) {
			clickError = "no supplier match for the code";
		}
		if(clickError!=null) {
			path = path + "?key_word=" + keyWord + "&click_error=" + clickError;
			response.sendRedirect(path);
			return;
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
			clickError = "db error, click again";
			path = path + "?key_word=" + keyWord + "&click_error=" + clickError;
			response.sendRedirect(path);
			return;
		}
		
		for(Supplier s : product.getSuppliers()) {
			for(CartSupplier c : cart) {
				if(c.getCode() == s.getCode()) {
					float tmp = s.getTotalProductsPrice();
					s.setTotalProductsPrice(tmp + c.getTotalPrice());
				}
			}
		}
		
		path = "/WEB-INF/Resultspage.html";
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
