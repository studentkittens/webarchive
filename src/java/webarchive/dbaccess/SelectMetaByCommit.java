package webarchive.dbaccess;

import java.io.File;
import java.sql.ResultSet;
import webarchive.api.model.CommitTag;
import webarchive.api.model.MetaData;
import webarchive.api.model.TimeStamp;

/**
 * Backend of {@link webarchive.api.select.SelectMetaByCommit}
 *
 * @author ccwelich
 */
public class SelectMetaByCommit extends SelectJoin<MetaData> {

	public SelectMetaByCommit(DbAccess dbAccess) {
		super(dbAccess, new String[]{
				"mimeType", "metaData", "history"
			}, new String[]{
				"mimeId", "metaId"
			});
	}

	@Override
	protected MetaData fromResultSet(ResultSet rs, Object arg) throws
		Exception {
		MetaData meta;
		webarchive.api.select.SelectMetaByCommit map = 
			(webarchive.api.select.SelectMetaByCommit) arg;
		String url = rs.getString("url");
		String mimeType = rs.getString("mimeName");
		String title = rs.getString("title");
		File path = new File(rs.getString("path"));
		TimeStamp createTime = new TimeStamp(rs.getString("createTime"));
		CommitTag commitTag = map.getCommit(rs.getInt("commitId"));
		meta = new MetaData(url, mimeType, title, path, createTime,
			commitTag);

		return meta;
	}
}
