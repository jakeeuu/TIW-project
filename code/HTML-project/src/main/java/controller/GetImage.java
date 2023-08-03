package controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetImage
 */
@WebServlet("/GetImage")
public class GetImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String imgFolderPath;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetImage() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException{
    	imgFolderPath = getServletContext().getInitParameter("imgFolderPath");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String name = request.getParameter("image");
		
		if (name == null || name.equals("/")) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to visualize the image");
			return;
		}
		
		String fileName = URLDecoder.decode(name.substring(0), "UTF-8");
		File file = new File(imgFolderPath + fileName);
		
		if(!file.exists() || file.isDirectory()) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to visualize the image");
			return;
		}
		
	
		response.setHeader("Content-Type", getServletContext().getMimeType(fileName));
		response.setHeader("Content-Length", String.valueOf(file.length()));
		response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
		
		
		Files.copy(file.toPath(), response.getOutputStream());	
	}

}
