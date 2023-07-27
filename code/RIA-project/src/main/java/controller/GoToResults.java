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

import org.apache.commons.lang.StringEscapeUtils;

import com.google.gson.Gson;

import beans.Product;
import dao.ProductDao;
import utils.ConnectionHandler;

/**
 * Servlet implementation class GoToResults
 */
@WebServlet("/GoToResults")
public class GoToResults extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToResults() {
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
		String keyWord = null;
		
		try {
			keyWord = StringEscapeUtils.escapeJava(request.getParameter("key_word"));
			if(keyWord.isEmpty()) {
				response.setStatus(403);
				response.getWriter().println("You have to write something into the search box");
				return;
			}	
		}catch(NullPointerException e) {
			response.setStatus(403);
			response.getWriter().println("You have to write something into the search box");
			return;
		}
		
		ProductDao productDao = new ProductDao(connection);
		ArrayList<Product> products = new ArrayList<Product>();
		
		try {
			products = productDao.produtcsFromSearch(keyWord);
		} catch (SQLException e) {
			response.setStatus(403);
			response.getWriter().println("db problem during the search of key word");
			return;
		} catch (NullPointerException e) {
			response.setStatus(403);
			response.getWriter().println("no match was found for the key word");
			return;
		}
		
		String json = new Gson().toJson(products);
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
