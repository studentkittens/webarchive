
package webarchive.api.select;
//TODO tests

/**
 * Select-statement for CommitTag-Objects
 * @author ccwelich
 */
public class SelectCommitTag extends Select {
 /** selects CommitTag-objects from database by a join of commitTag and domain-table
	 *
	 * @param where minimal sql-syntax WHERE clauses for domain- and commitTag-table ,
	 * omitted if null<br />
	 * example: "commitTime > '2012-05-23T17:00:00' AND domainName = 'www.heise.de'"
	 * @param orderBy array of minimal sql-syntax ORDER BY clauses, ommitted if
	 * null<br /> examble: "lastCommitTime ASC"
	 */
	public SelectCommitTag(String where, String... orderBy) {
		super(new String[]{where}, orderBy);
	}

}
