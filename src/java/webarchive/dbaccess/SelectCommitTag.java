
package webarchive.dbaccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import webarchive.api.CommitTag;
import webarchive.api.Date;

/**
 *
 * @author ccwelich
 */
public class SelectCommitTag extends SelectJoin<CommitTag> {

	public SelectCommitTag(DBAccess dbAccess) {
		super(dbAccess,new String[]{"commitTag","domain"}, new String[]{"domainId"});
	}

	public List<CommitTag> select(String where, String[] orderBy) throws SQLException {
		return super.select(new String[]{where}, orderBy);
	}
	
	@Override
	protected CommitTag fromResultSet(ResultSet rs) throws SQLException {
		int id = rs.getInt("commitId");
		String domain = rs.getString("domainName");
		Date date = null;
		try {
			date = new Date(rs.getString("commitTime"));
		} catch (ParseException ex) {
			Logger.getLogger(SelectCommitTag.class.getName()).log(Level.SEVERE, null, ex);
		}
		return new CommitTag(id, date, domain);
		
		
	}

}
