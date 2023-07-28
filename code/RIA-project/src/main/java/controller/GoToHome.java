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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import beans.Product;
import beans.User;
import dao.ProductDao;
import utils.ConnectionHandler;
import utils.GetEncoding;

/**
 * Servlet implementation class GoToHome
 */
@WebServlet("/GoToHome")
public class GoToHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToHome() {
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
		ProductDao productDao = new ProductDao(connection);
		ArrayList<Product> products = new ArrayList<Product>();
		
		
		try {
			products = productDao.produtcsToVisualize(user);
		}catch(SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//500
			response.getWriter().println("Not possible to visualize top five products list for a db problem");
			return;
		}
		
		JsonArray jArray = new JsonArray();
		JsonObject jSonObject;

		for(Product product : products) {
				jSonObject = new JsonObject();
				
				jSonObject.addProperty("code", product.getCode());
				jSonObject.addProperty("name" , product.getName());
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
		response.getWriter().write(json);   /// se non va provo con println anzichè write 
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
