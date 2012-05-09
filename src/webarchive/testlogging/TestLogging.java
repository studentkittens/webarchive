/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.testlogging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.*;

/**
 *
 * @author ccwelich
 */
public class TestLogging {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws IOException {
	
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.INFO);
		Handler handler = new FileHandler("testlog.xml");
		Formatter formatter = new XMLFormatter();
		handler.setFormatter(formatter);
		logger.addHandler(handler);

		FileReader r;
		try {
			r = new FileReader(new File("gibtsnicht"));
		} catch (FileNotFoundException ex) {
			Logger.getLogger(TestLogging.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
}
