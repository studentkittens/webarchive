
package webarchive.dbaccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import webarchive.api.model.CommitTag;
import webarchive.api.model.TimeStamp;

/**
 * Backend of {@link webarchive.api.select.SelectCommitTag}
 * @author ccwelich
 */
public class SelectCommitTag extends SelectJoin<CommitTag> {

	public SelectCommitTag(DbAccess dbAccess) {
		super(dbAccess,new String[]{"domain","commitTag"}, new String[]{"domainId"});
	}

	@Override
	protected CommitTag fromResultSet(ResultSet rs, Object arg) throws SQLException {
		int id = rs.getInt("commitId");
		String domain = rs.getString("domainName");
		TimeStamp date = null;
		try {
			date = new TimeStamp(rs.getString("commitTime"));
		} catch (ParseException ex) {
			Logger.getLogger(SelectCommitTag.class.getName()).log(Level.SEVERE, null, ex);
		}
		return new CommitTag(id, date, domain);
		
		
	}

}
