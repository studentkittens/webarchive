
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
	public SelectCommitTag(String whereCommitTag, String whereDomain, String... orderBy) {
		super(new String[]{whereDomain, whereCommitTag}, orderBy);
	}

}
/**
	 * selects metadata objects from database by a join of mimeType, metaData,
	 * domain, commitTag and history-table
	 *
	 * @param whereMimeType minimal sql-syntax WHERE clauses for mimeType-table
	 * , omitted if null<br /> 
	 * example: "mimeName LIKE 'text/html'"
	 * @param whereMeta minimal sql-syntax WHERE clauses for metaData-table ,
	 * omitted if null<br /> 
	 * example: "url LIKE 'www.heise.de%'"
	 * @param whereDomain minimal sql-syntax WHERE clauses for domain-table ,
	 * omitted if null<br />
	 * @param whereCommitTag minimal sql-syntax WHERE clauses for commitTag-table, omitted if null<br /> example:
	 * "commitTime > '2012-05-15T17:30:00';"
	 * @param whereHistory minimal sql-syntax WHERE clauses for history-table , omitted if null<br /> example:
	 * "title NOT null AND commitTime > '2012-05-15T17:30:00';"
	 * @param orderBy array of minimal sql-syntax ORDER BY clauses, ommitted if
	 * null<br /> examble: "lastCommitTime ASC"
	 */