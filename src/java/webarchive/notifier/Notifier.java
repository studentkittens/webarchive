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
 * @author SBiersack
 */
public class Notifier extends Thread {	
	private TimeStamp lastSearch; 
	private SqlHandler sqlHandler;
	private long mIntervall;
	
	public Notifier(long intervall, SqlHandler sql){		
		mIntervall = intervall;	
		lastSearch= new TimeStamp(new Date());
		this.sqlHandler = sql;
	}
		
	public void run() {	
		while(true){	
			 TimeStamp tmp = new TimeStamp(new Date ());	
			 routine(lastSearch);	
			 lastSearch  = 	tmp;
			//#############//
			// sleep (mIntervall)
				try {
				      sleep(mIntervall);
				    }
				    catch(InterruptedException e) {
				   Logger.getLogger(Notifier.class.getName()).log(Level.SEVERE, null,
								e);
				    }			
				
		}
	}	
	@SuppressWarnings("unchecked")
	private void routine(TimeStamp mLastSearch) {
		Server sv = Server.getInstance();
		Connection[] cons = sv.getObserverArray();
		if(cons.length == 0) return;
		//DB-Query & get list
		String where = "commitTime > \"" + mLastSearch.getXmlFormat()+"\""; // build where-clause example
		String orderBy = "commitTime ASC"; // order by clause example -- domainName ASC,
		List<CommitTag> list = null; 
		try {
			list = sqlHandler.select(new SelectCommitTag(where, null, orderBy)); //execute sql & get list
		} catch (Exception e2) {
			Logger.getLogger(Notifier.class.getName()).log(Level.SEVERE, null,
				e2);
		}
		if(list.size()==0) return;
		
		//send list
		
		Message msg = new Message(Header.NOTIFY, list);
		msg.setBroadCast();
		
		
		synchronized (cons) {
			for ( Connection c : cons ) {
			 	try {
			 			c.send(msg); 
			 		} catch (Exception e3) {
			 			Logger.getLogger(Notifier.class.getName()).log(Level.SEVERE, null,
			 					e3);
			 		}
			}
		}
	}
	
	
	
	
}
