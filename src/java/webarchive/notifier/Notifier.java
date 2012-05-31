/* 
 * Simple Notifier, which is written like the documentation told.
 * Need some help with the routine... I don't now wich classes/methodes i had to choose at yours...
 *  @ DB-Query and Sending the list ;)
 */


package webarchive.notifier;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import webarchive.api.model.CommitTag;
import webarchive.api.select.SelectCommitTag;
import webarchive.dbaccess.SqlHandler;

public class Notifier {
	
	private Date mLastSearch; // better prefer api.model.TimeStamp (wrapper für Date + getXmlFormat())
	private SqlHandler sqlHandler;  	
	public Notifier(Date lastSearch){ // add sqlHandler

		lastSearch=mLastSearch;
		routine();
		
	}

	private void routine() throws SQLException{
		//DB-Query & get list
		//String where = "commitTime > " + mLastSearch.getXmlFormat(); // build where-clause example
		//String orderBy = "domainName ASC, commitTime ASC"; // order by clause example
		//List<CommitTag> list = sqlHandler.select(new SelectCommitTag(where, null, orderBy)); //execute sql & get list
		 
		
		
		//send list
		/*
		 * First get an instance of "Server" by invoking Server.getInstance().
		 * Then you can get the clients, that are registered as Observers in a List<Connection> by calling Server.getInstance().getObservers()
		 * Each Connection represents a registered Client. You can iterate over this List<Connection> and use the send(Message)-method of each
		 * Connection-Object in the List.
		 * 
		 * exemplum gratum:
		 * 
		 * Server sv = Server.getInstance();
		 * Message msg = new Message(Header.NOTIFY, someData);
		 * msg.setBroadCast(); // tells the other side NOT to answer to this message, this is IMPORTANT
		 * List<Connection> list = sv.getObservers();
		 * for ( Connection c : list ) {
		 * 		try {
		 * 			c.send(msg); //<- here you actually send it to the clients
		 * 		} catch (Exception e) {
		 * 		... //ExceptionHandling
		 * 		}
		 * }
		 * 
		 */
		
	}
	
	
	
	
}
