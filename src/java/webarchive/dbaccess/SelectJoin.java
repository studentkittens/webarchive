package webarchive.dbaccess;

import java.sql.SQLException;
import java.util.List;

/**
 * Generic class to build and execute Select-statements. A SQL-template is built
 * at initialization by a array of tablenames and keys. n = number of table -1
 * <br/> number of keys = n - 1 <br/> If n==1, keys have to be null. The
 * template will then look like this "SELECT * FROM <table0> %1$s" <br/> If more
 * than one table is given, the tables will be joined by an equi join: table_i
 * JOIN table_i+1 USING(key_i) <br/> The form looks like this: "SELECT * FROM
 * <table_0> JOIN (... (SELECT * FROM <table_n> %4$s) USING(<key_n-1>) %<n>$s)
 * ... ) USING(<key_0>) %1$s" <br/> the "%<i>$s" tags are wildcards for where
 * statements. There are n where statements allowed.
 *
 * @author ccwelich
 *
 * @param <Type> Type is is the classtype of results of selects.
 */
public abstract class SelectJoin<Type> extends Select<Type> {

	private String sqlTemplate;
	private int maxWhere; // check value for asserts

	/**
	 * see description above
	 *
	 * @param dbAccess
	 * @param tables array of table names.
	 * @param keys array of key names. These are column-names which are the key
	 * for a EQUI-JOIN between two tables.
	 */
	public SelectJoin(DbAccess dbAccess, String[] tables, String[] keys) {
		super(dbAccess);
		this.sqlTemplate = buildSqlTemplate(tables, keys);
		this.maxWhere = tables.length;
	}

	/**
	 * executes select on database an returns a list of type
	 *
	 * @param where array of where clause. the length of the array is asserted
	 * as the number of tables, hence omitted where-clauses should be set as
	 * null. Only use SQL-expressions without leading "WHERE"
	 * @param orderBy array of orderBy-statements will be appended to the
	 * statement, will be ignored if null. Only use minimal expressions like:
	 * "<column> [ASC | DESC]"
	 * @param arg optional argument, can be used by implementation of
	 * fromResultSet()
	 * @return a list of Type
	 * @throws SQLException
	 */
	public List<Type> select(String[] where, String[] orderBy, Object arg) throws
		Exception {
		String s = insertWhere(where);
		StringBuilder sql = new StringBuilder(s);
		appendOrderBy(orderBy, sql);
		sql.append(';');
		return executeSelect(sql.toString(), arg);
	}

	@Override
	public List<Type> select(webarchive.api.select.Select<Type> select) throws
		Exception {

		return select(select.getWhere(), select.getOrderBy(), select);
	}

	/**
	 * get the template of the SQL-SELECT-statement
	 *
	 * @return the sqlTemplate
	 */
	public String getSqlTemplate() {
		return sqlTemplate;
	}

	/**
	 * array of where clause will be inserted int sqlTemplate
	 *
	 * @param where
	 * @return sqlStatement
	 */
	protected String insertWhere(String[] where) {
		assert where != null;
		assert where.length == maxWhere : "where.length=" + where.length + " != maxWhere " + maxWhere;
		String rc;
		int i = 0;
		for (; i < maxWhere; i++) {
			String tmp = where[i];
			where[i] = (tmp == null) ? "" : " WHERE " + truncSemiColon(tmp);
		}
		return String.format(sqlTemplate, (Object[]) where);
	}

	private static String buildSqlTemplate(String[] tables, String[] using) {
		assert tables.length > 0;
		assert (tables.length == 1 && using == null) || (using.length == tables.length - 1);
		StringBuilder bld = new StringBuilder();
		buildSqlRec(bld, tables, using, 0);
		return bld.toString();
	}

	@Override
	public String toString() {
		return "SelectJoin{" + "sqlTemplate=" + sqlTemplate + ", maxWhere=" + maxWhere + '}';
	}

	private static void buildSqlRec(StringBuilder bld, String[] tables,
		String[] using, int index) {
		bld.append("SELECT * FROM ");
		bld.append(tables[index]);
		if (index < tables.length - 1) {
			bld.append(" JOIN ");
			bld.append('(');
			buildSqlRec(bld, tables, using, index + 1);
			bld.append(')');
			bld.append(" USING(");
			bld.append(using[index]);
			bld.append(")");
		}

		bld.append("%");
		bld.append(index + 1);
		bld.append("$s");
	}
}
