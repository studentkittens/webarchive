package webarchive.api.select;

import java.util.List;
import webarchive.api.model.CommitTag;
//TODO tests

/**
 * Select-statement for MetaData-objects constrained by a given list of commitTags.
 * All MetaData are selected where the related commitTag is in the given list.
 * Use in favor for general SelectMetaData-class, if commits are already available.
 * @author ccwelich
 */
public class SelectMetaDataByCommitTags extends Select {

	private List<CommitTag> commits;

	/**
	 * selects MetaData-object from database by a join of mimeType, metaData and
	 * history-table. selects are constrained by a list of CommitTags.
	 *
	 * @param whereMimeType minimal sql-syntax WHERE clauses for mimeType-table
	 * , omitted if null<br /> example: "mimeName LIKE 'text/html'"
	 * @param whereMeta minimal sql-syntax WHERE clauses for metaData-table ,
	 * omitted if null<br /> example: "url LIKE 'www.heise.de%'"
	 * @param whereHistoryAdditional additional minimal sql-syntax WHERE clauses
	 * for history-table, append to commits-list with AND, omitted if null<br />
	 * example: "WHERE title NOT null"
	 * @param commits list of commit tags to use for WHERE clause
	 * @param orderBy array of minimal sql-syntax ORDER BY clauses, ommitted if
	 * null<br /> examble: "lastCommitTime ASC"
	 */
	public SelectMetaDataByCommitTags(
		List<CommitTag> commits,
		String whereHistoryAdditional,
		String whereMimeType,
		String whereMeta,
		String... orderBy) {
		super(new String[]{
				whereHistoryAdditional,
				whereMimeType,
				whereMeta
			},
			orderBy);
		this.commits = commits;
	}

	public List<CommitTag> getCommits() {
		return commits;
	}
}
