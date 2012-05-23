package webarchive.api.select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import webarchive.api.model.CommitTag;
//TODO tests

/**
 * Select-statement for MetaData-objects constrained by a given list of
 * commitTags. All MetaData are selected where the related commitTag is in the
 * given list. Use in favor for general SelectMetaData-class, if commits are
 * already available.
 *
 * @author ccwelich
 */
public class SelectMetaByCommit extends Select {

	private Map<Integer, CommitTag> commits;

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
	public SelectMetaByCommit(
		List<CommitTag> commits,
		String whereHistoryAdditional,
		String whereMimeType,
		String whereMeta,
		String... orderBy) {

		super(new String[]{
				whereMimeType,
				whereMeta,
				null
			},
			orderBy);
		this.commits = new HashMap<>();
		super.getWhere()[2] = buildWhereHistory(commits, whereHistoryAdditional);
	}

	public CommitTag getCommit(int id) {
		return commits.get(id);
	}

	private String buildWhereHistory(List<CommitTag> commits, String add) {
		StringBuilder where = new StringBuilder("commitId IN (");
		for (CommitTag tag : commits) {
			final int id = tag.getId();
			this.commits.put(id, tag);
			where.append(id);
			where.append(", ");
		}
		where.setLength(where.length() - 2);
		where.append(')');
		if (add != null) {
			where.append(" AND (");
			where.append(add);
			where.append(')');
		}
		return where.toString();
	}
}
