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

import webarchive.transfer.BAOS;
import webarchive.xml.XmlConf;

/**
 *
 * @author ccwelich
 */
public class TestLogging {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws IOException {
	
		Logger logger = Logger.getLogger("webarchive.xml");
		Logger logger2 = Logger.getLogger("webarchive.transfer");

		logger.setLevel(Level.INFO);
		Handler handler = new FileHandler("xml.log",true);
		
		Formatter formatter = new SimpleFormatter();
		handler.setFormatter(formatter);
		logger.addHandler(handler);
		
		logger2.setLevel(Level.INFO);
		Handler handler2 = new FileHandler("transfer.log",true);
		
		Formatter formatter2 = new SimpleFormatter();
		handler2.setFormatter(formatter2);
		logger2.addHandler(handler2);
		FileReader r;
		try {
			final File file = new File("gibtsnicht");
			System.out.println(file.getAbsolutePath());
			r = new FileReader(file);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(XmlConf.class.getName()).log(Level.SEVERE, null, ex);

		}
		try {
			final File file = new File("NOWHERE");
			System.out.println(file.getAbsolutePath());
			r = new FileReader(file);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(BAOS.class.getName()).log(Level.SEVERE, null, ex);

		}
		
	}
}
