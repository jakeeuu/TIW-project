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

import com.google.gson.Gson;

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
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckLogin() {
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
		
		String mail = request.getParameter("mail");
		String password = request.getParameter("password");
		
		if (mail == null || mail.isEmpty() || password == null || password.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
			response.getWriter().println("Missing credential");
			return;
		}
		
		UserDao userDao = new UserDao(connection);
		User user = null;
		try {
			user = userDao.checkCredentials(mail, password);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
			response.getWriter().println("Failure in database credential checking");
			return;
		}
		
		
		if(user == null){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
			response.getWriter().println("Incorrect mail or password");
		}else {
			
			request.getSession().setAttribute("user", user);
			response.setStatus(200);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println(mail);
			
			/*ArrayList<CartSupplier> cart = null;
			if(request.getSession().getAttribute("cart") == null) {
				cart = new ArrayList<CartSupplier>();
				request.getSession().setAttribute("cart", cart);
			}else {
				cart = (ArrayList<CartSupplier>) request.getSession().getAttribute("cart");
			}
			
			String json = new Gson().toJson(cart);
			response.setStatus(200);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json);*/
			
		}
		
		
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
