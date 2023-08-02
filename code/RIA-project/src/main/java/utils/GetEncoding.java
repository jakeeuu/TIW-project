package utils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;

public class GetEncoding {


	public static String getImageEncoding(String fileImage , ServletContext context) throws IOException {
		
		String imgFolderPath = context.getInitParameter("imgFolderPath");
		
		File file = new File(imgFolderPath + fileImage);
		
		if (!file.exists() || file.isDirectory()) {
			return null;
		}
		
		
		byte[] fileContent = FileUtils.readFileToByteArray(file);
		String encodedString = Base64.getEncoder().encodeToString(fileContent);
		
		encodedString = "data:" + context.getMimeType(fileImage) + ";base64," + encodedString ;
		
		return encodedString;
	}
}
