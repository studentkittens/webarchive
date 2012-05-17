package webarchive.dbaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Generic class for database access services for model classes.
 *
 * @author ccwelich
 *
 * @param <Type> type to save in database. Type is usually a model-class
 */
public abstract class Select<Type> {

	private DBAccess dbAccess;

	public Select(DBAccess dbAccess) {
		super();
		this.dbAccess = dbAccess;
	}

	/**
	 * @param rs current row of resultset
	 * @return one Object of this type
	 * @throws SQLException
	 */
	protected abstract Type fromResultSet(ResultSet rs) throws SQLException;

	/**
	 * return LinkedList of type t
	 *
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	protected List<Type> executeSelect(String selectStatement) throws SQLException {
		Connection connection = null;
		List<Type> list = new LinkedList<>();
		try {
			connection = dbAccess.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(selectStatement);
			while (rs.next()) {
				list.add(fromResultSet(rs));
			}
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return list;
	}

	protected void appendOrderBy(String[] orderBy, StringBuilder sql) {
		if (orderBy != null && orderBy.length > 0) {
			sql.append(" ORDER BY ");
			for (String val : orderBy) {
				sql.append(val);
				sql.append(", ");
			}
			sql.setLength(sql.length() - 2);
		}
	}
}
