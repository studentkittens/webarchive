package webarchive.dbaccess;

import java.io.File;
import java.sql.ResultSet;
import webarchive.api.model.CommitTag;
import webarchive.api.model.MetaData;
import webarchive.api.model.TimeStamp;
//TODO Tests

/**
 * Backend of {@link webarchive.api.select.SelectMetaData}
 *
 * @author ccwelich
 */
public class SelectMetaData extends SelectJoin<MetaData> {

	public SelectMetaData(DbAccess dbAccess) {
		super(dbAccess, new String[]{
				"mimeType", "metaData", "domain", "history", "commitTag"
			}, new String[]{
				"mimeId", "metaId", "domainId", "commitId"
			});
	}

	@Override
	protected MetaData fromResultSet(ResultSet rs, Object arg) throws
		Exception {
		MetaData meta;
		String url = rs.getString("url");
		String mimeType = rs.getString("mimeName");
		String title = rs.getString("title");
		File path = new File(rs.getString("path"));
		TimeStamp createTime = new TimeStamp(rs.getString("createTime"));
		CommitTag commitTag = new CommitTag(
			rs.getInt("commitId"),
			new TimeStamp(rs.getString("commitTime")),
			rs.getString("domainName"));
		meta = new MetaData(url, mimeType, title, path, createTime,
			commitTag);

		return meta;
	}
}
