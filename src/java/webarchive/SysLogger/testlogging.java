/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.*;

public class testlogging {

	public static void main(String[] args) throws IOException {

		String name ="testlogging";
		LogManager manager = LogManager.getLogManager();
		manager.readConfiguration(new FileInputStream("logging.properties"));	

// otherwise start jvm with parameter: java -Djava.util.logging.config.file=path/logging.properties
	
				Logger logger = Logger.getLogger(name);
manager.addLogger(logger);
	Handler handler = new FileHandler(name,1048576,5);


logger.addHandler(handler);	
	
FileReader r;
		
		

		
		
		for(int i=1; i<10000; i++)    {
			try {
			final File file = new File("nothing");
			
			r = new FileReader(file);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(name).log(Level.SEVERE, "File not Found", ex);
		}
	
		}
		
		
		/*
		* = 4 Files
		*/
		
		
	}
}
