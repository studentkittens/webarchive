package SysLogger;

import java.io.IOException;
import java.util.logging.*;

/**
 * @author sbiersack
 */

public class SysLogger extends Thread {

private Handler handler; 
private Logger logger;
private String mName; 
private Level mLevel ; 
private Throwable mEx;
private String logName;

	public SysLogger(String name) throws SecurityException, IOException {
		logName=name;
		config();
}

	private void config() throws SecurityException, IOException{
		logger = Logger.getLogger(logName);
		handler = new FileHandler(logName,1048576,5,true);
		logger.setLevel(Level.ALL); // LogLevel: For final version Level.SEVERE
		Formatter formatter = new SimpleFormatter();
		handler.setFormatter(formatter);
		logger.addHandler(handler);	
		}

	public void setLog(String name, Level level , Throwable ex)	{
		mName = name;
		mLevel = level;
		mEx=ex;
		loggin();				
	}
		
	private synchronized  void loggin() {
		 Logger.getLogger(logName).log(mLevel, mName, mEx);
		
	}
	
}
