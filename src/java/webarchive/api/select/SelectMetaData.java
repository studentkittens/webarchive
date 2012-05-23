package webarchive.api.select;
//TODO tests

/**
 * General Select-statement for MetaData-Objects
 *
 * @author ccwelich
 */
public class SelectMetaData extends Select {

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
	public SelectMetaData(
		String whereMimeType,
		String whereMeta,
		String whereDomain,
		String whereHistory,
				String whereCommitTag,

		String... orderBy) {

		super(
			new String[]{
				whereMimeType,
				whereMeta,
				whereDomain,
				whereHistory,
				whereCommitTag},
			orderBy);
	}
}
