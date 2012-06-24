package webarchive.dbaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Generic class to select data from db and map it java data-objects.
 *
 * @author ccwelich
 *
 * @param <Type> Type is is the classtype of results of selects.
 */
public abstract class Select<Type> {

	protected DbAccess dbAccess;

	public Select(DbAccess dbAccess) {
		super();
		this.dbAccess = dbAccess;
	}

	/**
	 * @param rs current row of resultset
	 * @param arg optional parameter, provided by executeSelect
	 * @return one Object of Type
	 * @throws SQLException
	 */
	protected abstract Type fromResultSet(ResultSet rs, Object arg) throws
		Exception;

	/**
	 * return List of type
	 *
	 * @param selectStatement complete selectStatement
	 * @param arg optional parameter, delegated to fromResultSet()
	 * @return a list of type
	 * @throws SQLException
	 */
	protected List<Type> executeSelect(String selectStatement, Object arg) throws
		Exception {
		Connection connection = null;
		List<Type> list = new LinkedList<>();
		try {
			connection = dbAccess.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(selectStatement);
			while (rs.next()) {
				list.add(fromResultSet(rs, arg));
			}
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return list;
	}

	/**
	 * appends the orderBy clauses at the end of the SQL-statement
	 *
	 * @param orderBy
	 * @param sql
	 */
	protected void appendOrderBy(String[] orderBy, StringBuilder sql) {
		if (orderBy != null && orderBy.length > 0) {
			sql.append(" ORDER BY ");
			for (String val : orderBy) {
				assert val != null;
				sql.append(val);
				sql.append(", ");
			}
			sql.setLength(sql.length() - 2);
		}
	}

	abstract List<Type> select(webarchive.api.select.Select<Type> select) throws Exception;
}
