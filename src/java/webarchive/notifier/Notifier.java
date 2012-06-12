/* 
 * Simple Notifier, which is written like the documentation told.
 * Todo: api.odel.TimeStamp...
 */


package webarchive.notifier;

import java.sql.SQLException;
import java.lang.InterruptedException
import java.util.List;
import java.util.Iterator;
import java.util.Date;
import webarchive.api.model.CommitTag;
import webarchive.api.select.SelectCommitTag;
import webarchive.dbaccess.SqlHandler;


public class Notifier extends Thread {	
	private Date lastSearch; // better prefer api.model.TimeStamp (wrapper für Date + getXmlFormat())
	private dbaccess.SqlHandler sqlHandler;
	private long mIntervall;
	
	public Notifier(long intervall){
		
		mIntervall = intervall;	
		lastSearch=new Date(0);
	}
	
	
	public void run() {
		
		while(true){
			
			//#############//
			// start routine
			new routine(lastSearch);
			
			//#############//
			// set Date
				long a;
				a=System.currentTimeMillis();
				lastSearch=new Date (a);
				
				
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
  	
	
	private void routine(Date mLastSearch) throws SQLException{
		//DB-Query & get list
		String where = "commitTime > " + mLastSearch.getXmlFormat(); // build where-clause example
		String orderBy = "commitTime ASC"; // order by clause example -- domainName ASC,
		List<CommitTag> list = sqlHandler.select(new SelectCommitTag(where, null, orderBy)); //execute sql & get list

		//send list
		Server sv = Server.getInstance();
		Message msg = new Message(Header.NOTIFY, list);
		msg.setBroadCast();
		
		List<Connection> list = sv.getObservers();
		for ( Connection c : list ) {
		 	try {
		 			c.send(msg); 
		 		} catch (Exception e) {
		 		 //ExceptionHandling
		 		}
		 }
	}
	
	
	
	
}
