
package webarchive.dbaccess;

import java.sql.ResultSet;
import java.sql.SQLException;
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
		date = new TimeStamp(rs.getString("commitTime"));
		return new CommitTag(id, date, domain);
		
		
	}

}
