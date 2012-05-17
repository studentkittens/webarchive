package webarchive.dbaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import webarchive.api.CommitTag;

/**
 * Generic class for database access services for model classes.
 *
 * @author ccwelich
 *
 * @param <Type> type to save in database. Type is usually a model-class
 */
public abstract class SelectJoin<Type> extends Select {

	private String sqlTemplate;
	private int maxWhere;

	public SelectJoin(DBAccess dbAccess, String[] tables, String[] keys) {
		super(dbAccess);
		this.sqlTemplate = buildSqlTemplate(tables, keys);
		this.maxWhere = keys.length;
		System.out.println("SelectJoin init: sqlTemplate=" + sqlTemplate);
	}

	public List<Type> select(String[] where, String[] orderBy) throws SQLException {
		String s = "";
		if (where != null) {
			assert where.length <= maxWhere;
			for (int i = 0; i < where.length; i++) {
				where[i] = "WHERE " + where[i];
			}
			s = String.format(sqlTemplate, where);
		} 
		StringBuilder sql = new StringBuilder(s);
		appendOrderBy(orderBy, sql);
		sql.append(';');
		System.out.println("select: sql="+sql);
		return executeSelect(sql.toString());
	}

	private static String buildSqlTemplate(String[] tables, String[] using) {
		assert tables.length > 1 && using.length == tables.length - 1;
		StringBuilder bld = new StringBuilder();
		buildSqlRec(bld, tables, using, 0);
		return bld.toString();
	}

	private static void buildSqlRec(StringBuilder bld, String[] tables, String[] using, int index) {
		bld.append("SELECT * FROM ");
		bld.append(tables[index]);
		bld.append(" JOIN ");
		if (index < tables.length - 2) {
			bld.append('(');
			buildSqlRec(bld, tables, using, index + 1);
			bld.append(')');
		} else {
			bld.append(tables[index + 1]);
		}
		bld.append(" USING(");
		bld.append(using[index]);
		bld.append(")");
		bld.append(" %");
		bld.append(index + 1);
		bld.append("$s");
	}
}
