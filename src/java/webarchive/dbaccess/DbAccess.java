package webarchive.dbaccess;

import java.sql.Connection;
import java.sql.SQLException;
//TODO Tests

/**
 * DbAccess Interface provides a JDBC access to a database.
 * The type of DBMS depends on the implementation of the DBAccess
 * @author ccwelich
 */
public interface DbAccess {
	/**
	 * get a connection to the database
	 * @return connection
	 * @throws SQLException 
	 */
	public Connection getConnection() throws SQLException;

}