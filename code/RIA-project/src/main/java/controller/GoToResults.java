package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import beans.Product;
import dao.ProductDao;
import utils.ConnectionHandler;
import utils.GetEncoding;

/**
 * Servlet implementation class GoToResults
 */
@WebServlet("/GoToResults")
@MultipartConfig
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String keyWord = null;
		
		try {
			keyWord = StringEscapeUtils.escapeJava(request.getParameter("key_word"));
			if(keyWord.isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
				response.getWriter().println("You have to write something into the search box");
				return;
			}	
		}catch(NullPointerException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
			response.getWriter().println("You have to write something into the search box");
			return;
		}
		
		ProductDao productDao = new ProductDao(connection);
		ArrayList<Product> products = new ArrayList<Product>();
		
		try {
			products = productDao.produtcsFromSearch(keyWord);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
			response.getWriter().println("db problem during the search of key word");
			return;
		}
		
		if(products == null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
			response.getWriter().println("no match was found for the key word");
			return;
		}
			
		
		JsonArray jArray = new JsonArray();
		JsonObject jSonObject;

		for(Product product : products) {
				jSonObject = new JsonObject();
				
				jSonObject.addProperty("code", product.getCode());
				jSonObject.addProperty("name" , product.getName());
				jSonObject.addProperty("minimumPrice", product.getMinimumPrice());
				jSonObject.addProperty("description" , product.getDescription());
				jSonObject.addProperty("category" , product.getCategory());
				try {
					jSonObject.addProperty("photo" , GetEncoding.getImageEncoding(product.getPhoto() , getServletContext()));
				} catch(IOException e) {
					jSonObject.addProperty("photo" , "");
				}
				
				jArray.add(jSonObject);
		}
		
		String json = new GsonBuilder().create().toJson(jArray);
		response.setStatus(HttpServletResponse.SC_OK);//200
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
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
