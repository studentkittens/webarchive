package webarchive.dbaccess;

import java.sql.SQLException;
import java.util.List;

/**
 * Generic class for database access services for model classes.
 *
 * @author ccwelich
 *
 * @param <Type> type to save in database. Type is usually a model-class
 */
public abstract class SelectSingle<Type> extends Select {

	private String sqlTemplate;

	public SelectSingle(DBAccess dbAccess, String table) {
		super(dbAccess);
		buildSqlTemplate(table);
	}

	public List<Type> select(String where, String[] orderBy) throws SQLException {
		StringBuilder sql = new StringBuilder();
		if (where != null) {
			sql.append(String.format(sqlTemplate, where));
		}
		appendOrderBy(orderBy, sql);
		sql.append(';');
		return executeSelect(sql.toString());

	}

	private void buildSqlTemplate(String table) {
		StringBuilder rc = new StringBuilder("SELECT * FROM ");
		rc.append(table);
	}
}
