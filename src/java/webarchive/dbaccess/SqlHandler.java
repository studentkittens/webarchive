package webarchive.dbaccess;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import webarchive.handler.Handler;
//TODO Tests

/**
 * Access class for SQL-Statements. accepts raw select-statements from
 * api.select and executes them on their backend counterpart. The simplenames of
 * both classes are asserted as equal. So package api.select.SelectCommitTag is
 * mapped to dbaccess.SelectCommitTag.
 *
 * @author ccwelich
 */
public class SqlHandler extends Handler {

	private Map<String, Select> selectMethods;

	/**
	 * creates a SqlHandler-object, which initializes all Backend-Select-methods
	 *
	 * @param dbaccess dbaccess class for dbconnections
	 */
	public SqlHandler(DbAccess dbaccess) {
		selectMethods = new HashMap<>();
		add(new SelectCommitTag(dbaccess));
		add(new SelectMetaData(dbaccess));
		add(new SelectMetaByCommit(dbaccess));
	}

	private void add(Select select) {
		selectMethods.put(select.getClass().getSimpleName(), select);
	}

	/**
	 *
	 * @param select, see api
	 * @return List of selected objects
	 * @throws SQLException
	 * @throws UnsupportedOperationException if select is not recognized or
	 * added in list.
	 * @throws Exception if select has failed
	 */
	public List select(webarchive.api.select.Select select) throws Exception {
		Select selectMethod = selectMethods.get(select.getClass().
			getSimpleName());
		if (selectMethod == null) {
			throw new UnsupportedOperationException(select.getClass().
				getSimpleName() + " method not yet implemented");
		}
		return selectMethod.select(select);
	}
}
