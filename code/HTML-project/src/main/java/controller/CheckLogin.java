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

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import beans.CartSupplier;
import beans.User;
import dao.UserDao;
import utils.ConnectionHandler;

/**
 * Servlet implementation class CheckLogin
 */
@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Connection connection = null;
	private TemplateEngine templateEngine;
    private ServletContext servletContext;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckLogin() {
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String mail = request.getParameter("mail");
		String password = request.getParameter("password");
		String error = null;
		
		if (mail == null || mail.isEmpty() || password == null || password.isEmpty()) {
			error = "Missing credential";
		}
		
		UserDao userDao = new UserDao(connection);
		User user = null;
		try {
			user = userDao.checkCredentials(mail, password);
		} catch (SQLException e) {
			error = "Failure in database credential checking";
		}
		
		if(user == null) {
			error = "Incorrect mail or password";
		}
		
		String path;
		if (error != null) {
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("error", error);
			path = "/LoginPage.html";
			templateEngine.process(path, ctx, response.getWriter());
		} else {
			ArrayList<CartSupplier> cart = new ArrayList<CartSupplier>();
			request.getSession().setAttribute("cart", cart);
			
			request.getSession().setAttribute("user", user);
			path = servletContext.getContextPath() + "/GoToHome";
			response.sendRedirect(path);
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
