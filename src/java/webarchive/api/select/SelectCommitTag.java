package webarchive.api.select;

import webarchive.api.model.CommitTag;

/**
 * Select-statement for CommitTag-Objects
 *
 * @author ccwelich
 */
public class SelectCommitTag extends Select<CommitTag> {

	/**
	 * selects CommitTag-objects from database by a join of commitTag and
	 * domain-table
	 *
	 * @param whereCommitTag minimal sql-syntax WHERE clauses for
	 * commitTag-table , omitted if null<br /> example: "commitTime >
	 * '2012-05-23T17:00:00'"
	 * @param whereDomain minimal sql-syntax WHERE clauses for domain- and
	 * commitTag-table , omitted if null<br /> example: "domainName =
	 * 'www.heise.de'"
	 * @param orderBy array of minimal sql-syntax ORDER BY clauses, ommitted if
	 * null<br /> examble: "lastCommitTime ASC"
	 */
	public SelectCommitTag(String whereCommitTag, String whereDomain,
		String... orderBy) {
		super(new String[]{whereDomain, whereCommitTag}, orderBy);
	}
}
