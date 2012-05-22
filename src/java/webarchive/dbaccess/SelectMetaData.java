
package webarchive.dbaccess;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import webarchive.api.CommitTag;
import webarchive.api.TimeStamp;
import webarchive.api.MetaData;

/**
 *
 * @author ccwelich
 */
public class SelectMetaData extends SelectJoin<MetaData> {
	
	public List<MetaData> select(List<CommitTag> commits, String[] orderBy) throws SQLException {
		Map<Integer, CommitTag> map = new HashMap<>();
		StringBuilder sql = new StringBuilder("SELECT * FROM mimeType JOIN (SELECT * FROM metaData JOIN(SELECT * FROM history WHERE commitId IN (");
		for (CommitTag tag : commits) {
			final int id = tag.getId();
			map.put(id, tag);
			sql.append(id);
			sql.append(", ");
		}
		sql.setLength(sql.length()-2);
		sql.append(")) USING (metaId)) USING (mimeId");
		appendOrderBy(orderBy,sql);
		Connection connection = null;
		List<MetaData> list = new LinkedList<>();
		try {
			connection = dbAccess.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql.toString());
			while (rs.next()) {
				list.add(fromResultSetWithCommitTags(rs,map));
			}
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return list;
	}

	@Override
	protected MetaData fromResultSet(ResultSet rs) throws SQLException {
		MetaData meta = null;
		try {
			URL url = rs.getURL("url");
			String mimeType = rs.getString("mimeName");
			String title = rs.getString("title");
			File path = new File(rs.getString("path"));
			TimeStamp createTime = new TimeStamp(rs.getString("createTime")); 
			CommitTag commitTag = new CommitTag(
				rs.getInt("commitId"), 
				new TimeStamp(rs.getString("commitTime")),
				rs.getString("domainName")
			);
			meta = new MetaData(url, mimeType, title, path, createTime, commitTag);
		} catch (ParseException ex) {
			Logger.getLogger(SelectMetaData.class.getName()).log(Level.SEVERE, null, ex);
		}
		return meta;
	}

	public List<MetaData> select(String whereMimeType, String whereMeta, String whereDomain, String whereCommitTagJoinHistory, String[] orderBy) throws SQLException {
		return super.select(new String[]{whereMimeType, whereMeta, whereDomain, whereCommitTagJoinHistory}, orderBy);
	}

	public SelectMetaData(DbAccess dbAccess) {
		super(dbAccess, new String[]{
			"mimeType",	"metaData", "domain", "commitTag", "history"
		}, new String[]{
			"mimeId",	"metaId",	"domainId", "commitId"
		});
	}

	private MetaData fromResultSetWithCommitTags(ResultSet rs, Map<Integer, CommitTag> map) throws SQLException {
		MetaData meta = null;
		try {
			URL url = rs.getURL("url");
			String mimeType = rs.getString("mimeName");
			String title = rs.getString("title");
			File path = new File(rs.getString("path"));
			TimeStamp createTime = new TimeStamp(rs.getString("createTime")); 
			CommitTag commitTag = map.get(rs.getInt("commitId"));
			meta = new MetaData(url, mimeType, title, path, createTime, commitTag);
		} catch (ParseException ex) {
			Logger.getLogger(SelectMetaData.class.getName()).log(Level.SEVERE, null, ex);
		}
		return meta;
	}
}

