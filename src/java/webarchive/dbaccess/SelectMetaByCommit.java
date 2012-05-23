package webarchive.dbaccess;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import webarchive.api.model.CommitTag;
import webarchive.api.model.MetaData;
import webarchive.api.model.TimeStamp;
//TODO Tests

/**
 * Backend of {@link webarchive.api.select.SelectMetaByCommit}
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
		SQLException {
		MetaData meta = null;
		webarchive.api.select.SelectMetaByCommit map = (webarchive.api.select.SelectMetaByCommit) arg;
		try {
			URL url = new URL("http://"+rs.getString("url"));
			String mimeType = rs.getString("mimeName");
			String title = rs.getString("title");
			File path = new File(rs.getString("path"));
			TimeStamp createTime = new TimeStamp(rs.getString("createTime"));
			CommitTag commitTag = map.getCommit(rs.getInt("commitId"));
			meta = new MetaData(url, mimeType, title, path, createTime,
				commitTag);
		} catch (MalformedURLException | ParseException ex) {
			Logger.getLogger(SelectMetaByCommit.class.getName()).
				log(Level.SEVERE, null, ex);
		}
		return meta;
	}
}
