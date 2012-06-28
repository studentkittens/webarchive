package webarchive.notifier;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import webarchive.api.model.CommitTag;
import webarchive.api.model.TimeStamp;
import webarchive.api.select.SelectCommitTag;
import webarchive.connection.Connection;
import webarchive.dbaccess.SqlHandler;
import webarchive.server.Server;
import webarchive.transfer.Header;
import webarchive.transfer.Message;

/**
 * Simple Notifier, which is written like the documentation told.
 * Todo: api.odel.TimeStamp...
 * @author sabrina
 */
public class Notifier extends Thread {	
	private TimeStamp lastSearch; 
	private SqlHandler sqlHandler;
	private long mIntervall;
	
	public Notifier(long intervall){
		
		mIntervall = intervall;	
		lastSearch= new TimeStamp(new Date());
	}
	
	
	public void run() {
		
		while(true){
			
			//#############//
			// start routine
			 routine(lastSearch);
			
			//#############//
			// set Date
				
				lastSearch= new TimeStamp(new Date ());
				
				
			//#############//
			// sleep (mIntervall)
				try {
				      sleep(mIntervall);
				    }
				    catch(InterruptedException e) {
				   	 //ExceptionHandling
				    }
				
				
		}
	}
	
	
	
	/*
	 * private void Notifier(Date lastSearch){ // add sqlHandler
	 * routine();
	 * 	
	 * }
	 * 
	 */
  	
	
	private void routine(TimeStamp mLastSearch) {
		//DB-Query & get list
		String where = "commitTime > " + mLastSearch.getXmlFormat(); // build where-clause example
		String orderBy = "commitTime ASC"; // order by clause example -- domainName ASC,
		List<CommitTag> list = null; 
		try {
			list = sqlHandler.select(new SelectCommitTag(where, null, orderBy)); //execute sql & get list
		} catch (Exception ex) {
			Logger.getLogger(Notifier.class.getName()).log(Level.SEVERE, null,
				ex);
		}

		//send list
		Server sv = Server.getInstance();
		Message msg = new Message(Header.NOTIFY, list);
		msg.setBroadCast();
		
		List<Connection> listConnection = sv.getObservers();
		synchronized (listConnection) {
			for ( Connection c : listConnection ) {
			 	try {
			 			c.send(msg); 
			 		} catch (Exception e) {
			 		 //ExceptionHandling
			 		}
			}
		}
	}
	
	
	
	
}
