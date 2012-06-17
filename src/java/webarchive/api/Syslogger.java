package webarchive.api;

import java.io.IOException;
import java.util.logging.Level;

public class SysLogger {
	
	/**
	 * This logs Exceptions in log-files which are readable for humans.
	 * @author SBiersack
	 */
	
	/**
	 * Initialize the logger, create log-files with the name of the class.
	 * Maximal size of a file is 1 MB.
	 * 
	 * @param Name of the class
	 * @throws Exception
	 */

	public void SysLogger(String name) throws SecurityException, IOException {
	}
	
	/**
	 * Handles Exceptions.
	 * 
	 * @param Write down, what has happened.
	 * @param Set a Level. SEVERE or WARNING in final version. 
	 * @param Send the Exception.
	 *
	 */
	public void setLog(String name, Level level , Throwable ex) {
	}
}
