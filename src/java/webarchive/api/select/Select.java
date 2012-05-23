package webarchive.api.select;
//TODO tests

/**
 * Abstract class for userdefined SELECT-statements. contains a list of minimal
 * where and orderBy Clauses.
 * Where-clauses have the shape of a regular SQL-Where-statement, but the "where" at the beginning is omitted.
 * OrderBy-clauses have the shape <lt/>column<gt/> [ ASC | DESC ]
 *
 * @author ccwelich
 */
public abstract class Select {

	private String[] where;
	private String[] orderBy;

	protected Select(String[] where, String[] orderBy) {
		this.where = where;
		this.orderBy = orderBy;
	}

	/**
	 * get all orderBy-clauses
	 *
	 * @return orderBy-clauses
	 */
	public String[] getOrderBy() {
		return orderBy;
	}

	/**
	 * get all where-clauses
	 *
	 * @return where-clauses
	 */
	public String[] getWhere() {
		return where;
	}
}
