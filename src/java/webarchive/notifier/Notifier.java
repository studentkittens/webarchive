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
		
	}
	
	
	
	
}
