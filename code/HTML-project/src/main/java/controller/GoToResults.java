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

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import beans.Product;
import beans.User;
import dao.ProductDao;
import utils.ConnectionHandler;

/**
 * Servlet implementation class GoToResults
 */
@WebServlet("/GoToResults")
public class GoToResults extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
	private ServletContext servletContext;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToResults() {
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
		String keyWord = null;
		try {
			keyWord = StringEscapeUtils.escapeJava(request.getParameter("key_word"));
			if(keyWord.isEmpty()) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You have to write something into the search box");
				return;
			}	
		}catch(NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You have to write something into the search box");
			return;
		}	
		
		ProductDao productDao = new ProductDao(connection);
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
