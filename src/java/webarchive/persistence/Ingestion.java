package webarchive.persistence;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Generic class for database access services for model classes.
 *
 * @author ccwelich
 *
 * @param <Type> type to save in database. Type is usually a model-class
 */
public abstract class Ingestion<Type> {

	protected String tableName, insertStatement, updateStatement, deleteStatement, selectStatement;
	protected DBAccess dbAccess;
	private static final int UPDATE = 0, DELETE = 1, INSERT = 2;

	public Ingestion(DBAccess dbAccess, String tableName) {
		super();
		this.dbAccess = dbAccess;
		this.tableName = tableName;
		this.insertStatement = insertStatement();
		this.updateStatement = updateStatement();
		this.deleteStatement = deleteStatement();
		this.selectStatement = "SELECT * FROM " + tableName;
	}

	/**
	 * Override to set create-table-statement. This method is called once at the
	 * initializiation of an GenDBPersitence object.
	 *
	 * @return the create-table-statement as a String
	 */
	public abstract String createTableStatement();

	/**
	 * Override to set delete-statement. This method is called once at the
	 * initializiation of an GenDBPersitence object.
	 *
	 * @return the prepared delete-statement as a String
	 */
	protected abstract String deleteStatement();

	/**
	 * Override to set update-statement. This method is called once at the
	 * initializiation of a GenDBPersitence object.
	 *
	 * @return the prepared update-statement as a String. use '?' as a wildcard.
	 */
	protected abstract String updateStatement();

	/**
	 * Override to set insert-statement. This method is called once at the
	 * initializiation of a GenDBPersitence object.
	 *
	 * @return the prepared insert-statement as a String. use '?' as a wildcard.
	 */
	protected abstract String insertStatement();

	/**
	 * Override to set delete-statement. This method is called once at the
	 * initializiation of a GenDBPersitence object.
	 *
	 * @return the prepared delete-statement as a String. use '?' as a wildcard.
	 */
	protected abstract void delete(PreparedStatement statement, Type item) throws SQLException;

	/**
	 * deletes an item by its id
	 *
	 * @param item
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Type item) throws SQLException {
		return manipulate(DELETE, item);
	}

	/**
	 * @param rs current row of resultset
	 * @return one Object of this type
	 * @throws SQLException
	 */
	public abstract Type fromResultSet(ResultSet rs) throws SQLException;

	/**
	 * @return connection
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException {
		return dbAccess.getConnection();
	}

	protected abstract void insert(PreparedStatement statement, Type item) throws SQLException;

	/**
	 * inserts an item and returns a new generated id
	 *
	 * @param item
	 * @return the database id
	 * @throws SQLException
	 */
	public boolean insert(Type item) throws SQLException {
		return manipulate(INSERT, item);
	}

	private boolean manipulate(int function, Type item) throws SQLException {
		Connection connection = null;
		int rowsAffected = 0;
		try {
			connection = getConnection();
			PreparedStatement statement = null;
			switch (function) {
				case UPDATE:
					statement = connection.prepareStatement(updateStatement);
					update(statement, item);
					break;
				case DELETE:
					statement = connection.prepareStatement(deleteStatement);
					delete(statement, item);
					break;
				case INSERT:
					statement = connection.prepareStatement(insertStatement);
					insert(statement, item);
					break;
				default:
					return false;
			}
			boolean isResultSet = statement.execute();
			assert !isResultSet;
			rowsAffected = statement.getUpdateCount();
			statement.close();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return rowsAffected == 1;
	}

	/**
	 * return LinkedList of type t
	 *
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	protected List<Type> select(String whereClause) throws SQLException {
		Connection connection = null;
		List<Type> list = new LinkedList<Type>();
		try {
			connection = getConnection();
			Statement statement = connection.createStatement();
			String sql = (whereClause != null) ? selectStatement + " " + whereClause + ";" : selectStatement + ";";
			ResultSet rs = statement.executeQuery(sql);
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

	/**
	 * selects all element of this type
	 *
	 * @return
	 * @throws SQLException
	 */
	public List<Type> selectAll() throws SQLException {
		return select(null);
	}

	protected abstract void update(PreparedStatement statement, Type item) throws SQLException;

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @throws IllegalStateException
	 */
	public boolean update(Type item) throws SQLException {
		return manipulate(UPDATE, item);
	}
}
